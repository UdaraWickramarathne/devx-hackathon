package com.zenvest.devx.dtos.outputs;

import com.zenvest.devx.constants.TransactionType;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {
    private Long id;
    private TransactionType transactionType;
    private Double amount;
    private String description;
    private LocalDateTime    timestamp;
    private Double accountBalance;
}
