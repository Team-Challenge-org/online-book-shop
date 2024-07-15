package org.teamchallenge.bookshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.teamchallenge.bookshop.service.SendMailService;

@RestController
@RequestMapping("api/v1/mail")
@Slf4j
@AllArgsConstructor
public class SendMailController {

    private final SendMailService sendMailService;

    @Operation(description = "send mail to user")
    @PostMapping("/send")
    public String sendMail(@RequestParam String mail) {
        return sendMailService.send(mail);
    }
}