package com.zenvest.devx.dtos.outputs;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferResponse {
    private Long fromAccountId;
    private Long toAccountId;
    private Double amount;
    private Double accountBalance;
    private String description;
    private LocalDateTime timestamp;
}
