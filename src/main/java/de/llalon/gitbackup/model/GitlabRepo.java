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
public class GitlabRepo implements GitRepo {

    @JsonProperty("path")
    private final String name;

    @JsonProperty("http_url_to_repo")
    private final String url;

    @JsonProperty("visibility")
    private final String visibility;

    @JsonProperty("path_with_namespace")
    private final String path;

    @JsonIgnore
    private final GitProvider provider = GitProvider.Gitlab;

    public String getOwner() {
        return this.path.split("/")[0];
    }

    public boolean isPrivate() {
        return "private".equals(this.visibility);
    }
}
