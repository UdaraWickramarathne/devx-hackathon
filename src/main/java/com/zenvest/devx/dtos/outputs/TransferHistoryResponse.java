package com.zenvest.devx.dtos.outputs;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferHistoryResponse {
    private Long id;
    private Double amount;
    private String description;
    private String fromAccountOwnerName;
    private String toAccountOwnerName;
    private LocalDateTime timestamp;
}
