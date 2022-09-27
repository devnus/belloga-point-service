package com.devnus.belloga.point.gift.domain;

import com.devnus.belloga.point.common.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "apply_gift")
@Getter
@NoArgsConstructor
public class ApplyGift extends BaseTimeEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "labeler_id")
    private String labelerId;

    @Column(name = "apply_status")
    @Enumerated(EnumType.STRING)
    private ApplyStatus applyStatus; //당첨 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gift_id")
    private Gift gift;

    @Builder
    public ApplyGift(String labelerId, Gift gift) {
        this.labelerId = labelerId;
        this.gift = gift;
        this.applyStatus = ApplyStatus.WAITING;
    }
}
