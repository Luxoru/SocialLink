package me.luxoru.sociallink.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
@UtilityClass
public final class MojangUtils {
    public static final String NAME_TO_UUID = "https://api.mojang.com/users/profiles/minecraft/";

    /**
     * Get the uuid associated with the given name
     * asynchronously.
     *
     * @param name the name of the Minecraft account to get
     * @return the Minecraft account in a future, null if none
     * @see CompletableFuture for future
     */
    public static CompletableFuture<String> getUUIDAsync(@NonNull String name) {
        return CompletableFuture.supplyAsync(() -> getNameToUuid(name));
    }

    /**
     * Get the Minecraft account associated with the given name.
     *
     * @param name the name of the Minecraft account to get
     * @return the Minecraft account, null if none
     */
    public static String getNameToUuid(@NonNull String name) {
        try {
            // Send a request to the Mojang API to get the UUID of the given name
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(NAME_TO_UUID + name))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json = response.body(); // The returned json response

            JsonElement jsonElement = JsonParser.parseString(json);
            if (jsonElement.isJsonNull()) { // If the json element is JsonNull, return null
                return null;
            }
            JsonObject jsonObject = (JsonObject) jsonElement; // Parse the json response
            return untrim(jsonObject.get("id").getAsString()).toString();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Get the untrimmed version of the given UUID.
     *
     * @param trimmedUUID the trimmed UUID
     * @return the untrimmed UUID
     */
    @NonNull
    public static UUID untrim(@NonNull String trimmedUUID) {
        return UUID.fromString(
                trimmedUUID.substring(0, 8) + "-" +
                        trimmedUUID.substring(8, 12) + "-" +
                        trimmedUUID.substring(12, 16) + "-" +
                        trimmedUUID.substring(16, 20) + "-" +
                        trimmedUUID.substring(20, 32)
        );
    }
}
