package ee.ut.jf2014.bank;

import ee.ut.jf2014.Cb;

import java.util.List;

public class Donator implements Runnable {
  private final Account source;
  private final List<Account> targets;
  private final Bank bank;
  private final Cb afterDoneCb;

  public Donator(Account source, List<Account> targets, Bank bank,
      Cb afterDoneCb) {
    this.source = source;
    this.targets = targets;
    this.bank = bank;
    this.afterDoneCb = afterDoneCb;
  }

  @Override
  public void run() {
    for (Account targetAccount : targets) {
      bank.transfer(source, targetAccount, 1);
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        throw new RuntimeException("Something went wrong", e);
      }
    }
    afterDoneCb.call();
  }
}