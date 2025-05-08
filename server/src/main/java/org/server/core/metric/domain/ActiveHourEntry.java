package org.server.core.metric.domain;

public record ActiveHourEntry(
        int hour,
        long seconds
) {

    public ActiveHourEntry {
        hour = (hour + 9 + 24) % 24;
    }
}
