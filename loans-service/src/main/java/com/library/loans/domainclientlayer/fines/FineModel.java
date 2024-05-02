package com.library.loans.domainclientlayer.fines;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
public class FineModel {

    String fineId;
    BigDecimal amount;
    String reason;
    Boolean isPaid;
}
