package parser;

import java.io.IOException;

public interface JsonFileParser {
    ParsingResult parse() throws IOException, InvalidJsonFormatException;
}
