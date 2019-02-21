package parser;

import app.model.Entity;
import app.model.Link;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.File;
import java.io.IOException;

/**
 * JSON parser implementation that uses Jackson streaming API to parse JSON.
 * FileParser avoids loading whole file into memory for processing by parsing the file on the fly
 * which is more efficient time-wise and memory-wise.
 */
public class FileParser implements JsonFileParser {

    private String filePath;

    public FileParser(String filePath){
        this.filePath = filePath;
    }

    /**
     * Parses supplied file
     * @return Parsing results, {@link ParsingResult}
     * @throws IOException
     * @throws InvalidJsonFormatException
     */
    @Override
    public ParsingResult parse() throws IOException, InvalidJsonFormatException {

        ParsingResult result = new ParsingResult();

        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(new File(filePath));

        while (parser.nextToken() != JsonToken.END_OBJECT) {

            String fieldName = parser.getCurrentName();

            if(fieldName == null) {
                continue;
            }

            if(!fieldName.equals("entities") && !fieldName.equals("links")){
                throw new InvalidJsonFormatException("entities or links fields not found");
            }

            if (fieldName.equals("entities")) {
                parseEntities(parser, result);
            } else {
                parseLinks(parser, result);
            }
        }

        parser.close();

        return result;
    }

    private void parseEntities(JsonParser parser, ParsingResult parsingResult) throws IOException, InvalidJsonFormatException {

        parser.nextToken();

        Entity entity = null;

        while (parser.nextToken() != JsonToken.END_ARRAY) {

            if(parser.getCurrentToken() == JsonToken.START_OBJECT){
                entity = new Entity();
            }

            if(entity == null) {
                throw new InvalidJsonFormatException("Missing START_OBJECT token");
            }

            String entityField = parser.getCurrentName();

            if(entityField != null) {

                switch (entityField) {
                    case "entity_id":
                        parser.nextToken();
                        entity.setId(parser.getIntValue());
                        break;
                    case "name":
                        parser.nextToken();
                        entity.setName(parser.getText());
                        break;
                    case "description":
                        parser.nextToken();
                        entity.setDescription(parser.getText());
                        break;
                }
            }

            if(parser.getCurrentToken() == JsonToken.END_OBJECT){

                if(entity.getId() == Entity.INVALID_ID) {
                    throw new InvalidJsonFormatException("entity_id property not found");
                }

                parsingResult.getEntities().put(entity.getId(), entity);

                if(entity.getId() > parsingResult.getLargestId()) {
                    parsingResult.setLargestId(entity.getId());
                }
            }
        }
    }

    private void parseLinks(JsonParser parser, ParsingResult parsingResult) throws IOException, InvalidJsonFormatException {

        parser.nextToken();

        Link link = null;

        while (parser.nextToken() != JsonToken.END_ARRAY) {

            if(parser.getCurrentToken() == JsonToken.START_OBJECT){
                link = new Link();
            }

            if(link == null) {
                throw new InvalidJsonFormatException("Missing START_OBJECT token");
            }

            String field = parser.getCurrentName();

            if(field != null) {

                switch (field) {
                    case "from":
                        parser.nextToken();
                        link.setFrom(parser.getIntValue());
                        break;
                    case "to":
                        parser.nextToken();
                        link.setTo(parser.getIntValue());
                        break;
                }
            }

            if(parser.getCurrentToken() == JsonToken.END_OBJECT){

                if(link.getFrom() == Link.INVALID_REF || link.getTo() == Link.INVALID_REF){
                    throw new InvalidJsonFormatException("Link fields from or to not found");
                }

                parsingResult.getLinks().add(link);
            }
        }
    }
}
