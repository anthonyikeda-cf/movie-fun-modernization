package org.superbiz.moviefun.albumsapi;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.superbiz.moviefun.blobstore.Blob;
import org.superbiz.moviefun.blobstore.BlobStore;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static java.lang.String.format;

public class CoverCatalog {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private BlobStore blobStore;

    @Autowired
    public CoverCatalog(BlobStore aBlobStore) {
        this.blobStore = aBlobStore;
    }

    void uploadCover(long albumId, InputStream inputStream, String contentType) throws IOException {
        logger.debug("Uploading cover for album with id {}", albumId);

        Blob coverBlob = new Blob(
                coverBlobName(albumId),
                inputStream,
                contentType
        );

        blobStore.put(coverBlob);
    }

    @HystrixCommand(fallbackMethod = "buildDefaultCoverBlob")
    Blob getCover(long albumId) throws IOException {
        Optional<Blob> maybeCoverBlob = blobStore.get(coverBlobName(albumId));

        return maybeCoverBlob.orElseGet(this::buildDefaultCoverBlob);
    }

    Blob buildDefaultCoverBlob(long albumId) {
        return buildDefaultCoverBlob();
    }

    private Blob buildDefaultCoverBlob() {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream input = classLoader.getResourceAsStream("default-cover.jpg");

        return new Blob("default-cover", input, MediaType.IMAGE_JPEG_VALUE);
    }

    private String coverBlobName(long albumId) {
        return format("covers/%d", albumId);
    }

}
