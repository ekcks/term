package com.bips.reserve.domain.entity;

        import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
        import com.bips.reserve.exception.vaccine.NotEnoughStockException;
        import lombok.AccessLevel;
        import lombok.Builder;
        import lombok.Getter;
        import lombok.NoArgsConstructor;
        import org.hibernate.annotations.BatchSize;
        import org.hibernate.annotations.ColumnDefault;
        import org.hibernate.annotations.Type;

        import javax.persistence.*;
        import java.time.LocalDateTime;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "brest")
@Getter
public class Brest extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "brest_id")
    private Long id;

    @Column(name = "brest_name")
    private String brestName;

    // 양방향
    @OneToMany(mappedBy = "brest",cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"brest"})
    private List<AvailableDate> availableDates = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    public void setAdmin(Admin admin) {
        this.admin = admin;
        admin.getBrests().add(this);
    }

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String detailAddress;

    @Column(name = "total_quantity")
    private Integer totalQuantity;

    public void cancel() {
        this.totalQuantity++;
    }

    @Column(name = "date_accept")
    private Integer dateAccept;
    @Column(name = "time_accept")
    private Integer timeAccept;

    public void setTotalBtableQuantity(Integer qty) {
        this.totalQuantity = qty;
    }

    public void removeStock() {
        int restStock=this.totalQuantity-1;
        if(restStock==0){
            setEnabled(false);
        }
        if(restStock<0){
            throw new NotEnoughStockException("예약 가능한 수량이 부족합니다.");
        }

        this.totalQuantity=restStock;
    }

    public void updateDateAccept(Integer dateAccept){this.dateAccept=dateAccept;}

    public void updateTimeAccept(Integer timeAccept){this.timeAccept=timeAccept;}

    // true: y, false: n
    @Type(type = "yes_no")
    private Boolean enabled = true; // 예약 가능 여부

    @OneToMany(mappedBy = "brest", cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JsonIgnoreProperties({"brest"})
    private List<Btable> Btables = new ArrayList<>();

    public void setEnabled(boolean flag) {
        this.enabled = flag;
    }

    // 연관관계 편의 메서드
    private void addAdmin(Admin admin) {
        this.admin = admin;
        admin.getBrests().add(this);
    }


    @Builder(builderMethodName = "createBRest")
    public Brest(String brestName, String address, String detailAddress,Integer dateAccept,Integer timeAccept) {
        this.brestName = brestName;
        this.address = address;
        this.detailAddress = detailAddress;
        this.createAt = LocalDateTime.now();
        this.dateAccept=dateAccept;
        this.timeAccept=timeAccept;
    }
}