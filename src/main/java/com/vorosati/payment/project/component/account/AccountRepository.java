package com.vorosati.payment.project.component.account;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Account> findByIdForUpdate(@Param("id") Long id);

}