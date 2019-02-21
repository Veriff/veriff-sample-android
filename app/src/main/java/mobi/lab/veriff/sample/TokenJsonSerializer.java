package mobi.lab.veriff.sample;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import mobi.lab.veriff.sample.data.TokenPayload;

public class TokenJsonSerializer implements JsonSerializer<TokenPayload> {

    @Override
    public JsonElement serialize(TokenPayload foo, Type type, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        JsonObject verification = new JsonObject();
        JsonObject document = new JsonObject();
        document.add("number", context.serialize(foo.getVerification().getDocument().getNumber()));
        document.add("type", context.serialize(foo.getVerification().getDocument().getType()));
        document.add("country", context.serialize(foo.getVerification().getDocument().getCountry()));
        verification.add("document", document);
        JsonObject additional = new JsonObject();
        if (foo.getVerification().getAdditionalData().containsKey("placeOfResidence")) {
            additional.add("placeOfResidence", context.serialize(foo.getVerification().getAdditionalData().get("placeOfResidence")));
        }
        if (foo.getVerification().getAdditionalData().containsKey("citizenship")) {
            additional.add("citizenship", context.serialize(foo.getVerification().getAdditionalData().get("citizenship")));
        }
        verification.add("additionalData", additional);
        verification.add("timestamp", context.serialize(foo.getVerification().getTimestamp()));
        verification.add("lang", context.serialize(foo.getVerification().getLang()));
        verification.add("features", context.serialize(foo.getVerification().getFeatures()));
        JsonObject person = new JsonObject();
        person.add("firstName", context.serialize(foo.getVerification().getPerson().getFirstName()));
        person.add("idNumber", context.serialize(foo.getVerification().getPerson().getIdNumber()));
        person.add("lastName", context.serialize(foo.getVerification().getPerson().getLastName()));
        verification.add("person", person);
        result.add("verification", verification);
        return result;
    }

}
