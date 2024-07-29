package org.teamchallenge.bookshop.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StripeChargeDto {
    private String stripeToken;
    private String username;
    private Double amount;
    private Boolean success;
    private String message;
    private String chargeId;
    private Map<String, Object> additionalInfo = new HashMap<>();

    public StripeChargeDto(String stripeToken, String username, Double amount) {
        this.stripeToken = stripeToken;
        this.username = username;
        this.amount = amount;
        this.additionalInfo = new HashMap<>();
    }
}
