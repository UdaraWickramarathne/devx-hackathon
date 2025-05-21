package com.zenvest.devx.controllers;

import com.zenvest.devx.constants.ApiEndpoint;
import com.zenvest.devx.dtos.inputs.TransactionRequest;
import com.zenvest.devx.dtos.outputs.TransactionResponse;
import com.zenvest.devx.responses.ZenvestResponse;
import com.zenvest.devx.services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * TransactionController handles all transaction-related operations.
 * It provides endpoints to fetch transactions, deposit, and withdraw funds.
 */
@RestController
@RequestMapping(ApiEndpoint.TRANSACTION)
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Fetches all transactions for a specific account.
     *
     * @param accountId the ID of the account
     * @return a ResponseEntity containing a ZenvestResponse with a list of TransactionResponse objects
     */
    @GetMapping
    public ResponseEntity<ZenvestResponse<TransactionResponse>> getTransactions(@PathVariable  Long accountId){
        List<TransactionResponse> transactions = transactionService.getTransactionsForAccount(accountId);
        ZenvestResponse<TransactionResponse> response = new ZenvestResponse<>();
        response.setResults(transactions);
        response.setMessage("Transactions fetched successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Deposits funds into a specific account.
     *
     * @param accountId the ID of the account
     * @param request   the request object containing deposit details
     * @return a ResponseEntity containing a ZenvestResponse with the created TransactionResponse object
     */
    @PostMapping(ApiEndpoint.TRANSACTION_DEPOSIT)
    public ResponseEntity<ZenvestResponse<TransactionResponse>> deposit(
            @PathVariable Long accountId,
            @Valid @RequestBody TransactionRequest request) {

        TransactionResponse transactionResponse = transactionService.deposit(accountId, request);
        ZenvestResponse<TransactionResponse> response = new ZenvestResponse<>(transactionResponse);
        response.setMessage("Deposit successful");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Withdraws funds from a specific account.
     *
     * @param accountId the ID of the account
     * @param request   the request object containing withdrawal details
     * @return a ResponseEntity containing a ZenvestResponse with the created TransactionResponse object
     */
    @PostMapping(ApiEndpoint.TRANSACTION_WITHDRAW)
    public ResponseEntity<ZenvestResponse<TransactionResponse>> withdraw(
            @PathVariable Long accountId,
            @Valid @RequestBody TransactionRequest request) {
        TransactionResponse transactionResponse = transactionService.withdraw(accountId, request);
        ZenvestResponse<TransactionResponse> response = new ZenvestResponse<>(transactionResponse);
        response.setMessage("Withdraw successful");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
