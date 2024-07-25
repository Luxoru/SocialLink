package me.luxoru.sociallink.message;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.luxoru.sociallink.message.friend.FriendRequestPendingMessage;

import java.io.IOException;

/**
 * A factory class for creating custom {@link TypeAdapter} instances for serializing and deserializing {@link Message} objects.
 * This factory handles different types of {@link Message} by including a type identifier in the serialized JSON.
 */
public class MessageAdapterFactory implements TypeAdapterFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (!Message.class.isAssignableFrom(type.getRawType())) {
            return null; // This factory only handles Message types
        }

        final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
        final TypeAdapter<FriendRequestPendingMessage> friendRequestPendingMessageTypeAdapter = gson.getDelegateAdapter(this, TypeToken.get(FriendRequestPendingMessage.class));

        return new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter out, T value) throws IOException {
                JsonObject obj = new JsonObject();
                if (value instanceof FriendRequestPendingMessage) {
                    obj.addProperty("type", "FriendRequestPendingMessage");
                    obj.add("data", friendRequestPendingMessageTypeAdapter.toJsonTree((FriendRequestPendingMessage) value));
                }
                elementAdapter.write(out, obj);
            }

            @Override
            public T read(JsonReader in) throws IOException {
                JsonObject obj = elementAdapter.read(in).getAsJsonObject();
                if (obj.get("type") == null) {
                    return null;
                }

                String type = obj.get("type").getAsString();
                JsonElement data = obj.get("data");

                switch (type) {
                    case "FriendRequestPendingMessage":
                        return (T) friendRequestPendingMessageTypeAdapter.fromJsonTree(data);
                    default:
                        throw new IllegalArgumentException("Unknown type: " + type);
                }
            }
        }.nullSafe();
    }
}
