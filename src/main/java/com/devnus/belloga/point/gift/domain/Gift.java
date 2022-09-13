package com.devnus.belloga.point.gift.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "gift")
@Getter
@NoArgsConstructor
public class Gift {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "admin_id")
    private String adminId;

    @Column(name = "title")
    private String title;

    @Column(name = "gift_type")
    @Enumerated(EnumType.STRING)
    private GiftType giftType;

    @Column(name = "expected_draw_date")
    @Temporal(TemporalType.DATE)
    private Date expectedDrawDate;

    @Builder
    public Gift(GiftType giftType, String title, String adminId, Date expectedDrawDate) {
        this.adminId = adminId;
        this.giftType = giftType;
        this.title = title;
        this.expectedDrawDate = expectedDrawDate;
    }
}