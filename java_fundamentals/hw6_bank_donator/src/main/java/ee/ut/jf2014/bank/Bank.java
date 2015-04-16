package ee.ut.jf2014.bank;

import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class Bank {
  private final List<Account> accounts;
  private final ReadLock readLock;
  private final WriteLock writeLock;

  public Bank(List<Account> accounts) {
    this.accounts = accounts;
    ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    readLock = readWriteLock.readLock();
    writeLock = readWriteLock.writeLock();
  }

  public void transfer(Account source, Account target, int amount) {
    readLock.lock();

    Account first;
    Account second;
    if (source.hashCode() < target.hashCode()) {
      first = source;
      second = target;
    } else {
      first = target;
      second = source;
    }
    synchronized (first) {
      synchronized (second) {
        source.withdraw(amount);
        target.deposit(amount);
      }
    }
    readLock.unlock();
  }

  public String getAllBalances() {
    writeLock.lock();
    StringBuilder sb = new StringBuilder();
    Long sum = 0L;
    for (Account account : accounts) {
      int balance = account.getBalance();
      sb.append(balance + " ");
      sum += balance;
    }
    sb.append(sum.toString());
    writeLock.unlock();
    return sb.toString();
  }
}