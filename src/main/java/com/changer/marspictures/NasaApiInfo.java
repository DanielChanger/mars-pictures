package com.changer.marspictures;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Component
public class NasaApiInfo {

    @Value("${nasa.api.url}")
    private String url;
    @Value("${nasa.api.key}")
    private String apiKey;
    @Value("${nasa.api.sol.param-name}")
    private String solParamName;
    @Value("${nasa.api.key.param-name}")
    private String apiKeyParamName;
    @Value("${nasa.api.camera.param-name}")
    private String cameraParamName;


    public String buildMarsPicturesUri(int sol, String camera) {
        return UriComponentsBuilder.fromHttpUrl(url)
                .queryParam(solParamName, sol)
                .queryParam(apiKeyParamName, apiKey)
                .queryParamIfPresent(cameraParamName, Optional.ofNullable(camera))
                .toUriString();
    }
}
