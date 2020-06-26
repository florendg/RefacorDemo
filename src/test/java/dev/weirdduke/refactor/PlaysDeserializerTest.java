package dev.weirdduke.refactor;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import model.Play;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlaysDeserializerTest {

    @Test
    void shouldParseValidPlayJson() {
        Type type = new TypeToken<Map<String, Play>>(){}.getType();
        String json = """
                {
                    "hamlet": {
                        "name": "Hamlet",
                        "type": "drama"
                    }
                }
                """;
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(type,new PlaysDeserializer())
                .create();
        Map<String,Play> result = gson.fromJson(json,type);
        assertNotNull(result);
        assertTrue(result.containsKey("hamlet"));
        Play play = result.get("hamlet");
    }



}