package com.devnus.belloga.point.gift.domain;

import com.devnus.belloga.point.common.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "gifticon")
@Getter
@NoArgsConstructor
public class Gifticon extends BaseTimeEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "code")
    private String code;

    @Column(name = "expired_date")
    @Temporal(TemporalType.DATE)
    private Date expiredDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gift_id")
    private Gift gift;

    @Builder
    public Gifticon(Gift gift, String title, String code, Date expiredDate) {
        this.gift = gift;
        this.title = title;
        this.code = code;
        this.expiredDate = expiredDate;
    }
}
