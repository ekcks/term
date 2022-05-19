package com.bips.reserve.service.admin;

import com.bips.reserve.domain.entity.*;

import com.bips.reserve.dto.brest.*;
import com.bips.reserve.dto.reserve.ReserveItemWithUsernameDto;
import com.bips.reserve.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import com.bips.reserve.service.Holiday;

@RequiredArgsConstructor
@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    private final BrestRepository brestRepository;
    private final AdminRepository adminRepository;
    private final Holiday holiday;
    private final AvailableTimeRepository availableTimeRepository;
    private final AvailableDateRepository availableDateRepository;
    private final ReserveItemRepository reserveItemRepository;

    /**
     * 레스토랑 정보 등록
     */
    @Transactional
    @Override
    public Long addBrest(BrestRequestDto brestRequestDto,String adminName) throws Exception{

        // 병원 엔티티 생성
        Brest brest = brestRequestDto.toBRestEntity();
        /**
         * 현재 Authentication 객체로부터 받은 adminName을 등록하는 레스토랑의 admin으로 설정하는 방식
         */
        Admin admin = adminRepository.findByName(adminName).get();
        brest.setAdmin(admin);
        // 총 좌석 수량 (종류 상관X)
        Integer total = 0;
        // 테이블 엔티티 생성 및 레스토랑 엔티티에 add
        Map<String, Integer> btableInfoMap = brestRequestDto.getBtableInfoMap();
        for (String key : btableInfoMap.keySet()) {
            Btable btable = Btable.createBTable()
                    .btableName(key)
                    .quantity(btableInfoMap.get(key))
                    .build();
            btable.addBrest(brest);
            total += btableInfoMap.get(key);
        }
        brest.setTotalBtableQuantity(total);

        /**
         * 예약 가능 날짜를 생성 (휴일제외)
         */
        // 예약가능시간
        List<Integer> availableTimeList = getAvailableTimes(brestRequestDto.getStartTime(), brestRequestDto.getEndTime());

        // 예약가능날짜
        List<String> holidays = holiday.holidayList(brestRequestDto.getStartDate(),brestRequestDto.getEndDate());
        List<String> availableDateList = holiday.availableDateList(brestRequestDto.getStartDate(), brestRequestDto.getEndDate(), holidays);

        for (String date : availableDateList) {
            AvailableDate availableDate= AvailableDate.createAvailableDate()
                    .date(date)
                    .acceptCount(brestRequestDto.getDateAccept())
                    .build();
            for (Integer time : availableTimeList) {
                AvailableTime availableTime= AvailableTime.createAvailableTime()
                        .time(time)
                        .acceptCount(brestRequestDto.getTimeAccept())
                        .build();
                availableTime.addAvailableDate(availableDate);
            }
            availableDate.addBrest(brest);
        }


        Brest savedBrest = brestRepository.save(brest);

        return savedBrest.getId();
    }

    /**
     * 예약가능시간 처리 메서드
     */
    private List<Integer> getAvailableTimes(String startTime, String endTime) {
        int start = Integer.parseInt(startTime);
        int end = Integer.parseInt(endTime);
        List<Integer> availableTimes = new ArrayList<>();
        for (int i=start; i<=end;i++) {
            availableTimes.add(i);
        }
        return availableTimes;
    }

    /**
     * 레스토랑이름으로 레스토랑 정보 얻어오기
     */
    @Transactional(readOnly = true)
    @Override
    public BrestResponseDto getBrestInfo(String brestName) {
        Brest findBrest = brestRepository.findByBrestName(brestName)
                .orElseThrow(() -> {
                    throw new IllegalArgumentException("존재하지 않는  레스토랑입니다.");
                });
        List<Btable> btables = findBrest.getBtables();
        Map<String, Integer> map = new HashMap<>();
        for (Btable btable : btables) {
            map.put(btable.getBtableName(), btable.getQuantity());
        }

        // 리턴 고쳐야 함
        return null;
    }

    /**
     * 어드민이 관리하는 래스토랑 리스트를 보여주기 위한 메서드
     */
    @Override
    public List<BrestSimpleInfoDto> getAllSimpleBrestInfo(String name) {
        Admin admin = adminRepository.findByName(name).get();
        return brestRepository.findAllByAdmin(admin);
    }

    @Override
    public List<BrestListDto> getBrestList(String name,String address) {
        Admin admin = adminRepository.findByName(name).get();
        if(address.equals("noSearch"))
            return brestRepository.findAllBrestInfo(admin.getId());

        return brestRepository.findBrestListByAddressAndAdmin(address, admin.getId());
    }

    /**
     * 레스토랑 상세 정보 조회 후 dto로 변환
     */
    @Override
    public BrestUpdateDto getBrest(Long id) {
        Optional<Brest> brestDetail = brestRepository.findBrestDetail(id);
        Brest brest = brestDetail.stream().findFirst().orElse(null);

        List<AvailableDate> availableDates = brest.getAvailableDates();
        List<AvailableTime> availableTimes = availableDates.get(0).getAvailableTimes();
        List<Btable> btables = brest.getBtables();

        Map<String,Integer> btableMap=new HashMap<>();

        for (Btable btable : btables) {
            btableMap.put(btable.getBtableName(),btable.getQuantity());
        }

        return BrestUpdateDto.createBrestUpdateDto()
                .id(brest.getId())
                .brestName(brest.getBrestName())
                .address(brest.getAddress())
                .detailAddress(brest.getDetailAddress())
                .dateAccept(brest.getDateAccept())
                .timeAccept(brest.getTimeAccept())
                .startDate(availableDates.get(0).getDate())
                .endDate(availableDates.get(availableDates.size()-1).getDate())
                .startTime(String.valueOf(availableTimes.get(0).getTime()))
                .endTime(String.valueOf(availableTimes.get(availableTimes.size()-1).getTime()))
                .A(btableIsPresent(btableMap,"A"))
                .B(btableIsPresent(btableMap,"B"))
                .C(btableIsPresent(btableMap,"C"))
                .D(btableIsPresent(btableMap,"D"))
                .build();
    }

    @Override
    @Transactional
    public Long brestUpdate(BrestUpdateDto dto) throws ParseException {
        Optional<Brest> brestDetail = brestRepository.findBrestDetail(dto.getId());
        Brest brest = brestDetail.stream().findFirst().orElse(null);

        //수정 목록
        List<Btable> btables = brest.getBtables();

        //==테이블 수정==//
        Map<String, Integer> btableInfoMap = dto.getBtableInfoMap();

        Integer total = 0;

        //테이블 이름 리스트. 추가된 테이블, 잔여 좌석이 0이된 테이블 확인 위해
        List<String> btableNames=new ArrayList<>();
        for (Btable btable : btables) {
            btableNames.add(btable.getBtableName());
        }

        for(String key:btableInfoMap.keySet()){
            total+=btableInfoMap.get(key);
            //추가된 테이블이 있는 지 확인
            if(!btableNames.contains(key)){
                Btable aditionalBTable = Btable.createBTable()
                        .btableName(key)
                        .quantity(btableInfoMap.get(key))
                        .build();
                aditionalBTable.addBrest(brest);
            }
            // 기존의 테이블에서 좌석 수가 바뀌었는지 확인
            else {
                for (Btable btable : btables) {
                    if (btable.getBtableName().equals(key)) {
                        //수량 수정 시, 0을 입력하면 dto로 전달이 안되기 때문에 확인을 위한 과정
                        btableNames.remove(key);
                        //이미 있는 테이블이라면 수량이 같으면 update 필요 x 수량이 다르면 update
                        if (btable.getQuantity() != btableInfoMap.get(key)) {
                            btable.updateBtableQty(btableInfoMap.get(key));
                            btable.setEnabled(true);
                        }
                        break;
                    }
                }
            }
    }
    // 비어있지 않다면, 수정 폼에서 0으로 설정되었다는 뜻. 수량을 0으로 설정하자
        if(!btableNames.isEmpty()){
            for (String btableName : btableNames) {
                Btable btable = btables.stream().filter(v -> v.getBtableName().equals(btableName)).findFirst().orElse(null);
                if(btable!=null){
                    btable.updateBtableQty(0);
                    btable.setEnabled(false);
                }
            }
        }

        //총 수량의 합이 같다면 update x
        if(total!=brest.getTotalQuantity()) {
            //원래 0이었다면 false 였으니
            if(brest.getTotalQuantity()==0)
                brest.setEnabled(true);

            brest.setTotalBtableQuantity(total);

            if(brest.getTotalQuantity()==0)
                brest.setEnabled(false);
        }

        //레스토랑의 예약가능 날짜 리스트
        List<AvailableDate> availableDates = brest.getAvailableDates();

        //==dateAccept수정부분==//
        Integer dateAcceptCount = dto.getDateAccept();
        Integer originDateAccept = brest.getDateAccept();
        //dateAccept가 수정되었다면
        if(originDateAccept != dateAcceptCount){
            brest.updateDateAccept(dateAcceptCount);
            int updateDateAcceptCount = dateAcceptCount - originDateAccept;

            List<Long> availableDateIds=new ArrayList<>();

            //수정된 dateAccept 적용 시, 0보다 작거나 같아질 경우
            boolean flag=false;
            for (AvailableDate availableDate : availableDates) {
                if(availableDate.getAcceptCount()+updateDateAcceptCount<=0){
                    availableDateIds.add(availableDate.getId());
                    flag=true;
                }
            }
            availableDateRepository.updateAvailableDateAcceptCount(updateDateAcceptCount
                    ,brest.getId());
            if(flag)
            {
                availableDateRepository.updateAvailableDateAcceptCountToZero(availableDateIds);
            }
        }

        //==timeAccept수정부분==//
        Integer timeAcceptCount = dto.getTimeAccept();
        Integer originTimeAccept = brest.getTimeAccept();

        //timeAccept가 수정되었다면
        if(originTimeAccept !=timeAcceptCount){
            int updateAcceptCount = timeAcceptCount - originTimeAccept;

            brest.updateTimeAccept(timeAcceptCount);

            List<Long> availableDateIds=new ArrayList<>();
            List<Long> availableTimeIds=new ArrayList<>();

            boolean flag=false;
            for (AvailableDate availableDate : availableDates) {
                availableDateIds.add(availableDate.getId());
                List<AvailableTime> availableTimes = availableDate.getAvailableTimes();

                //수량이 0보다 작거나 같아지는 것이 있으면
                for (AvailableTime availableTime : availableTimes) {
                    if(availableTime.getAcceptCount()+updateAcceptCount<=0){
                        availableTimeIds.add(availableTime.getId());
                        flag=true;
                    }
                }
            }
            availableTimeRepository.updateAvailableTimeAcceptCount(updateAcceptCount
                    ,availableDateIds);
            if(flag){
                availableTimeRepository.updateAvailableTimeAcceptCountToZero(availableTimeIds);
            }
        }

        return brest.getId();
    }

    /**
     * 예약 현황 정보 얻어오기
     */
    @Transactional(readOnly = true)
    @Override
    public List<ReserveItemWithUsernameDto> getReserveItemCondition(Long brestId) {
        List<ReserveItem> reserveItems = reserveItemRepository.findAllReserveItem(brestId);
        if(reserveItems.isEmpty()) {
            return null;
        }

        return reserveItems.stream()
                .map(ri->new ReserveItemWithUsernameDto(ri))
                .collect(Collectors.toList());
    }

    /**
     * 레스토랑 정보 조회 시 , 해당 테이블이 존재하는 지에 대한 여부
     */
    private Integer btableIsPresent(Map<String, Integer> btableMap,String key){
        Integer btableQty = btableMap.get(key);

        if(btableQty ==null)
            return 0;
        return btableQty;
    }
}