package ee.ut.jf2014;

import ee.ut.jf2014.bank.Account;
import ee.ut.jf2014.bank.Bank;
import ee.ut.jf2014.bank.Donator;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Homework {
  private final List<Account> accounts;
  private final Bank bank;
  private final List<Thread> donatorThreads;
  private final AtomicInteger numOfDonatorsRunning;
  private PrintStream out;

  public Homework(int n, PrintStream out) {
    this.out = out;
    accounts = initAccounts(n);
    bank = new Bank(accounts);
    donatorThreads = initDonators();
    numOfDonatorsRunning = new AtomicInteger();
  }

  private List<Account> initAccounts(int n) {
    List<Account> accounts = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      accounts.add(new Account(n));
    }
    return accounts;
  }

  private List<Thread> initDonators() {
    List<Thread> donatorThreads = new ArrayList<>();
    for (Account account : accounts) {
      List<Account> targets = new ArrayList<>(accounts);
      targets.remove(account);
      Collections.shuffle(targets);
      Donator donator = new Donator(account, targets, bank, new Cb() {
        @Override
        public void call() {
          numOfDonatorsRunning.decrementAndGet();
        }
      });
      donatorThreads.add(new Thread(donator));
    }
    return donatorThreads;
  }

  public void run() {
    out.println(bank.getAllBalances());
    out.println("Starting donators");

    for (Thread donator : donatorThreads) {
      donator.start();
      numOfDonatorsRunning.incrementAndGet();
    }

    while (numOfDonatorsRunning.get() != 0) {
      out.println(bank.getAllBalances());
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        throw new RuntimeException("Something went wrong", e);
      }
    }
    out.println("All donators have finished");
    out.println(bank.getAllBalances());
  }
}