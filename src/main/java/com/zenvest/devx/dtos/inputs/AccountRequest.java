package com.zenvest.devx.dtos.inputs;
import lombok.*;
import jakarta.validation.constraints.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequest {
    @NotBlank(message = "Owner name must not be blank.")
    private String ownerName;

    @NotNull(message = "Balance must not be null.")
    @Min(value = 1, message = "Balance must be greater than 0")
    private Double balance;

    @NotNull(message = "Active status must not be null.")
    private Boolean active;
}
