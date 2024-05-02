package com.library.apigateway.presentationlayer.fines;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FineRequestModel {

    private BigDecimal amount;
    private String reason;
    private Boolean isPaid;
//    private String patronId;
}
