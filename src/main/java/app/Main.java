package app;

import app.model.Entity;
import com.fasterxml.jackson.core.JsonProcessingException;

public class Main {

    private static final int STATUS_ERROR = 1;

    public static void main(String[] args){

        //args = new String[]{"data/valid.json", "5"};

        if(args.length < 2) {
            System.out.println("Usage:\n app <inputfile> <entityID>");
            return;
        }

        String fileName = args[0];
        String entityIdStr = args[1];
        int entityId = Entity.INVALID_ID;

        if(fileName == null || fileName.isEmpty()) {
            System.out.println("Path to input JSON file must be specified");
            System.exit(STATUS_ERROR);
        }

        if(entityIdStr == null || entityIdStr.isEmpty()) {
            System.out.println("Entity ID must be specified");
            System.exit(STATUS_ERROR);
        }

        try {
            entityId = Integer.parseInt(entityIdStr);
        } catch (NumberFormatException nfe){
            //nfe.printStackTrace();
            System.out.println("Entity ID must be integer number");
            System.exit(STATUS_ERROR);
        }

        App app = new App();

        try {
            app.parseFile(fileName);
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println(String.format("Error parsing JSON file: %s", e.getMessage()));
            System.exit(STATUS_ERROR);
        }

        Entity entity = app.findEntity(entityId);

        if(entity == null) {
            System.out.println(String.format("Entity with ID %d not found", entityId));
            System.exit(STATUS_ERROR);
        }

        app.clone(entity);

        try {
            String out = app.printElementsAndLinks();
            System.out.println(out);
        } catch (JsonProcessingException e) {
            //e.printStackTrace();
            System.out.println(String.format("Error generating JSON: %s", e.getMessage()));
            System.exit(STATUS_ERROR);
        }
    }
}
