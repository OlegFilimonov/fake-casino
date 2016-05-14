package com.olegfilimonov.fakecasino;

/**
 * @author Oleg Filimonov
 */
public class Player {
    private String username;
    private int balance;
    private int id;

    public Player(int id, int balance, String username) {
        this.id = id;
        this.username = username;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getBalance() {
        return balance;

    }

    public void setBalance(int balance) {
        this.balance = balance;

    }

    public void removeBalance(int amount) {
        this.balance -= amount;
        updateBalance();

    }

    public void addBalance(int amount) {
        this.balance += amount;
        updateBalance();
    }

    private boolean updateBalance(){
        return SQLDatabaseConnection.updateUserMoney(id,balance);
    }
}
