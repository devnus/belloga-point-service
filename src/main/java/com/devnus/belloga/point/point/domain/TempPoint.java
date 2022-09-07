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
@NamedEntityGraph(name = "TempPoint.point",
        attributeNodes = @NamedAttributeNode("point")
)
// 상태가 NOT_CHANGED 인 labelerId에 해당하는 임시포인트의 합을 반환
@NamedQuery(
        name="TempPoint.sumTempPointByLabelerId",
        query="select SUM(t.pointValue) " +
                "from TempPoint t inner join t.point p " +
                "where t.status = 'NOT_CHANGED' and p.labelerId = :labelerId"
)
public class TempPoint {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "labeling_uuid")
    private String labelingUUID; // 라벨링 고유값

    @Column(name = "point_value")
    private Long pointValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_id")
    private Point point;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TempPointStatus status; // 실제 포인트로 변환 여부

    @Builder
    public TempPoint(String labelingUUID, Long pointValue) {
        this.labelingUUID = labelingUUID;
        this.pointValue = pointValue;
        changeTmpPointStatus(TempPointStatus.NOT_CHANGED);
    }

    public void setPoint(Point point) {
        this.point = point;
    }
    public void changeTmpPointToPoint() {
        changeTmpPointStatus(TempPointStatus.CHANGED);
        this.point.increasePoint(this.pointValue);
    }
    public void changeTmpPointStatus(TempPointStatus status) {
        this.status = status;
    }
}
