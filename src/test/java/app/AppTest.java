package app;

import app.exceptions.InvalidEntityLinkException;
import app.model.Entity;
import app.model.Link;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import parser.InvalidJsonFormatException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AppTest {

    private static final String VALID_JSON_FILE = "data/valid.json";
    private static final String VALID_CYCLIC_JSON_FILE = "data/valid-cyclic.json";
    private static final String VALID_NULL_JSON_FILE = "data/valid-null.json";
    private static final String INVALID_LINKS_JSON_FILE = "data/invalid-links.json";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Test that valid file is processed and that entities are cloned correctly
     * @throws InvalidJsonFormatException
     * @throws InvalidEntityLinkException
     * @throws IOException
     */
    @Test
    public void testValidFile() throws InvalidJsonFormatException, InvalidEntityLinkException, IOException {

        App app = new App();
        app.parseFile(VALID_JSON_FILE);
        Entity entity = app.findEntity(5);

        Assert.assertNotNull(entity);
        Assert.assertEquals(5, entity.getId());
        Assert.assertEquals("EntityB", entity.getName());

        app.clone(entity);

        Assert.assertTrue(app.getEntityMap().containsKey(12));
        Assert.assertEquals(12, app.getEntityMap().get(12).getId());
        Assert.assertEquals("EntityB", app.getEntityMap().get(12).getName());

        Assert.assertEquals(1, app.getEntityMap().get(12).getFromEntities().size());
        Assert.assertEquals(3, app.getEntityMap().get(12).getFromEntities().get(0).getId());
        Assert.assertEquals("EntityA", app.getEntityMap().get(12).getFromEntities().get(0).getName());

        Assert.assertEquals(1, app.getEntityMap().get(12).getToEntities().size());
        Assert.assertEquals(13, app.getEntityMap().get(12).getToEntities().get(0).getId());
        Assert.assertEquals("EntityC", app.getEntityMap().get(12).getToEntities().get(0).getName());

        Assert.assertTrue(app.getEntityMap().containsKey(13));
        Assert.assertEquals(13, app.getEntityMap().get(13).getId());
        Assert.assertEquals("EntityC", app.getEntityMap().get(13).getName());

        Assert.assertEquals(1, app.getEntityMap().get(13).getFromEntities().size());
        Assert.assertEquals(12, app.getEntityMap().get(13).getFromEntities().get(0).getId());
        Assert.assertEquals("EntityB", app.getEntityMap().get(13).getFromEntities().get(0).getName());

        Assert.assertEquals(1, app.getEntityMap().get(13).getToEntities().size());
        Assert.assertEquals(14, app.getEntityMap().get(13).getToEntities().get(0).getId());
        Assert.assertEquals("EntityD", app.getEntityMap().get(13).getToEntities().get(0).getName());
    }

    /**
     * Test that valid file is processed and that entities are cloned correctly and that links are created correctly
     * @throws InvalidJsonFormatException
     * @throws InvalidEntityLinkException
     * @throws IOException
     */
    @Test
    public void testValidFile_Links() throws InvalidJsonFormatException, InvalidEntityLinkException, IOException {

        App app = new App();
        app.parseFile(VALID_JSON_FILE);
        Entity entity = app.findEntity(5);

        Assert.assertNotNull(entity);
        Assert.assertEquals(5, entity.getId());
        Assert.assertEquals("EntityB", entity.getName());

        app.clone(entity);

        List<Link> links = Arrays.asList(
                new Link(3, 12),
                new Link(12, 13),
                new Link(13, 14)
        );

        Assert.assertTrue(app.getLinks().containsAll(links));
    }

    /**
     * Test that entities without links are handled correctly and can be cloned
     * @throws InvalidJsonFormatException
     * @throws InvalidEntityLinkException
     * @throws IOException
     */
    @Test
    public void testValidNullFile() throws InvalidJsonFormatException, InvalidEntityLinkException, IOException {

        App app = new App();
        app.parseFile(VALID_NULL_JSON_FILE);
        Entity entity = app.findEntity(3);

        Assert.assertNotNull(entity);
        Assert.assertEquals(3, entity.getId());
        Assert.assertEquals("EntityA", entity.getName());

        app.clone(entity);

        Assert.assertTrue(app.getEntityMap().containsKey(4));
        Assert.assertEquals(4, app.getEntityMap().get(4).getId());
        Assert.assertEquals("EntityA", app.getEntityMap().get(4).getName());

        Assert.assertEquals(0, app.getEntityMap().get(4).getFromEntities().size());
        Assert.assertEquals(0, app.getEntityMap().get(4).getToEntities().size());

        Assert.assertEquals(0, app.getLinks().size());
    }

    /**
     * Test that cyclic references are handled correctly
     * @throws InvalidJsonFormatException
     * @throws InvalidEntityLinkException
     * @throws IOException
     */
    @Test
    public void testValidCyclicFile() throws InvalidJsonFormatException, InvalidEntityLinkException, IOException {

        App app = new App();
        app.parseFile(VALID_CYCLIC_JSON_FILE);
        Entity entity = app.findEntity(5);

        Assert.assertNotNull(entity);
        Assert.assertEquals(5, entity.getId());
        Assert.assertEquals("EntityB", entity.getName());

        app.clone(entity);

        // 13 is the next bigger number than numbers already used in entities, so it is used as ID for cloned entity
        Assert.assertTrue(app.getEntityMap().containsKey(13));
        Assert.assertEquals("EntityB", app.getEntityMap().get(13).getName());

        Assert.assertEquals(1, app.getEntityMap().get(13).getFromEntities().size());
        Assert.assertEquals(3, app.getEntityMap().get(13).getFromEntities().get(0).getId());
        Assert.assertEquals("EntityA", app.getEntityMap().get(13).getFromEntities().get(0).getName());

        Assert.assertEquals(1, app.getEntityMap().get(13).getToEntities().size());
        Assert.assertEquals(14, app.getEntityMap().get(13).getToEntities().get(0).getId());
        Assert.assertEquals("EntityC", app.getEntityMap().get(13).getToEntities().get(0).getName());

        Assert.assertTrue(app.getEntityMap().containsKey(14));
        Assert.assertEquals(14, app.getEntityMap().get(14).getId());
        Assert.assertEquals("EntityC", app.getEntityMap().get(14).getName());

        Assert.assertEquals(1, app.getEntityMap().get(14).getFromEntities().size());
        Assert.assertEquals(13, app.getEntityMap().get(14).getFromEntities().get(0).getId());
        Assert.assertEquals("EntityB", app.getEntityMap().get(14).getFromEntities().get(0).getName());

        Assert.assertEquals(2, app.getEntityMap().get(14).getToEntities().size());
        Assert.assertEquals(15, app.getEntityMap().get(14).getToEntities().get(0).getId());
        Assert.assertEquals(16, app.getEntityMap().get(14).getToEntities().get(1).getId());
        Assert.assertEquals("EntityD", app.getEntityMap().get(14).getToEntities().get(0).getName());
        Assert.assertEquals("EntityE", app.getEntityMap().get(14).getToEntities().get(1).getName());

        Assert.assertTrue(app.getEntityMap().containsKey(16));
        Assert.assertEquals(16, app.getEntityMap().get(16).getId());
        Assert.assertEquals("EntityE", app.getEntityMap().get(16).getName());

        Assert.assertEquals(1, app.getEntityMap().get(16).getFromEntities().size());
        Assert.assertEquals(14, app.getEntityMap().get(16).getFromEntities().get(0).getId());
        Assert.assertEquals("EntityC", app.getEntityMap().get(16).getFromEntities().get(0).getName());

        Assert.assertEquals(1, app.getEntityMap().get(16).getToEntities().size());
        Assert.assertEquals(17, app.getEntityMap().get(16).getToEntities().get(0).getId());
        Assert.assertEquals("EntityA", app.getEntityMap().get(16).getToEntities().get(0).getName());

        Assert.assertEquals(0, app.getEntityMap().get(17).getToEntities().size());
    }

    /**
     * Test that cyclic references are handled correctly and that links are created correctly
     * @throws InvalidJsonFormatException
     * @throws InvalidEntityLinkException
     * @throws IOException
     */
    @Test
    public void testValidCyclicFile_Links() throws InvalidJsonFormatException, InvalidEntityLinkException, IOException {

        App app = new App();
        app.parseFile(VALID_CYCLIC_JSON_FILE);
        Entity entity = app.findEntity(5);

        Assert.assertNotNull(entity);
        Assert.assertEquals(5, entity.getId());
        Assert.assertEquals("EntityB", entity.getName());

        app.clone(entity);

        List<Link> links = Arrays.asList(
                new Link(3, 13),
                new Link(13, 14),
                new Link(14, 15),
                new Link(14, 16),
                new Link(16, 17));

        Assert.assertTrue(app.getLinks().containsAll(links));
    }

    /**
     * Test that invalid entity-link structure is handled correctly
     * @throws InvalidJsonFormatException
     * @throws InvalidEntityLinkException
     * @throws IOException
     */
    @Test
    public void testInvalidLinks() throws InvalidJsonFormatException, InvalidEntityLinkException, IOException {
        thrown.expect(InvalidEntityLinkException.class);

        App app = new App();
        app.parseFile(INVALID_LINKS_JSON_FILE);
    }
}
