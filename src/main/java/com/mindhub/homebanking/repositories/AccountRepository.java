package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.persistence.Id;
@RepositoryRestResource
public interface AccountRepository extends JpaRepository<Account, Long> {



    String findByNumber(String number);
}
