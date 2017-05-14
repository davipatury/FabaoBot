/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.davipatury.fabaobot.apis;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.ThumbnailDetails;
import com.google.api.services.youtube.model.Video;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author Davi
 */
public class YoutubeAPI {
    
    private final String API_KEY;
    private final YouTube youtube;
    
    public YoutubeAPI(String apiKey) {
        this.API_KEY = apiKey;
        this.youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), request -> {}).setApplicationName("youtube-bot-api").build();
    }
    
    public void getVideoById(String videoId, Consumer<Video> success) {
        getVideoById(videoId, success, obj -> {});
    }
    
    public void getVideoById(String videoId, Consumer<Video> success, Consumer error) {
        try {
            YouTube.Videos.List videos = youtube.videos().list("id,snippet,statistics");
            videos.setKey(API_KEY);
            videos.setId(videoId);
            
            List<Video> videosResults = videos.execute().getItems();
            if(videosResults.isEmpty()) {
                error.accept(videosResults);
            } else {
                success.accept(videosResults.get(0));
            }
        } catch (IOException ex) {
            error.accept(ex);
        }
    }
    
    public static Thumbnail getBetterThumbnail(ThumbnailDetails thumbnails) {
        if(thumbnails.getMaxres() != null) {
            return thumbnails.getMaxres();
        } else if(thumbnails.getHigh() != null) {
            return thumbnails.getHigh();
        } else if(thumbnails.getMedium() != null) {
            return thumbnails.getMedium();
        } else if(thumbnails.getStandard() != null) {
            return thumbnails.getStandard();
        } else {
            return thumbnails.getDefault();
        }
    }
}
