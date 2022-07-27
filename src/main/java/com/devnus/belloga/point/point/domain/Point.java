package com.devnus.belloga.point.point.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Point 엔티티이다.
 */
@Entity
@Table(name = "point")
@Getter
@NoArgsConstructor
public class Point {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "labeler_id")
    private String labelerId;

    @Column(name = "is_tmp_point")
    private Boolean isTmpPoint;

    @Column(name = "point_value")
    private Long pointValue;

    @Builder
    public Point(String labelerId, Long pointValue, Boolean isTmpPoint) {
        this.labelerId = labelerId;
        this.pointValue = pointValue;
        this.isTmpPoint = isTmpPoint;
    }
}
