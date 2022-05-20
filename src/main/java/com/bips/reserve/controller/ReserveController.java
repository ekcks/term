package com.bips.reserve.controller;

import com.bips.reserve.dto.brest.BrestListDto;
import com.bips.reserve.dto.reserve.AvailableDateDto;
import com.bips.reserve.dto.reserve.AvailableTimeDto;
import com.bips.reserve.dto.reserve.ReserveItemRequestDto;
import com.bips.reserve.dto.reserve.ReserveItemSimpleDto;
import com.bips.reserve.dto.security.PrincipalDetails;
import com.bips.reserve.dto.btable.BtableReserveDto;
import com.bips.reserve.service.reserve.ReserveItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/reserve")
public class ReserveController {

    private final ReserveItemService reserveItemService;

    /**
     * 예약가능 레스토랑 조회
     */
    @GetMapping("/brests")
    public String hospitalList(
            @RequestParam(name = "offset", defaultValue = "0") int offset,
            @RequestParam(name = "limit", defaultValue = "10") int limit, Model model,
            @AuthenticationPrincipal PrincipalDetails user) {

        reserveItemService.validateDuplicateUser(user.getUsername());
        List<BrestListDto> brestListDtos = reserveItemService.getAllBrestInfo(offset, limit);
        model.addAttribute("brestList", brestListDtos);
        return "user/reserve/brestList";
    }

    /**
     * 예약가능 레스토랑 주소로 검색
     */
    @GetMapping("/search")
    public String searchByAddress(
            @RequestParam(name = "offset", defaultValue = "0") int offset,
            @RequestParam(name = "limit", defaultValue = "10") int limit, Model model,
            @RequestParam String addressSearch) {
        List<BrestListDto> brestListDtos = reserveItemService.getAllBrestInfoSearchByAddress(addressSearch, offset, limit);
        model.addAttribute("brestList", brestListDtos);
        return "user/reserve/brestList";
    }

    /**
     * 예약가능날짜 조회 및 선택
     */
    @GetMapping("/{brestId}/dates")
    public String getAvailableDates(@PathVariable Long brestId, Model model) {
        // 레스토랑이름으로 해당 레스토랑의 예약가능날짜 조회
        List<AvailableDateDto> availableDates = reserveItemService.getAvailableDates(brestId);
        model.addAttribute("brestId", brestId);
        model.addAttribute("dates", availableDates);

        return "user/reserve/reserveDateSelectForm";
    }

    /**
     * 예약가능시간 조회 및 선택
     */
    @GetMapping("/{brestId}/times")
    public String getAvailableTimes(
            @PathVariable Long brestId,
            @RequestParam(name="date") Long selectedDateId, Model model) {
        // 선택한 예약날짜의 pk로 예약가능시간 조회
        List<AvailableTimeDto> times = reserveItemService.getAvailableTimes(selectedDateId);
        model.addAttribute("date", selectedDateId);
        model.addAttribute("times", times);
        return "user/reserve/reserveTimeSelectForm";
    }

    /**
     * 예약가능테이블 조회 및 선택
     */
    @GetMapping("/{brestId}/btable")
    public String selectBtable(
            @PathVariable Long brestId,
            @RequestParam(name = "date") Long selectedDateId,
            @RequestParam(name = "time") Long selectedTimeId, Model model) {

        List<BtableReserveDto> btables = reserveItemService.getAvailableBtableNameList(brestId);

        model.addAttribute("btables", btables);
        model.addAttribute("date", selectedDateId);
        model.addAttribute("time", selectedTimeId);
        model.addAttribute("brestId", brestId);

        return "user/reserve/reserveBtableSelectForm";
    }

    /**
     * 예약처리
     */
    @PostMapping
    public String reserve(
            @AuthenticationPrincipal PrincipalDetails principal,
            @ModelAttribute ReserveItemRequestDto reserveItemRequestDto,
            RedirectAttributes redirectAttributes) {
        log.info("brestId = {}", reserveItemRequestDto.getBrestId());
        log.info("btableName = {}", reserveItemRequestDto.getBtableName());
        log.info("reserveDateId = {}", reserveItemRequestDto.getReserveDateId());
        log.info("reserveTimeId = {}", reserveItemRequestDto.getReserveTimeId());

        String username = principal.getUsername();
        log.info("username = {}", username);

        Long savedUserId = reserveItemService.reserve(
                username,
                reserveItemRequestDto.getBrestId(),
                reserveItemRequestDto.getBtableName(),
                reserveItemRequestDto.getReserveDateId(),
                reserveItemRequestDto.getReserveTimeId()
        );

        return "redirect:/reserve";
    }

    /**
     * 예약조회
     */
    @GetMapping
    public String reserveResult(@AuthenticationPrincipal PrincipalDetails principal, Model model) {
        String username = principal.getUsername();
        log.info("username = {}", username);

        ReserveItemSimpleDto reserveResult = reserveItemService.getReserveResult(username);
        if (reserveResult.getBrestName() == null) {
            return "redirect:/";
        }
        model.addAttribute("reserveResult", reserveResult);
        return "user/reserve/ReserveResult";
    }

    /**
     * 예약취소
     */
    @GetMapping("/{reserveItemId}/cancel")
    public String cancel(@PathVariable Long reserveItemId) {
        reserveItemService.cancelReserveItem(reserveItemId);

        return "redirect:/";
    }
}