package de.llalon.gitbackup.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GitProvider {
    Gitlab("gitlab"),
    Github("github");

    private final String qualifier;

    @Override
    public String toString() {
        return this.qualifier;
    }
}
