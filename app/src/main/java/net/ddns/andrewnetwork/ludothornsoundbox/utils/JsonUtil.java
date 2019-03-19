package net.ddns.andrewnetwork.ludothornsoundbox.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonUtil {

    private static GsonBuilder gsonBuilder;

    static {
        gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        gsonBuilder.setDateFormat("dd/MM/yyyy");
        gsonBuilder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                Date date = parse(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ"), json.getAsString());
                if (date == null) {
                    date = parse(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"), json.getAsString());
                }
                if (date == null) {
                    date = parse(new SimpleDateFormat("dd/MM/yyyy"), json.getAsString());
                }
                if (date == null) {
                    date = new Date();
                }
                return date;
            }
        });
    }

    private static Date parse(SimpleDateFormat dateFormat, String string) {
        try {
            return dateFormat.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Gson getGson() {
        return gsonBuilder.create();
    }


}
