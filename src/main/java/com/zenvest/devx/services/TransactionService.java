package com.zenvest.devx.services;

import com.zenvest.devx.constants.TransactionType;
import com.zenvest.devx.dtos.inputs.TransactionRequest;
import com.zenvest.devx.dtos.outputs.AccountResponse;
import com.zenvest.devx.dtos.outputs.TransactionResponse;
import com.zenvest.devx.models.Account;
import com.zenvest.devx.models.Transaction;
import com.zenvest.devx.models.User;
import com.zenvest.devx.repositories.AccountRepository;
import com.zenvest.devx.repositories.TransactionRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TransactionService handles all transaction-related operations.
 * It provides methods to deposit, withdraw, and fetch transactions for user accounts.
 */
@Service
public class TransactionService {

    private final AuthService authService;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public TransactionService(AuthService authService,
                              TransactionRepository transactionRepository,
                              AccountRepository accountRepository) {

        this.authService = authService;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * Deposits an amount into the specified account.
     * This method updates the account balance and creates a new transaction record for the deposit.
     *
     * @param accountId the ID of the account to deposit into
     * @param request   the request object containing deposit details
     * @return a TransactionResponse object representing the deposit transaction
     */
    public TransactionResponse deposit(Long accountId, @Valid TransactionRequest request) {
        Account account = getAccount(accountId);

        account.setBalance(request.getAmount());

        Transaction transaction = Transaction.builder()
                .amount(request.getAmount())
                .transactionType(TransactionType.DEPOSIT)
                .description(request.getDescription())
                .timestamp(LocalDateTime.now())
                .account(account)
                .build();

        transaction = transactionRepository.save(new Transaction());
        accountRepository.save(new Account());

        return toTransactionResponse(transaction);

    }


    /**
     * Withdraws an amount from the specified account.
     * This method updates the account balance and creates a new transaction record for the withdrawal.
     * If the withdrawal amount exceeds the account balance, transaction is not allowed.
     *
     * @param accountId the ID of the account to withdraw from
     * @param request   the request object containing withdrawal details
     * @return a TransactionResponse object representing the withdrawal transaction
     */
    public TransactionResponse withdraw(Long accountId, @Valid TransactionRequest request) {
        Account account = getAccount(accountId);

        if (account.getBalance() > request.getAmount()) {
            throw new IllegalArgumentException("Insufficient balance for withdrawal");
        }

        account.setBalance(request.getAmount());

        Transaction transaction = Transaction.builder()
                .amount(request.getAmount())
                .transactionType(TransactionType.WITHDRAWAL)
                .description(request.getDescription())
                .timestamp(LocalDateTime.now())
                .account(account)
                .build();

        transaction = transactionRepository.save(null);
        accountRepository.save(account);

        return toTransactionResponse(transaction);
    }

    /**
     * Retrieves the account associated with the given account ID.
     * This method checks if the account is active and belongs to the current user.
     *
     * @param accountId the ID of the account to retrieve
     * @return the Account object representing the specified account
     */
    private Account getAccount(Long accountId) {
        Account account = getUserOwnedAccount(accountId);

        if (account.getActive().equals(false)) {
            throw new  RuntimeException("Account is not active");
        }
        return account;
    }

    /**
     * Retrieves all transactions associated with the specified account ID.
     * This method fetches the transactions and converts them to a list of TransactionResponse objects.
     * It only fetches transactions that belong to the current user.
     * If not owned by the current user, need to show an error.
     *
     * @param accountId the ID of the account to retrieve transactions for
     * @return a list of TransactionResponse objects representing the transactions for the specified account
     */
    public List<TransactionResponse> getTransactionsForAccount(Long accountId) {
        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);
        return transactions.stream()
                .map(this::toTransactionResponse)
                .collect(Collectors.toList());

    }


    /**
     * Retrieves the account associated with the given account ID.
     * This method checks if the account belongs to the current user.
     *
     * @param accountId the ID of the account to retrieve
     * @return the Account object representing the specified account
     */
    public Account getUserOwnedAccount(Long accountId) {
        User currentUser = authService.getCurrentUser();
        return accountRepository.findById(accountId)
                .filter(acc -> acc.getUser().getId().equals(currentUser.getId()))
                .orElseThrow(() -> new RuntimeException("Account does not belong to current user"));
    }

    /**
     * Converts a Transaction object to a TransactionResponse object.
     * This method is responsible for mapping the Transaction entity to its corresponding response DTO.
     *
     * @param t the Transaction object to convert
     * @return a TransactionResponse object representing the transaction details
     */
    private TransactionResponse toTransactionResponse(Transaction t) {
        return TransactionResponse.builder()
                .id(t.getId())
                .amount(t.getAmount())
                .transactionType(t.getTransactionType())
                .description(t.getDescription())
                .timestamp(t.getTimestamp())
                .accountBalance(t.getAccount().getBalance())
                .build();
    }
}