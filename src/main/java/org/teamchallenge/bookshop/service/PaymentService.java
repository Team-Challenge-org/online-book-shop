package org.teamchallenge.bookshop.service;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.teamchallenge.bookshop.dto.StripeChargeDto;
import org.teamchallenge.bookshop.model.PaymentRequest;

public interface PaymentService {
    StripeChargeDto createCharge(StripeChargeDto chargeDto);
}
