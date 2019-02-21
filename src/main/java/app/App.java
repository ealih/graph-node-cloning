package app;

import app.exceptions.InvalidEntityLinkException;
import app.model.Entity;
import app.model.Link;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import parser.FileParser;
import parser.InvalidJsonFormatException;
import parser.ParsingResult;

import java.io.IOException;
import java.util.*;

public class App {

    private Map<Integer, Entity> entityMap;
    private List<Link> links;

    /**
     * Cloned entities will have ID that is largestId incremented by one
     */
    private int largestId;

    public App(){
        entityMap = new HashMap<>();
        links = new ArrayList<>();
    }

    /**
     * Parses JSON input file
     * @param fileName Path of the file
     * @throws IOException
     */
    public void parseFile(String fileName) throws IOException, InvalidJsonFormatException, InvalidEntityLinkException {

        FileParser parser = new FileParser(fileName);
        ParsingResult result = parser.parse();

        entityMap = result.getEntities();
        links = result.getLinks();
        largestId = result.getLargestId();

        verifyAndBuildGraph();
    }

    /**
     * Find entity by its ID
     * @param entityId ID of the entity
     * @return Entity
     */
    public Entity findEntity(int entityId){
        return entityMap.get(entityId);
    }

    /**
     * Prints entities and links as JSON to stdout
     * @throws JsonProcessingException
     */
    public String printElementsAndLinks() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        Map<String, Collection<?>> outputMap = new HashMap<>();
        outputMap.put("entities", entityMap.values());
        outputMap.put("links", links);

        return mapper.writeValueAsString(outputMap);
    }

    /**
     * Recursively clones entities linked to this entity (including this entity itself)
     * @param entity Starting entity to clone
     */
    public void clone(Entity entity){
        cloneEntities(entity, new ArrayList<>(), entity.getFromEntities().toArray(new Entity[0]));
    }

    /**
     * Recursively clones given entity including entities that link to this entity
     * @param entity Entity to clone
     * @param attachTo List of entities to attach clones to
     * @param visited Empty list to keep info about visited entities and clones
     */
    private void cloneEntities(Entity entity, List<Integer> visited, Entity... attachTo){

        Entity clone = new Entity();
        clone.setId(++largestId);
        clone.setDescription(entity.getDescription());
        clone.setName(entity.getName());

        for(Entity e : attachTo) {
            clone.getFromEntities().add(e);
            e.getToEntities().add(clone);

            links.add(new Link(e.getId(), clone.getId()));
        }

        entityMap.put(clone.getId(), clone);
        visited.add(entity.getId());
        visited.add(clone.getId());

        for (Entity neighbour : entity.getToEntities()) {

            if (!visited.contains(neighbour.getId())) {
                cloneEntities(neighbour, visited, clone);
            }
        }
    }

    /**
     * Get entities as map with entity IDs as keys
     * @return Entities map
     */
    public Map<Integer, Entity> getEntityMap(){
        return entityMap;
    }

    /**
     * Get links
     * @return links list
     */
    public List<Link> getLinks(){
        return links;
    }

    /**
     * Verify that links correspond to existing entities
     * @throws InvalidEntityLinkException
     */
    private void verifyAndBuildGraph() throws InvalidEntityLinkException {

        for(Link link : links) {

            if(!entityMap.containsKey(link.getFrom())){
                throw new InvalidEntityLinkException(String.format("No entity found with ID %d", link.getFrom()));
            }

            if(!entityMap.containsKey(link.getTo())){
                throw new InvalidEntityLinkException(String.format("No entity found with ID %d", link.getFrom()));
            }

            entityMap.get(link.getFrom()).getToEntities().add(entityMap.get(link.getTo()));
            entityMap.get(link.getTo()).getFromEntities().add(entityMap.get(link.getFrom()));
        }
    }
}
