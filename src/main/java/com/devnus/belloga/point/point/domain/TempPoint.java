package com.devnus.belloga.point.point.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 라벨링 수행 시 받는 임시 포인트, 1 대 다
 */
@Entity
@Table(name = "temp_point")
@Getter
@NoArgsConstructor
public class TempPoint {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "labeler_id")
    private String labelerId;

    @Column(name = "labeling_uuid")
    private String labelingUUID; // 라벨링 고유값

    @Column(name = "point_value")
    private Long pointValue;

    @Builder
    public TempPoint(String labelerId, String labelingUUID, Long pointValue) {
        this.labelerId = labelerId;
        this.labelingUUID = labelingUUID;
        this.pointValue = pointValue;
    }
}
