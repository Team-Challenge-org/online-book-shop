package org.teamchallenge.bookshop.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/payment")
@AllArgsConstructor
public class PaymentTestController {

    @GetMapping("/test")
    public String test() {
        return "payment";
    }
}
