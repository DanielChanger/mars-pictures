package com.changer.marspictures;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MarsPicturesService {

    private final NasaApiInfo nasaApiInfo;
    private final RestTemplate restTemplate;

    @Cacheable("pictures")
    public Optional<byte[]> findLargestPicture(int sol, String camera) {
        String uri = nasaApiInfo.buildMarsPicturesUri(sol, camera);

        return Objects.requireNonNull(restTemplate.getForObject(uri, JsonNode.class))
                .findValuesAsText("img_src")
                .parallelStream()
                .map(URI::create)
                .map(this::getPictureLocationUrlAndSize)
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .map(this::getPictureBytes);
    }

    @SneakyThrows
    private byte[] getPictureBytes(URI pictureUri) {
        return restTemplate.getForObject(pictureUri, byte[].class);
    }

    private Map.Entry<URI, Long> getPictureLocationUrlAndSize(URI src) {
        HttpHeaders httpHeaders = restTemplate.headForHeaders(src);
        URI location = httpHeaders.getLocation();
        URI imageFinalSrc = src;

        while (location != null) {
            httpHeaders =  restTemplate.headForHeaders(location);
            imageFinalSrc = location;
            location = httpHeaders.getLocation();
        }

        return new AbstractMap.SimpleEntry<>(imageFinalSrc, httpHeaders.getContentLength());
    }
}
