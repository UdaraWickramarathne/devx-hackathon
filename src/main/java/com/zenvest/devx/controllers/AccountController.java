package com.zenvest.devx.controllers;

import com.zenvest.devx.constants.ApiEndpoint;
import com.zenvest.devx.dtos.inputs.AccountRequest;
import com.zenvest.devx.dtos.inputs.UpdateAccountRequest;
import com.zenvest.devx.dtos.outputs.AccountResponse;
import com.zenvest.devx.responses.ZenvestResponse;
import com.zenvest.devx.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * AccountController handles all account-related operations.
 * It provides endpoints to create, fetch, and update user accounts.
 */
@RestController
@RequestMapping(ApiEndpoint.ACCOUNT)
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Fetches all accounts for the current user.
     *
     * @return a ResponseEntity containing a ZenvestResponse with a list of AccountResponse objects
     */
    @GetMapping()
    public ResponseEntity<ZenvestResponse<AccountResponse>> getAccountsForCurrentUser() {
        List<AccountResponse> userAccounts = accountService.getAccountsForCurrentUser();
        ZenvestResponse<AccountResponse> response = new ZenvestResponse<>();
        response.setResults(userAccounts);
        response.setMessage("User accounts fetch successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Creates a new account for the current user.
     *
     * @param accountRequest the request object containing account details
     * @return a ResponseEntity containing a ZenvestResponse with the created AccountResponse object
     */
    @PostMapping()
    public ResponseEntity<ZenvestResponse<AccountResponse>> createAccount (@Valid @RequestBody AccountRequest accountRequest){
        AccountResponse accountResponse = accountService.createNewAccount(accountRequest);
        ZenvestResponse<AccountResponse> response = new ZenvestResponse<>(accountResponse);
        response.setMessage("Account created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Fetches an account by its ID.
     *
     * @param id the ID of the account to fetch
     * @return a ResponseEntity containing a ZenvestResponse with the AccountResponse object
     */
    @GetMapping(ApiEndpoint.ACCOUNTS_RESOURCE_BY_ID)
    public ResponseEntity<ZenvestResponse<AccountResponse>> getAccountById(@PathVariable Long id){
        AccountResponse accountResponse = accountService.getAccountById(id);
        ZenvestResponse<AccountResponse> response = new ZenvestResponse<>(accountResponse);
        response.setMessage("Account fetch successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Updates an existing account by its ID.
     *
     * @param id      the ID of the account to update
     * @param request the request object containing updated account details
     * @return a ResponseEntity containing a ZenvestResponse with the updated AccountResponse object
     */
    @PutMapping(ApiEndpoint.ACCOUNTS_RESOURCE_BY_ID)
    public ResponseEntity<ZenvestResponse<AccountResponse>> updateAccount(@PathVariable Long id, @RequestBody UpdateAccountRequest request){
        AccountResponse accountResponse = accountService.updateAccountDetails(id, request);
        ZenvestResponse<AccountResponse> response = new ZenvestResponse<>(accountResponse);
        response.setMessage("Account updated successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
