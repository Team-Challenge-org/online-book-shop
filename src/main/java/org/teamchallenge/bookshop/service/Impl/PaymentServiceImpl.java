package org.teamchallenge.bookshop.service.Impl;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import org.teamchallenge.bookshop.dto.StripeChargeDto;
import org.teamchallenge.bookshop.exception.StripeInputException;
import org.teamchallenge.bookshop.service.PaymentService;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${stripe.apiKey}")
    private String stripeApiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    @Override
    public StripeChargeDto createCharge(StripeChargeDto chargeRequest) {
        try {
            chargeRequest.setSuccess(false);
            Map<String, Object> chargeParams = new HashMap<>();
            chargeParams.put("amount", (int) (chargeRequest.getAmount() * 100));  // Перевірте правильність конвертації
            chargeParams.put("currency", "usd");
            chargeParams.put("description", "Payment for id " + chargeRequest.getAdditionalInfo().getOrDefault("ID_546", ""));
            chargeParams.put("source", chargeRequest.getStripeToken());
            Map<String, Object> metaData = new HashMap<>();
            metaData.put("id", chargeRequest.getChargeId());
            metaData.putAll(chargeRequest.getAdditionalInfo());
            chargeParams.put("metadata", metaData);

            System.out.println("Charge Parameters: " + chargeParams);

            Charge charge = Charge.create(chargeParams);

            System.out.println("Charge Response: " + charge);

            chargeRequest.setMessage(charge.getOutcome().getSellerMessage());

            if (charge.getPaid()) {
                chargeRequest.setChargeId(charge.getId());
                chargeRequest.setSuccess(true);
            }

            return chargeRequest;
        } catch (StripeException e) {
            e.printStackTrace();  // Логування помилки
            throw new StripeInputException("Wrong input!");
        }
    }
}

