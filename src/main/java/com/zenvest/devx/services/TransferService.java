package com.zenvest.devx.services;

import com.zenvest.devx.dtos.inputs.TransferRequest;
import com.zenvest.devx.dtos.outputs.TransferHistoryResponse;
import com.zenvest.devx.dtos.outputs.TransferResponse;
import com.zenvest.devx.models.Account;
import com.zenvest.devx.models.Transfer;
import com.zenvest.devx.models.User;
import com.zenvest.devx.repositories.AccountRepository;
import com.zenvest.devx.repositories.TransferRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TransferService handles all fund transfer-related operations.
 * It provides methods to transfer funds and fetch transfer history for users.
 */
@Service
public class TransferService {

    private final AuthService authService;
    private final TransactionService transactionService;
    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;

    @Autowired
    public TransferService(AuthService authService,
                           AccountRepository accountRepository,
                           TransferRepository transferRepository,
                           TransactionService transactionService) {

        this.authService = authService;
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
        this.transactionService = transactionService;
    }

    /**
     * Transfers funds from one account to another.
     * This method updates the balances of both accounts and creates a new transfer record.
     *
     * @param request the request object containing transfer details
     * @return a TransferResponse object representing the transfer transaction
     */
    @Transactional
    public TransferResponse transferFunds(TransferRequest request) {
        Account from = transactionService.getUserOwnedAccount(request.getFromAccountId());
        Account to = accountRepository.findById(request.getToAccountId())
                .orElseThrow(() -> new RuntimeException("Destination account not found"));

        if(from.getId().equals(to.getId())) {
            throw new RuntimeException("Cannot transfer to the same account");
        }

        if(from.getBalance() < request.getAmount()){
            throw new IllegalArgumentException("Insufficient balance in source account");
        }

        from.setBalance(from.getBalance() - request.getAmount());
        to.setBalance(to.getBalance() + request.getAmount());

        from = accountRepository.save(from);
        accountRepository.save(to);

        Transfer transfer = Transfer.builder()
                .amount(request.getAmount())
                .description(request.getDescription())
                .timestamp(LocalDateTime.now())
                .fromAccount(from)
                .toAccount(to)
                .build();

        transferRepository.save(transfer);

        return toTransferResponse(transfer);

    }

    /**
     * Retrieves the transfer history for the current user.
     * This method fetches all transfers where the user is either the sender or receiver.
     *
     * @return a list of TransferHistoryResponse objects representing the user's transfer history
     */
    public List<TransferHistoryResponse> getTransferHistoryForCurrentUser() {
        User currentUser = authService.getCurrentUser();
        List<Transfer> transfers = transferRepository.findAllByFromAccountUserIdOrToAccountUserId(currentUser.getId(), currentUser.getId());

        return transfers.stream().map(transfer -> TransferHistoryResponse.builder()
                .id(transfer.getId())
                .amount(transfer.getAmount())
                .description(transfer.getDescription())
                .fromAccountOwnerName(transfer.getFromAccount().getOwnerName())
                .toAccountOwnerName(transfer.getToAccount().getOwnerName())
                .timestamp(transfer.getTimestamp())
                .build()
        ).collect(Collectors.toList());
    }

    /**
     * Converts a Transfer object to a TransferResponse object.
     * This method is used to create a response object for the transfer transaction.
     *
     * @param transfer the Transfer object to convert
     * @return a TransferResponse object representing the transfer transaction
     */
    private TransferResponse toTransferResponse(Transfer transfer) {
        return TransferResponse.builder()
                .fromAccountId(transfer.getFromAccount().getId())
                .toAccountId(transfer.getToAccount().getId())
                .amount(transfer.getAmount())
                .accountBalance(transfer.getFromAccount().getBalance())
                .description(transfer.getDescription())
                .timestamp(transfer.getTimestamp())
                .build();
    }

}
