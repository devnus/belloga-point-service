package com.devnus.belloga.point.gift.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "apply_gift")
@Getter
@NoArgsConstructor
public class ApplyGift {
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
