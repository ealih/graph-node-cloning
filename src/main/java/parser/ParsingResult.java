package parser;

import app.model.Entity;
import app.model.Link;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that holds results of JSON parsing
 */
public class ParsingResult {
    private Map<Integer, Entity> entities;
    private List<Link> links;
    private int largestId;

    public ParsingResult() {
        this.entities = new HashMap<>();
        this.links = new ArrayList<>();
    }

    public Map<Integer, Entity> getEntities() {
        return entities;
    }

    public List<Link> getLinks() {
        return links;
    }

    public int getLargestId() {
        return largestId;
    }

    public void setLargestId(int largestId) {
        this.largestId = largestId;
    }
}