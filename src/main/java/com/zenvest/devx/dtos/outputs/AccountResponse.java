package com.zenvest.devx.dtos.outputs;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AccountResponse {
    private Long id;
    private String ownerName;
    private Double balance;
    private Boolean active;
}
