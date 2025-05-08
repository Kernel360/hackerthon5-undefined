package org.server.global.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LocalDateToInstant {

    public static final ZoneId SEOUL_ZONE = ZoneId.of("Asia/Seoul");

    public InstantRange getBoundaryInstants(LocalDate date, ZoneId zone) {
        return new InstantRange(
                date
                        .minusDays(1)
                        .atTime(23, 59, 59)
                        .atZone(zone)
                        .toInstant(),
                date
                        .plusDays(1)
                        .atStartOfDay(zone)
                        .toInstant()
        );
    }

    public record InstantRange(Instant start, Instant end) {
    }
}
