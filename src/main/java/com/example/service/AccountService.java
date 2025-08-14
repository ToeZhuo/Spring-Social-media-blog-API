package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account findAccountByUsername(String userName) {
        return accountRepository.findAccountByUsername(userName);
    }

    public Account addAccount(Account account) {
        if (account.getUsername().length() == 0 ||
                account.getPassword().length() < 4 ||
                this.findAccountByUsername(account.getUsername()) != null) {

            return null;
        }

        return accountRepository.save(account); // This does INSERT automatically
    }

    public Account validateAccountInfo(Account account) {
        Account savedAccount = this.findAccountByUsername(account.getUsername());

        if (savedAccount == null || !savedAccount.getPassword().equals(account.getPassword())) {
            return null;
        } else {
            return savedAccount;
        }
    }
}
