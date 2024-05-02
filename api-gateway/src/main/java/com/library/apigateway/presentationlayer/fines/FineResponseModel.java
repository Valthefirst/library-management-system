package com.library.apigateway.presentationlayer.fines;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FineResponseModel extends RepresentationModel<FineResponseModel> {

    private String fineId;
    private BigDecimal amount;
    private String reason;
    private Boolean isPaid;
//    private String patronId;
}
