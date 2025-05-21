package com.zenvest.devx.dtos.inputs;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAccountRequest {
    private String ownerName;
    private Boolean active;
}
