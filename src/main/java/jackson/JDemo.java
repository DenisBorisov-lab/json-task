package jackson;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.File;
import java.io.IOException;

import static java.lang.System.*;

public class JDemo {
    public static void main(String[] args) throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(new File("C:\\Users\\maibo\\IdeaProjects\\JSON\\src\\main\\resources\\person.json"));
        while (!parser.isClosed()) {
            JsonToken jsonToken = parser.nextToken();
            if (jsonToken == null) {
                break;
            }
            out.printf(
                    "jsonToken = %s [%s] [%b] [%s]\n",
                    jsonToken, jsonToken.asString(),
                    jsonToken.isNumeric(),
                    parser.getValueAsString()
            );
        }
    }
}
