package com.library.fines.datalayer;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "fines")
@Data
@NoArgsConstructor
public class Fine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Embedded
    private FineIdentifier fineIdentifier;
    private BigDecimal amount;
    private String reason;
    private Boolean isPaid;

    public Fine(@NotNull BigDecimal amount, @NotNull String reason, @NotNull Boolean isPaid) {
        this.fineIdentifier = new FineIdentifier();
        this.amount = amount;
        this.reason = reason;
        this.isPaid = isPaid;;
    }
}
