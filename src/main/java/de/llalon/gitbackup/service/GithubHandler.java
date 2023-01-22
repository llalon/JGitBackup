package de.llalon.gitbackup.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import de.llalon.gitbackup.config.AppConfig;
import de.llalon.gitbackup.model.GitRepo;
import de.llalon.gitbackup.model.GithubRepo;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
@Qualifier("github")
public class GithubHandler implements GitHandler {

    private static final String ENDPOINT = "/user/repos";
    private static final String DEFAULT_HOST = "https://api.github.com/";

    private final AppConfig config;

    private URI getURI(int page) throws URISyntaxException {
        var host = config.getHost();
        var base = (host == null || host.isBlank()) ? DEFAULT_HOST : host;

        return new URIBuilder(new URI(base + ENDPOINT))
                .setParameter("page", String.valueOf(page))
                .build()
                .normalize();
    }

    private Map.Entry<String, String> getAuthHeader() {
        if (config.getToken() == null || config.getToken().isBlank()) {
            log.debug("Token is invalid: {}", config.getToken());
            throw new IllegalArgumentException("Token must be provided");
        }

        return Map.entry("Authorization", "token " + config.getToken());
    }

    public List<GitRepo> getAllRepos() throws Exception {
        log.debug("Getting all repos from Github");

        List<GitRepo> repos = new ArrayList<>();

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            var r = getRepoPage(i);
            if (r.isEmpty()) break;
            repos.addAll(r);
        }

        return repos;
    }

    private List<GitRepo> getRepoPage(int page) throws URISyntaxException, IOException, InterruptedException {
        log.debug("Getting repos page {} from Github", page);

        var headers = getAuthHeader();
        var request = HttpRequest.newBuilder()
                .uri(getURI(page).normalize())
                .headers(headers.getKey(), headers.getValue())
                .GET()
                .build();

        var response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());

        CollectionType typeReference =
                TypeFactory.defaultInstance().constructCollectionType(List.class, GithubRepo.class);

        if (response.statusCode() != 200) {
            log.error("Github API returned with non 200 status code: {}", response.statusCode());
            throw new RuntimeException("Non 200 response");
        }

        return new ObjectMapper().readValue(response.body(), typeReference);
    }
}
