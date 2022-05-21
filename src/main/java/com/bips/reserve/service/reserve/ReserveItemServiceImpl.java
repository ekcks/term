package com.bips.reserve.service.reserve;

import com.bips.reserve.domain.entity.*;
import com.bips.reserve.domain.value.ReserveStatus;
import com.bips.reserve.dto.brest.BrestListDto;
import com.bips.reserve.dto.reserve.AvailableDateDto;
import com.bips.reserve.dto.reserve.AvailableTimeDto;
import com.bips.reserve.dto.reserve.ReserveItemSimpleDto;
import com.bips.reserve.dto.btable.BtableReserveDto;
import com.bips.reserve.repository.*;
import com.bips.reserve.repository.custom.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReserveItemServiceImpl implements ReserveItemService{

    private final BtableCustomRepositoryImpl btableCustomRepository;
    private final BrestRepository brestRepository;
    private final AvailableDateRepository availableDateRepository;
    private final AvailableTimeRepository availableTimeRepository;
    private final UserRepository userRepository;
    private final ReserveItemRepository reserveItemRepository;


    /**
     * 유저가 예약하기 버튼을 눌렀을 때 모든 레스토랑의 간단한 정보 (레스토랑 이름, 주소, 테이블 잔여 수) 보여주기
     */
    @Override
    public List<BrestListDto> getAllBrestInfo(int offset, int limit) {
        return brestRepository.findBrestListPaging(offset, limit);
    }

    @Override
    public List<BrestListDto> getAllBrestInfoSearchByAddress(String address, int offset, int limit) {

        return brestRepository.findBrestListByAddressPaging(offset, limit, address);
    }

    /**
     * 레스토랑 이름으로 예약가능날짜 조회
     */
    @Override
    public List<AvailableDateDto> getAvailableDates(Long BrestId) {

        return reserveItemRepository.findAvailableDatesByBrestId(BrestId)
                .stream().map( m -> new AvailableDateDto(m.getId(), m.getDate())).collect(Collectors.toList());
    }

    /**
     * 예약가능시간 조회
     */
    public List<AvailableTimeDto> getAvailableTimes(Long id) {

        return reserveItemRepository.findAvailableTimesByAvailableDateId(id)
                .stream().map(t -> new AvailableTimeDto(t.getId(), t.getTime())).collect(Collectors.toList());
    }

    /**
     * 예약가능 테이블 조회
     */
    @Override
    public List<BtableReserveDto> getAvailableBtableNameList(Long brestId) {
        return reserveItemRepository.findAvailableBtables(brestId)
                .stream().map(v -> new BtableReserveDto(v.getId(), v.getBtableName())).collect(Collectors.toList());
    }

    /**
     * 예약처리
     */
    @Override
    public Long reserve(String username, Long brestId, String btableName, Long dateId, Long timeId) {
        Brest brest = brestRepository.findById(brestId).orElseThrow(
                () -> {
                    throw new IllegalArgumentException("존재하지 않는 레스토랑입니다.");
                }
        );
        Btable btable = btableCustomRepository.findBtable(brestId, btableName);
        AvailableTime time = availableTimeRepository.findAvailableTimeById(timeId);

        time.decreaseCount();
        if (time.getAcceptCount() <= 0) time.setEnabled(false);

        brest.removeStock();

        btable.removeStock();

        AvailableDate availableDate = availableDateRepository.findById(dateId).get();

        User user = userRepository.findByEmail(username).get();

        ReserveItem reserveItem = ReserveItem.createReserveItem()
                .Brest(brest)
                .reserveDate(availableDate.getDate())
                .reserveTime(time.getTime())
                .status(ReserveStatus.예약완료)
                .user(user)
                .btableName(btableName)
                .build();
        ReserveItem savedReserveItem = reserveItemRepository.save(reserveItem);

        return user.getId();
    }

    /**
     * 예약서 조회
     */
    @Override
    public ReserveItemSimpleDto getReserveResult(String username) {
        log.info("getReserveResult username = {}", username);
        User user = userRepository.findByEmail(username).orElseThrow(
                () -> {
                    throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
                }
        );
        return reserveItemRepository.findByUserId(user.getId()).orElseGet(
                () -> { return new ReserveItemSimpleDto(); });
    }

    /**
     * 이미 예약한 회원인지 확인.
     */
    @Override
    public void validateDuplicateUser(String username){
        User user = userRepository.findByEmail(username).orElseThrow(
                () -> {
                    throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
                }
        );
        Optional<ReserveItemSimpleDto> reserveItemByUserId = reserveItemRepository.findByUserId(user.getId());
        if(!reserveItemByUserId.isEmpty()){
            throw new IllegalStateException("이미 예약한 회원 입니다.");
        }

    }

    /**
     * 예약취소
     */
    @Override
    public void cancelReserveItem(Long reserveItemId) {
        ReserveItem reserveItem = reserveItemRepository.findById(reserveItemId).get();

        Brest brest = reserveItem.getBrest();
        brest.cancel();

        Btable btable = btableCustomRepository.findBtableDisabled(brest.getId(), reserveItem.getBtableName());
        btable.cancel();

        AvailableDate date = availableDateRepository.findAvailableDateByBrestIdAndDate(brest.getId(), reserveItem.getReserveDate());
        AvailableTime time = availableTimeRepository.findAvailableTimeByTimeAndDateId(reserveItem.getReserveTime(), date.getId());
        time.cancel();

        reserveItemRepository.deleteById(reserveItemId);
    }
}