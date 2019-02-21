package parser;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

public class FileParserTest {

    private static final String VALID_JSON_FILE = "data/valid.json";
    private static final String INVALID_JSON_FILE = "data/invalid.json";
    private static final String INVALID_JSON_MISSING_FIELDS_FILE = "data/invalid-json-missing-fields.json";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Test that valid json file is parsed correctly
     * @throws IOException
     * @throws InvalidJsonFormatException
     */
    @Test
    public void testValidJson() throws IOException, InvalidJsonFormatException {

        JsonFileParser parser = new FileParser(VALID_JSON_FILE);
        ParsingResult parsingResult = parser.parse();

        Assert.assertNotNull(parsingResult);
        Assert.assertEquals(4, parsingResult.getEntities().size());
        Assert.assertEquals(4, parsingResult.getLinks().size());
        Assert.assertEquals(11, parsingResult.getLargestId());
    }

    /**
     * Test that invalid json is handled correctly
     * @throws IOException
     * @throws InvalidJsonFormatException
     */
    @Test
    public void testInvalidJson() throws IOException, InvalidJsonFormatException {
        thrown.expect(InvalidJsonFormatException.class);

        JsonFileParser parser = new FileParser(INVALID_JSON_FILE);
        parser.parse();
    }

    /**
     * Test that valid json but with missing necessary fields is handled correctly
     * @throws IOException
     * @throws InvalidJsonFormatException
     */
    @Test
    public void testInvalidJson_missingFields() throws IOException, InvalidJsonFormatException {
        thrown.expect(InvalidJsonFormatException.class);

        JsonFileParser parser = new FileParser(INVALID_JSON_MISSING_FIELDS_FILE);
        parser.parse();
    }
}
