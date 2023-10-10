package com.sliit.travelhelp.Models;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ObjectIdDeserializer extends JsonDeserializer<Object> {

    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String value = jsonParser.getText();
        // Check if it's an ObjectId
        if (ObjectId.isValid(value)) {
            return new ObjectId(value);
        }
        // Check if it's an ISODate
        else if (isISODate(value)) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                Date date = dateFormat.parse(value);
                return date;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        // Handle other cases if needed
        return null;
    }

    private boolean isISODate(String value) {
        // You can implement a regex pattern or other checks to validate ISODate
        // For simplicity, let's assume any string starting with "ISODate(" is an ISODate
        return value.startsWith("ISODate(");
    }
}
