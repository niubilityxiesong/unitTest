package com.cleancode.unitTest.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class JsonFileReader {
    public static String toJsonString(String fileName) throws FileNotFoundException {
        FileReader fileReader = new FileReader("src/test/java/com/cleancode/unitTest/controller/data/" + fileName);
        JsonObject asJsonObject = JsonParser.parseReader(fileReader).getAsJsonObject();

        return asJsonObject.toString();
    }
}
