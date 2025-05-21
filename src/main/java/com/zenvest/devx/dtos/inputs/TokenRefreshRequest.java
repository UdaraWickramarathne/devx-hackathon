package com.zenvest.devx.dtos.inputs;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenRefreshRequest {
    @NotBlank(message = "Refresh token must not be blank")
    private String refreshToken;
}

