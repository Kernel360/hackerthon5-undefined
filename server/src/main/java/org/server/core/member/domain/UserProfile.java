package org.server.core.member.domain;

import java.util.Map;

public record UserProfile(
        String name,
        String id,
        String avatarUrl
) {}
