package com.bips.reserve;

import com.bips.reserve.domain.entity.*;
import com.bips.reserve.domain.value.Gender;
import com.bips.reserve.domain.value.Role;
import com.bips.reserve.repository.AdminRepository;
import com.bips.reserve.repository.BrestRepository;
import com.bips.reserve.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 초기 admin 데이터 저장 클래스, DI(의존 관계) 주입 후 바로 실행 될 메서드 정의
 */
@Component
@RequiredArgsConstructor
public class AdminInit {
    private final UserService userService;
    private final AdminRepository adminRepository;
    private final BrestRepository brestRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


@PostConstruct
    public void init(){
        User user = User.createUser()
                .email("admin")
                .password(bCryptPasswordEncoder.encode("admin"))
                .name("admin")
                .age(0)
                .address("admin")
                .detailAddress("admin")
                .role(Role.ROLE_ADMIN)
                .gender(Gender.MALE)
                .build();

        User savedUser = userService.createUser(user);
        Admin admin = Admin.createAdmin()
                .name(savedUser.getName())
                .build();
        adminRepository.save(admin);

        Brest brest = Brest.createBRest()
                .brestName("BrestA")
                .address("서울특별시 강서구")
                .detailAddress("A빌딩")
                .dateAccept(100)
                .timeAccept(10)
                .build();
        brest.setAdmin(admin);

        Btable seat2 = Btable.createBTable()
                .btableName("seat2")
                .quantity(50)
                .build();
        Btable seat4 = Btable.createBTable()
                .btableName("seat4")
                .quantity(20)
                .build();
        Btable seat6 = Btable.createBTable()
                .btableName("seat6")
                .quantity(40)
                .build();
       Btable seat8 = Btable.createBTable()
                .btableName("seat8")
                .quantity(10)
                .build();

        seat2.addBrest(brest);
        seat4.addBrest(brest);
        seat6.addBrest(brest);
        seat8.addBrest(brest);

        List<String> dateList=new ArrayList<>();
        dateList.add("2021.1.1");
        dateList.add("2021.1.2");
        dateList.add("2021.1.3");
        dateList.add("2021.1.4");

        List<Integer> timeList=new ArrayList<>();
        timeList.add(9);
        timeList.add(10);
        timeList.add(11);
        timeList.add(13);

        for (String date : dateList) {
            AvailableDate availableDate= AvailableDate.createAvailableDate()
                    .date(date)
                    .acceptCount(100)
                    .build();
            for (Integer time : timeList) {
                AvailableTime availableTime = AvailableTime.createAvailableTime()
                        .time(time)
                        .acceptCount(10)
                        .build();
                availableTime.addAvailableDate(availableDate);
            }
            availableDate.addBrest(brest);
        }
        brest.setTotalBtableQuantity(120);

        brestRepository.save(brest);
    }
}