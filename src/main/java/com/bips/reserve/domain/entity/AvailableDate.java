package com.bips.reserve.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.bips.reserve.exception.btable.NotEnoughStockException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "available_date")
@Getter
public class AvailableDate {

    @Id
    @GeneratedValue
    @Column(name = "available_data_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brest_id")
    private Brest brest;

    @Column(nullable = false)
    private String date;

    // 양방향
    @OneToMany(mappedBy = "availableDate", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"availableDate"})
    private List<AvailableTime> availableTimes = new ArrayList<>();

    // 일일 수용 가능 인원
    @Column(name = "accept_count")
    private Integer acceptCount;

    public void cancel() {
        this.acceptCount++;
        this.enabled=true;
    }

    public void decreaseCount() {
        int restStock=this.acceptCount-1;
        if(restStock==0){
            setEnabled(false);
        }
        if(restStock<0){
            throw new NotEnoughStockException("예약 가능한 테이블이 없습니다.");
        }

        this.acceptCount=restStock;
    }

    @Type(type = "yes_no")
    private Boolean enabled = true;

    public void setEnabled(boolean flag) {
        this.enabled = flag;
    }

    // 양방향 연관관계 편의 메서드
    public void addBrest(Brest brest) {
        this.brest = brest;
        brest.getAvailableDates().add(this);
    }

    @Builder(builderMethodName = "createAvailableDate")
    public AvailableDate(String date,Integer acceptCount){
        this.date=date;
        this.acceptCount=acceptCount;
    }

    //레스토랑 상세내용 수정 시, count update
    public void updateAcceptCount(Integer acceptCount){
        this.acceptCount=acceptCount;
    }
}