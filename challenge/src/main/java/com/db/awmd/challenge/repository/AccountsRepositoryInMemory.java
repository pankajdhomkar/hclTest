package com.db.awmd.challenge.repository;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;

import lombok.Synchronized;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

@Repository
public class AccountsRepositoryInMemory implements AccountsRepository {

  private final Map<String, Account> accounts = new ConcurrentHashMap<>();

  @Override
  public void createAccount(Account account) throws DuplicateAccountIdException {
    Account previousAccount = accounts.putIfAbsent(account.getAccountId(), account);
    if (previousAccount != null) {
      throw new DuplicateAccountIdException(
        "Account id " + account.getAccountId() + " already exists!");
    }
  }

  @Override
  public Account getAccount(String accountId) {
    return accounts.get(accountId);
  }

  @Override
  public void clearAccounts() {
    accounts.clear();
  }

@Override
@Synchronized
public Account moneyTransfer(String accountId, String accountToId, BigDecimal amount) {
	Account accountFrom= accounts.get(accountId);
	Account accountTo =  accounts.get(accountToId);
	
	BigDecimal balance = accountFrom.getBalance();
	if(balance.equals( BigDecimal.ZERO)) {
		return null;
	}else {
		BigDecimal totalAmountTo = accountTo.getBalance().add(amount);
		BigDecimal totalAmountFrom = accountFrom.getBalance().subtract(amount);
		accountTo.setBalance(totalAmountTo);
		accountFrom.setBalance(totalAmountFrom);
	}
	return accounts.get(accountId);
}

}
