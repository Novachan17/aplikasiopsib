package com.project.aplikasiopsib.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GuestController {

    @PostMapping("/guest/login")
    public String guestLogin(@RequestParam String nama, HttpSession session) {

        session.setAttribute("role", "guest");
        session.setAttribute("guestName", nama);

        return "redirect:/guest/dashboard";
    }
}
