package com.library.fines.presentationlayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FineResponseModel {

    private String fineId;
    private BigDecimal amount;
    private String reason;
    private Boolean isPaid;
//    private String patronId;
}
