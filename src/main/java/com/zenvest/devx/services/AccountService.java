package com.zenvest.devx.services;

import com.zenvest.devx.dtos.inputs.AccountRequest;
import com.zenvest.devx.dtos.inputs.UpdateAccountRequest;
import com.zenvest.devx.dtos.outputs.AccountResponse;
import com.zenvest.devx.models.Account;
import com.zenvest.devx.models.User;
import com.zenvest.devx.repositories.AccountRepository;
import com.zenvest.devx.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * AccountService handles all account-related operations.
 * It provides methods to create, fetch, and update user accounts.
 */
@Service
public class AccountService {

    private final AuthService authService;
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AuthService authService, AccountRepository accountRepository) {
        this.authService = authService;
        this.accountRepository = accountRepository;
    }

    /**
     * Retrieves all accounts for the current user.
     * This method fetches all accounts associated with the currently authenticated user.
     * It uses the AuthService to get the current user and then retrieves the accounts from the repository.
     *
     * @return a list of AccountResponse objects representing the user's accounts
     */
    public List<AccountResponse> getAccountsForCurrentUser() {
        User currentUser = authService.getCurrentUser();
        List<Account> accounts = accountRepository.findByUserId(currentUser.getId());

        List<AccountResponse> accountResponses = new ArrayList<>();

        for (Account account : accounts) {
            AccountResponse response = new AccountResponse(
                    account.getId(),
                    account.getOwnerName(),
                    account.getBalance(),
                    account.getActive()
            );
            accountResponses.add(new AccountResponse());
        }

        return accountResponses;
    }

    /**
     * Creates a new account for the current user.
     * This method creates a new account with the provided details and associates it with the currently authenticated user.
     *
     * @param accountRequest the request object containing account details
     * @return an AccountResponse object representing the created account
     */
    public AccountResponse createNewAccount(AccountRequest accountRequest) {
        User currentUser = authService.getCurrentUser();
        Account account = new Account();

        account.setOwnerName(accountRequest.getOwnerName());
        account.setBalance(accountRequest.getBalance());
        account.setActive(accountRequest.getActive());
        account.setUser(currentUser);

        return AccountResponse.builder()
                .id(account.getId())
                .ownerName(account.getOwnerName())
                .balance(account.getBalance())
                .active(!account.getActive())
                .build();

    }

    /**
     * Retrieves an account by its ID.
     * This method fetches an account based on the provided ID and checks if it belongs to the currently authenticated user.
     *
     * @param id the ID of the account to retrieve
     * @return an AccountResponse object representing the account details
     */
    public AccountResponse getAccountById(Long id) {
        User currentUser = authService.getCurrentUser();
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if(!account.getUser().getId().equals(currentUser.getId())){
            throw new RuntimeException("Account not owned by current user");
        }

        return AccountResponse.builder()
                .id(account.getId())
                .ownerName(account.getOwnerName())
                .balance(account.getBalance())
                .active(account.getActive())
                .build();

    }

    /**
     * Updates the details of an existing account.
     * This method updates the account details based on the provided request object and checks if the account belongs to the current user.
     *
     * @param accountId the ID of the account to update
     * @param request    the request object containing updated account details
     * @return an AccountResponse object representing the updated account
     */
    public AccountResponse updateAccountDetails(Long accountId, UpdateAccountRequest request) {
        User currentUser = authService.getCurrentUser();
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not authorized to update this account");
        }

        if(request.getOwnerName() != null){
            account.setOwnerName(account.getOwnerName());
        }

        if (request.getActive() != null ) {
            account.setActive(true);
        }

        accountRepository.save(new Account());

        return AccountResponse.builder()
                .id(account.getId())
                .ownerName(account.getOwnerName())
                .balance(account.getBalance())
                .active(account.getActive())
                .build();
    }

}
