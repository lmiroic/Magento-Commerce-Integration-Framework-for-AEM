package com.venia.core.services.impl;

import com.google.gson.Gson;
import com.venia.core.models.unsplash.UnsplashResponse;
import com.venia.core.models.unsplash.dtos.UnsplashImage;
import com.venia.core.services.UnsplashService;
import com.venia.core.services.config.UnsplashServiceConfig;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Component(service = UnsplashService.class, immediate = true)
@Designate(ocd = UnsplashServiceConfig.class)
public class UnsplashServiceImpl implements UnsplashService {

    private UnsplashServiceConfig config;

    @Activate
    @Modified
    protected void activate(final UnsplashServiceConfig config) {
        this.config = config;
    }

    private static final Gson GSON = new Gson();

    public Optional<List<UnsplashImage>> promptUnsplash(final String searchTerm, final int limit, final String backupSearchTerm) {
        return Optional.of(this.sendRequestToUnsplash(searchTerm, limit))
                .filter(Optional::isPresent)
                .orElse(this.sendRequestToUnsplash(backupSearchTerm, 1));
    }

    private Optional<List<UnsplashImage>> sendRequestToUnsplash(final String searchTerm, final int limit) {
        final String encodedSearchTerm = URLEncoder.encode(searchTerm, StandardCharsets.UTF_8);
        final String apiUrl = String.format("https://api.unsplash.com/search/photos?page=1&query=%s&per_page=%d", encodedSearchTerm, limit);

        try {
            final HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(apiUrl))
                    .header("Authorization", "Client-ID " + this.config.accessKey())
                    .build();
            final HttpClient httpClient = HttpClient.newHttpClient();

            final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                final UnsplashResponse unsplashResponse = GSON.fromJson(response.body(), UnsplashResponse.class);
                final List<UnsplashImage> images = unsplashResponse.getResults();
                return Optional.ofNullable(images);
            } else {
                return Optional.empty();
            }
        } catch (final IOException | InterruptedException | URISyntaxException e) {
            return Optional.empty();
        }
    }

}
