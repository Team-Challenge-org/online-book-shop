package org.teamchallenge.bookshop.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teamchallenge.bookshop.dto.StripeChargeDto;
import org.teamchallenge.bookshop.service.PaymentService;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/charge")
    @ResponseBody
    public ResponseEntity<StripeChargeDto> charge(@RequestBody StripeChargeDto chargeDto) {
        StripeChargeDto response = paymentService.createCharge(chargeDto);
        return ResponseEntity.ok(response);
    }
}
