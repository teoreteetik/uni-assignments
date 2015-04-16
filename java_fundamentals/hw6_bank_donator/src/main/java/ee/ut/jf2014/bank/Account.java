package ee.ut.jf2014.bank;

public class Account {

  private int balance;

  public Account(int balance) {
    this.balance = balance;
  }

  public int getBalance() {
    return balance;
  }

  public synchronized void withdraw(int amount) {
    this.balance -= amount;
  }

  public synchronized void deposit(int amount) {
    this.balance += amount;
  }
}