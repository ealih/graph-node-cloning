package parser;

/**
 * Thrown when specified JSON file has invalid structure
 */
public class InvalidJsonFormatException extends Exception {

    public InvalidJsonFormatException(String message) {
        super(message);
    }
}
