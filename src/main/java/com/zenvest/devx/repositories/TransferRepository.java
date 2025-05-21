package com.zenvest.devx.repositories;

import com.zenvest.devx.models.Account;
import com.zenvest.devx.models.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransferRepository extends JpaRepository<Transfer,Long> {
    List<Transfer> findByFromAccountIn(List<Account> fromAccounts);

    List<Transfer> findAllByFromAccountUserIdOrToAccountUserId(Long fromUserId, Long toUserId);
}
