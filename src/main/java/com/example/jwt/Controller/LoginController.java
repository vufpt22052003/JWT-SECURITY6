package com.example.jwt.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
    @RequestMapping("login")
    public String login() {
        return "login"; // Trả về tên của view template
    }
    @GetMapping("demo")
    public String demo(){
        return "demo";
    }
}
