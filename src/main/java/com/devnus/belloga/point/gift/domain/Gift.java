package com.devnus.belloga.point.gift.domain;

import com.devnus.belloga.point.common.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "gift")
@Getter
@NoArgsConstructor
@NamedEntityGraph(name = "Gift.fetchGifticon", attributeNodes = {
        @NamedAttributeNode("applyGiftList"),
        @NamedAttributeNode("gifticonList")
})
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

    @Column(name = "gift_status")
    @Enumerated(EnumType.STRING)
    private GiftStatus giftStatus;

    @Column(name = "expected_draw_date")
    @Temporal(TemporalType.DATE)
    private Date expectedDrawDate;

    @OneToMany(mappedBy = "gift", fetch = FetchType.LAZY)
    private Set<ApplyGift> applyGiftList = new HashSet<>();

    @OneToMany(mappedBy = "gift", fetch = FetchType.LAZY)
    private Set<Gifticon> gifticonList = new HashSet<>();

    @Builder
    public Gift(GiftType giftType, String title, String adminId, Date expectedDrawDate) {
        this.adminId = adminId;
        this.giftType = giftType;
        this.title = title;
        this.expectedDrawDate = expectedDrawDate;
        this.giftStatus = GiftStatus.WAITING;
    }

    public void changeGiftStatusToDone() {
        this.giftStatus = GiftStatus.DONE;
    }
    public void addGifticon(Gifticon gifticon) {
        gifticon.setGift(this);
        this.gifticonList.add(gifticon);
    }
}
