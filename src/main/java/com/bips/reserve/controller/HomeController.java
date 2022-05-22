package com.bips.reserve.controller;

import com.bips.reserve.dto.security.PrincipalDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
public class HomeController {

    @GetMapping("/")
    public String home(@AuthenticationPrincipal PrincipalDetails details) {

        return "MainPage";
    }

    @GetMapping("/index")
    public String index(){
        return "index";
    }
    @GetMapping("/adminLogin")
    public String loginForm() {
        return "user/login/loginForm";
    }

    @PostMapping("/loginFail")
    public String loginFail(@RequestParam String errorMsg, Model model){
        model.addAttribute("errorMsg",errorMsg);

        return "user/login/loginForm";
    }

    @RequestMapping("/bipsLocation")
    public String location(){
        return "fragment/bipsLocation";
    }

    @RequestMapping("/bipsMenu")
    public String bipsMenu(){
        return "fragment/bipsMenu";
    }

    @RequestMapping("/bipsReserveNote")
    public String reserveNote(){
        return "fragment/bipsReserveNote";
    }
    @GetMapping("/test/asd")
    public String asdqwd() {
        return "user/signup/signupForm";
    }

    @GetMapping("/test/ad")
    public String asdqd() {
        return "admin/brestRegister";
    }

}