package ru.demo;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.File;
import java.io.IOException;

public class JGeneration {


    public static void main(String[] args) throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonGenerator generator = factory.createGenerator(new File("person_new.json"), JsonEncoding.UTF8);

        generator.writeStartObject();
        generator.writeStringField("firstName", "Jason");
        generator.writeStringField("lastName", "POJO");
        generator.writeNumberField("age", 42);

        generator.writeFieldName("address");

        generator.writeStartObject();
        generator.writeStringField("Street", "Baker-Street");
        generator.writeStringField("Country", "USA");
        generator.writeStringField("State", "Texas");
        generator.writeNumberField("Zip-code", 010203);
        generator.writeEndObject();

        generator.writeFieldName("phone numbers");
        generator.writeStartArray();

        generator.writeStartObject();
        generator.writeStringField("type", "home");
        generator.writeStringField("number", "+79123945");
        generator.writeEndObject();

        generator.writeStartObject();
        generator.writeStringField("type", "mobile");
        generator.writeStringField("number", "+73126409234");
        generator.writeEndObject();

        generator.writeEndArray();

        generator.writeEndObject();
        generator.close();
        System.out.println("ok");
    }
}
