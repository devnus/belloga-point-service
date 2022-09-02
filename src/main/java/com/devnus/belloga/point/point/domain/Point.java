package com.devnus.belloga.point.point.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


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

    @Column(name = "point_value")
    private Long pointValue;

    @OneToMany(mappedBy = "point", cascade = CascadeType.PERSIST)
    private List<TempPoint> tempPointList = new ArrayList<>();

    @Builder
    public Point(String labelerId, Long pointValue) {
        this.labelerId = labelerId;
        this.pointValue = pointValue;
    }

    public void addTempPoint(TempPoint tempPoint) {
        tempPoint.setPoint(this);
        this.tempPointList.add(tempPoint);
    }

    public void decreasePoint(Long pointValue) {
        this.pointValue -= pointValue;
    }
}
