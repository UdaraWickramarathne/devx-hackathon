package com.zenvest.devx.controllers;

import com.zenvest.devx.constants.ApiEndpoint;
import com.zenvest.devx.dtos.inputs.TransferRequest;
import com.zenvest.devx.dtos.outputs.TransferHistoryResponse;
import com.zenvest.devx.dtos.outputs.TransferResponse;
import com.zenvest.devx.responses.ZenvestResponse;
import com.zenvest.devx.services.TransferService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TransferController handles all fund transfer-related operations.
 * It provides endpoints to transfer funds and fetch transfer history.
 */
@RestController
@RequestMapping(ApiEndpoint.TRANSFER)
public class TransferController {

    private TransferService transferService;

    @Autowired
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    /**
     * Transfers funds between accounts.
     *
     * @param request the request object containing transfer details
     * @return a ResponseEntity containing a ZenvestResponse with the created TransferResponse object
     */
    @PostMapping
    public ResponseEntity<ZenvestResponse<TransferResponse>> transfer(@Valid @RequestBody TransferRequest request) {
        TransferResponse transfer = transferService.transferFunds(request);
        ZenvestResponse<TransferResponse> response = new ZenvestResponse<>(transfer);
        response.setMessage("Transfer successful");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Fetches the transfer history for the current user.
     *
     * @return a ResponseEntity containing a ZenvestResponse with a list of TransferHistoryResponse objects
     */
    @GetMapping(ApiEndpoint.TRANSFER_HISTORY)
    public ResponseEntity<ZenvestResponse<TransferHistoryResponse>> getTransferHistory() {
        List<TransferHistoryResponse> transferHistoryResponses = transferService.getTransferHistoryForCurrentUser();
        ZenvestResponse<TransferHistoryResponse> response = new ZenvestResponse<>();
        response.setResults(transferHistoryResponses);
        response.setMessage("Transfers retrieved successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
