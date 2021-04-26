package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.repository.AccountsRepository;
import lombok.Getter;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountsService {

	@Getter
	private final AccountsRepository accountsRepository;

	@Autowired
	public AccountsService(AccountsRepository accountsRepository) {
		this.accountsRepository = accountsRepository;
	}

	public void createAccount(Account account) {
		this.accountsRepository.createAccount(account);
	}

	public Account getAccount(String accountId) {
		return this.accountsRepository.getAccount(accountId);
	}

	public Account moneyTransfer(String accountId, String accountToId, BigDecimal amount) {
		Account acc = this.accountsRepository.moneyTransfer(accountId, accountToId, amount);
		if(acc == null) {
			return null;
		}else {
			EmailNotificationService email = new EmailNotificationService();
			String notificationDescription = "from your account amount : " + amount + " transfer to this account : " + accountToId; 
			email.notifyAboutTransfer(acc, notificationDescription);
			return acc;  
		}
	}
}