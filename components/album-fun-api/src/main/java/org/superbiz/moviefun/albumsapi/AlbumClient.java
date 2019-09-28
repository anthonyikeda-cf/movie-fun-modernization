package org.superbiz.moviefun.albumsapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

public class AlbumClient {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String albumsUrl;
    private RestOperations restOperations;

    private static ParameterizedTypeReference<List<AlbumInfo>> albumListType = new ParameterizedTypeReference<List<AlbumInfo>>() { };

    public AlbumClient(String url, RestOperations restOperations) {
        this.albumsUrl = url;
        this.restOperations = restOperations;
    }

    public void addAlbum(AlbumInfo album) {
        restOperations.postForEntity(albumsUrl, album, AlbumInfo.class);
    }

    public AlbumInfo find(long id) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(albumsUrl).path(String.valueOf(id));
        logger.debug("Loading album from {}", builder.toUriString());
        return restOperations.exchange(builder.toUriString(), HttpMethod.GET, null, AlbumInfo.class).getBody();
    }

    public List<AlbumInfo> getAlbums() {
        logger.debug("Listing albums at {}", albumsUrl);
        return restOperations.exchange(albumsUrl, HttpMethod.GET, null, albumListType).getBody();
    }

//    public void deleteAlbum(AlbumInfo album) {
//
//    }
//
//    public void updateAlbum(AlbumInfo album) {
//
//    }
}
