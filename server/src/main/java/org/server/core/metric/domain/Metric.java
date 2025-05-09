package org.server.core.metric.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.server.core.site.domain.SiteDomain;

@Getter
@Entity
@Table(name = "metrics")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Metric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private SiteDomain siteDomain;

    @Embedded
    private MetricMetadata metadata;

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Metric(
            Long memberId,
            SiteDomain siteDomain,
            MetricMetadata metadata
    ) {
        this.memberId = memberId;
        this.siteDomain = siteDomain;
        this.metadata = metadata;
    }
}
