package com.olegfilimonov.fakecasino;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Main Page of the casino
 *
 * @author Oleg Filimonov
 */

@SessionScoped
@Named
public class CasinoView implements Serializable {

    @Inject
    private CasinoManager casinoManager;
    @Inject
    private MessageHelper messageHelper;
    @Inject
    private ChatManager chatManager;

    private boolean ready = false;
    private boolean betMade = false;

    private String username;
    private String password;
    private int earnings;
    private int bet;
    private boolean loggedIn;
    private String message;
    private String[] numbers;

    public void login() {
        loggedIn = casinoManager.login(username, password);
        if (!loggedIn) {
            messageHelper.addErrorMessage("Wrong username/password");
        } else {
            chatManager.sendMessage(username, "joined the game");
        }

    }

    public void sendMessage() {
        chatManager.sendMessage(username, message);
        message = "";
    }

    public void logout() {
        casinoManager.logout(username);
        loggedIn = false;
        chatManager.sendMessage(username, "left the game");
        casinoManager.removeReady(username);
    }

    public void makeBet() {
        setReady(false);
        List<Integer> nums = new ArrayList<Integer>();

        for (String numString : numbers) {
            nums.add(Integer.valueOf(numString));
        }

        Bet bet = new Bet(this.bet, nums);
        betMade = casinoManager.makeBet(username, bet);

        if(!betMade) messageHelper.addErrorMessage("You don't have enough to make a bet of " + bet.getAmount() + "$");
    }

    public void updateReady() {
        if (ready) {
            casinoManager.addReady(username);
        } else {
            casinoManager.removeReady(username);
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getMessage() {
        return message;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public int getBet() {
        return bet;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public String[] getNumbers() {
        return numbers;
    }

    public void setNumbers(String[] numbers) {
        this.numbers = numbers;
    }

    public boolean isBetMade() {
        return betMade;
    }

    public void setBetMade(boolean betMade) {
        this.betMade = betMade;
    }

    public void resetBets() {
        this.betMade = false;
    }
}
