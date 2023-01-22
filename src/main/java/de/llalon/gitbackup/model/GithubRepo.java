package de.llalon.gitbackup.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubRepo implements GitRepo {

    @JsonProperty("owner")
    private final GithubOwner githubOwner;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("html_url")
    private final String url;

    @JsonProperty("private")
    private final boolean isPrivate;

    @JsonIgnore
    private final GitProvider provider = GitProvider.Github;

    public String getOwner() {
        return this.githubOwner.getLogin();
    }
}
