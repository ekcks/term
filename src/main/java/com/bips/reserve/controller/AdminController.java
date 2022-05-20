package com.bips.reserve.controller;

import com.bips.reserve.dto.brest.*;
import com.bips.reserve.dto.reserve.ReserveItemWithUsernameDto;
import com.bips.reserve.dto.security.PrincipalDetails;
import com.bips.reserve.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    /**
     * 레스토랑 이름으로 레스토랑 단건 조회
     */
    @GetMapping("/brest")
    @ResponseBody
    public ResponseEntity<BrestResponseDto> getBRest(@RequestParam("name") String brestName) {
        BrestResponseDto brestResponseDto = adminService.getBrestInfo(brestName);

        return ResponseEntity.ok(brestResponseDto);
    }

    /**
     * 레스토랑 등록 폼 랜더링
     */
    @GetMapping("/brest/add")
    public String brestForm(Model model){
        model.addAttribute("brestRequestDto",new BrestRequestDto());
        return "admin/brestRegister";
    }

    /**
     * 현재 어드민이 관리하는 병원 목록 조회 (병원이름, 주소만 조회)
     */
    @ResponseBody
    @GetMapping("/brests")
    public List<BrestSimpleInfoDto> asd(Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        List<BrestSimpleInfoDto> brests = adminService.getAllSimpleBrestInfo(principal.getName());
        return brests;
    }

    /**
     * 레스토랑 등록
     * @param authentication 등록되는 레스토랑에 admin을 추가해주기 위해 현재 인증 객체를 사용
     */
    @PostMapping("/brest/add")
    public String addBRest(
            Authentication authentication,
            @Validated @ModelAttribute BrestRequestDto form, BindingResult result, HttpServletRequest request) throws Exception{

        if(result.hasErrors()){
            return "admin/brestRegister";
        }

        makeBTableInfoMap(form.getA(), form.getB(), form.getC(), form.getD(), form.getBtableInfoMap());

        timeParse(form);
        /**
         * /admin/** 으로 접근되었다는 것은 security filter를 지나 인가된 사용자라는 것. (Role = ADMIN)
         * 따라서 병원 등록시 Authentication에서 얻어온 유저 정보를 그대로 사용 (병원에 Admin을 넣어주기 위함)
         */
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        log.info("principal.name = {}", principal.getName());

        adminService.addBrest(form, principal.getName());

        return "redirect:/admin/brest/list";
    }

    /**
     * 레스톨랑 목록
     */
    @GetMapping("/brest/list")
    public String brestList(@AuthenticationPrincipal PrincipalDetails principal, Model model,
                               @RequestParam(defaultValue = "noSearch")String addressSearch) {
        String adminName = principal.getName();
        List<BrestListDto> brestList = adminService.getBrestList(adminName,addressSearch);
        model.addAttribute("brestList", brestList);
        return "admin/brestList";
    }

    /**
     * 레스토랑 상세정보 조회
     */
    @GetMapping("/brest/{brestId}")
    public String brestInfo(Model model,@PathVariable("brestId")Long id){

        BrestUpdateDto brestUpdateDto = adminService.getBrest(id);
        model.addAttribute("brestUpdateDto",brestUpdateDto);

        return "admin/brestDetail";
    }

    /**
     * 레스토랑 수정
     */
    @PostMapping("/brest/" + "edit/{brestId}")
    public String brestEdit(@PathVariable Long brestId,
                               @Validated @ModelAttribute BrestUpdateDto brestUpdateDto,BindingResult result)
            throws ParseException {
        if(result.hasErrors()){
            return "admin/brestDetail";
        }
        brestUpdateDto.setId(brestId);
        makeBTableInfoMap(brestUpdateDto.getA(), brestUpdateDto.getB(),
                brestUpdateDto.getC(), brestUpdateDto.getD(), brestUpdateDto.getBtableInfoMap());
        adminService.brestUpdate(brestUpdateDto);

        return "redirect:/admin/brest/list";
    }

    /**
     * 예약 현황 조회
     */
    @GetMapping("/brest/reserves/{brestId}")
    public String reserveCondition(@PathVariable Long brestId, Model model){
        List<ReserveItemWithUsernameDto> reserveItemConditions = adminService.getReserveItemCondition(brestId);

        model.addAttribute("reserveItemConditions",reserveItemConditions);
        return "admin/reserveCondition";
    }

    // 시간을 parseInt 되도록 만드는 메서드
    private void timeParse(BrestRequestDto form) {
        form.setStartTime(form.getStartTime().split(":")[0]);
        form.setEndTime(form.getEndTime().split(":")[0]);
    }

    // btableInfoMap만드는 메서드
    private void makeBTableInfoMap(Integer A, Integer B, Integer C, Integer D, Map<String,Integer> btableInfoMap) {

        if(A !=null && A !=0){
            btableInfoMap.put("A", A);
        }
        if(B !=null && B !=0){
            btableInfoMap.put("B", B);
        }
        if(C !=null && C !=0){
            btableInfoMap.put("C", C);
        }
        if(D !=null && D !=0){
            btableInfoMap.put("D", D);
        }
    }

}