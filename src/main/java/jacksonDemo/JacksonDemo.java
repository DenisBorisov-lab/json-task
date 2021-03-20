package jacksonDemo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JacksonDemo {
    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(new File("C:\\Users\\maibo\\IdeaProjects\\JSON\\src\\main\\resources\\person.json"));
        System.out.printf("firstName = %s%n", rootNode.get("firstName"));
        System.out.printf("firstName = %s%n", rootNode.get("lastName"));

        JsonNode phoneNumbers = rootNode.get("phoneNumbers");
        for (int i = 0; i < phoneNumbers.size(); i++) {
            System.out.println(i);
            JsonNode jsonNode = phoneNumbers.get(i);
            System.out.println(jsonNode.get("type"));
            System.out.println(jsonNode.get("number"));
        }
    }
}
