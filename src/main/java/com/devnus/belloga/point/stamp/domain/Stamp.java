package com.devnus.belloga.point.stamp.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Stamp 엔티티이다.
 */
@Entity
@Table(name = "stamp")
@Getter
@NoArgsConstructor
public class Stamp {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "labeler_id")
    private String labelerId;

    @Column(name = "stamp_value")
    private Integer stampValue;

    @Builder
    public Stamp(String labelerId) {
        this.stampValue = 0;
        this.labelerId = labelerId;
    }

    public void increaseStamp(Integer stamp) {
        if(stamp < 0) return;
        this.stampValue += stamp;
    }

    public void decreaseStamp(Integer stamp) {
        if(this.stampValue - stamp < 0) return;
        this.stampValue -= stamp;
    }

}
