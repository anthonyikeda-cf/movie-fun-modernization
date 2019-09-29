package org.superbiz.moviefun.albums;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.superbiz.cloudfoundry.ServiceCredentials;
import org.superbiz.moviefun.blobstore.BlobStore;
import org.superbiz.moviefun.blobstore.S3Store;

@SpringBootApplication
public class AlbumServiceApplication {
    public static void main(String... args) {
        SpringApplication.run(AlbumServiceApplication.class, args);
    }

    @Bean
    ServiceCredentials serviceCredentials(@Value("${vcap.services}") String vcapServices) {
        return new ServiceCredentials(vcapServices);
    }

    @Bean
    public BlobStore blobStore(
            ServiceCredentials serviceCredentials,
            @Value("${vcap.services.photo-storage.credentials.endpoint:#{null}}") String endpoint
    ) {
        String photoStorageAccessKeyId = serviceCredentials.getCredential("photo-storage", "user-provided", "accessKey");
        String photoStorageSecretKey = serviceCredentials.getCredential("photo-storage", "user-provided", "secretKey");
        String photoStorageBucket = serviceCredentials.getCredential("photo-storage", "user-provided", "bucket");
        String otherEndpoint = serviceCredentials.getCredential("photo-storage", "user-provided", "endpoint");

        AWSCredentials credentials = new BasicAWSCredentials(photoStorageAccessKeyId, photoStorageSecretKey);

        AwsClientBuilder.EndpointConfiguration eConfig = null;
        if (!"".equals(endpoint)) {
            eConfig = new AwsClientBuilder.EndpointConfiguration(endpoint, "auto");
        } else {
            if (!"".equals(otherEndpoint)) {
                eConfig = new AwsClientBuilder.EndpointConfiguration(otherEndpoint, "auto");
            } else {
                eConfig = new AwsClientBuilder.EndpointConfiguration("https://storage.googleapis.com", "auto");
            }
        }

        AmazonS3 interopClient = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(eConfig)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();

        return new S3Store(interopClient, photoStorageBucket);
    }
}
