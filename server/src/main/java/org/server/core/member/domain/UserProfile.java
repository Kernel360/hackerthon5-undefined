package org.server.core.member.domain;

public record UserProfile(
        String name,
        String id,
        String avatarUrl
) {}
