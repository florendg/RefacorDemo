package dev.weirdduke.refactor;

import com.google.gson.*;
import model.Play;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.stream.Collectors;


public class PlaysDeserializer implements JsonDeserializer<Map<String,Play>> {

    private static final GsonBuilder builder = new GsonBuilder();
    private static final Gson gson = builder.create();

    @Override
    public Map<String, Play> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return json.getAsJsonObject()
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        e -> gson.fromJson(e.getValue(), Play.class)));
    }
}
