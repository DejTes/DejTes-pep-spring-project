package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    //Registers a new account after validating the username and password
    public Account registerAccount(Account account) {
        if (account.getUsername() == null || account.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be blank");
        }
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters");
        }
        if (accountRepository.findByUsername(account.getUsername()) != null) {
            throw new IllegalStateException("An account with this username already exists");
        }
        return accountRepository.save(account);
    }


    //Authenticates a user by verifying the username and password
    public Account login(String username, String password) {
        Account account = accountRepository.findByUsernameAndPassword(username, password);
        if (account == null) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        return account;
    }

    //Retrieves an account by its username
    public Account getAccountByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    //Retrieves an account by its ID
    public Account getAccountById(Integer accountId) {
        return accountRepository.findById(accountId).orElse(null);
    } 

    //Save an account to the repository
    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }
}
