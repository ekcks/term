package com.bips.reserve.domain.entity;

import com.bips.reserve.exception.btable.NotEnoughStockException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "btable")
@Getter
public class Btable extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "btable_id")
    private Long id;

    @Column(name = "btable_name", nullable = false)
    private String btableName;

    @Column(nullable = false)
    private Integer quantity;

    public void cancel() {
        this.quantity++;
        this.enabled=true;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brest_id")
    private Brest brest;

    @Type(type="yes_no")
    private boolean enabled = true;

    public void setEnabled(boolean flag) {
        this.enabled = flag;
    }

    @Builder(builderMethodName = "createBTable")
    public Btable(String btableName, Integer quantity) {
        this.btableName = btableName;
        this.quantity = quantity;

        this.createAt = LocalDateTime.now();
    }
    // 연관관계 편의 메서드
    public void addBrest(Brest brest) {
        this.brest = brest;
        brest.getBtables().add(this);
    }

    //==비즈니스 로직==//

    //예약 취소 시, 사용
    public void addStock(){
        this.quantity+=1;
    }

    //예약 시, 사용
    public void removeStock(){
        int restStock=this.quantity-1;
        if(restStock==0){
            setEnabled(false);
        }
        if(restStock<0){
            throw new NotEnoughStockException("예약 가능한 테이블 수가 부족합니다.");
        }

        this.quantity=restStock;
    }

    //레스토랑 수정 시, 사용
    public void updateBtableQty(Integer quantity){
        this.quantity=quantity;
    }
}