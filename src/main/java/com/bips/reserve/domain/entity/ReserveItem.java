package com.bips.reserve.domain.entity;

import com.bips.reserve.domain.value.ReserveStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * USER의 예약서
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "reserve_item")
@Getter
public class ReserveItem extends BaseEntity {

    @Column(name = "reserve_item_id")
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brest_id")
    private Brest Brest;

    @Enumerated(value = EnumType.STRING)
    private ReserveStatus status = ReserveStatus.예약완료;

    @Column(nullable = false)
    private String btableName;
    @Column(nullable = false)
    private String reserveDate;
    @Column(nullable = false)
    private int reserveTime;

    @Builder(builderMethodName = "createReserveItem")
    public ReserveItem(
            User user, Brest Brest, ReserveStatus status, String reserveDate, int reserveTime, String btableName) {
        this.user = user;
        this.Brest = Brest;
        this.status = status;
        this.reserveDate = reserveDate;
        this.reserveTime = reserveTime;
        this.btableName = btableName;
        this.createAt = LocalDateTime.now();
    }

    //==비즈니스 로직==//
    //예약 날짜 및 예약 시간 update
    public void updateDateAndTime(String reserveDate,int reserveTime){
        this.reserveDate=reserveDate;
        this.reserveTime=reserveTime;
    }
}