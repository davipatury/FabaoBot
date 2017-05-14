/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot.apis.twitch;

import net.davipatury.fabaobot.apis.twitch.entities.StreamImpl;
import net.davipatury.fabaobot.apis.twitch.entities.Stream;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.json.JSONObject;

/**
 *
 * @author Davi
 */
public class TwitchAPI {
    
    private final String API_KEY;
    
    public TwitchAPI(String apiKey) {
        this.API_KEY = apiKey;
    }
    
    public void searchStreams(String query, Consumer<List<Stream>> success) {
        searchStreams(query, 25, success, obj -> {});
    }
    
    public void searchStreams(String query, int limit, Consumer<List<Stream>> success) {
        searchStreams(query, limit, success, obj -> {});
    }
    
    public void searchStreams(String query, int limit, Consumer<List<Stream>> success, Consumer error) {
        try {
            HttpResponse<JsonNode> response = Unirest.get("https://api.twitch.tv/kraken/search/streams?query=" + query)
                    .header("Accept", "application/vnd.twitchtv.v5+json")
                    .header("Client-ID", API_KEY)
                    .queryString("limit", limit)
                    .asJson();
            if(response.getStatus() == 200) {
                JSONObject json = response.getBody().getObject();
                final List<Stream> streams = new ArrayList<>();
                json.getJSONArray("streams").forEach(obj -> {
                    streams.add(new StreamImpl(new JSONObject(obj.toString())));
                });
                success.accept(streams);
            } else {
                error.accept(response);
            }
        } catch (UnirestException ex) {
            error.accept(ex);
        }
    }
    
    
    public void getStreamById(String channelId, Consumer<Stream> success) {
        getStreamById(channelId, success, obj -> {});
    }
    
    public void getStreamById(String channelId, Consumer<Stream> success, Consumer error) {
        try {
            HttpResponse<JsonNode> response = Unirest.get("https://api.twitch.tv/kraken/streams/" + channelId)
                    .header("Accept", "application/vnd.twitchtv.v5+json")
                    .header("Client-ID", API_KEY)
                    .asJson();
            if(response.getStatus() == 200) {
                JSONObject json = response.getBody().getObject();
                if(json.isNull("stream")) {
                    error.accept(json.getJSONObject("stream"));
                } else {
                    success.accept(new StreamImpl(json));
                }
            } else {
                error.accept(response);
            }
        } catch (UnirestException ex) {
            error.accept(ex);
        }
    }
    
    public static String getBetterThumbnailUrl(Stream stream) {
        JSONObject preview = stream.getPreview();
        if(preview.has("large")) {
            return preview.getString("large");
        } else if(preview.has("medium")) {
            return preview.getString("medium");
        } else if(preview.has("small")) {
            return preview.getString("small");
        } else if(stream.getChannel().getLogoUrl() != null) {
            return stream.getChannel().getLogoUrl();
        }
        return null;
    }
}
