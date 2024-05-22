package com.example.service;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository =  accountRepository;
    }

    public Account registerAccount(Account account) {
        if(account.getUsername() == null || account.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be blank");
        }
        if ( account.getPassword() == null || account.getPassword().length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters");
        }
        if (accountRepository.findByUsername(account.getUsername()) != null ) {
            throw new IllegalArgumentException("An account with this username already exists");
        }
        return accountRepository.save(account);
    }



    public Account login(String username, String password) {
        Account account = accountRepository.findByUsername(username);
        if(account == null || !account.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        return account;
    }


}
