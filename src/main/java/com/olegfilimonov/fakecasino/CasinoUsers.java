package com.olegfilimonov.fakecasino;

import org.primefaces.push.PushContext;
import org.primefaces.push.PushContextFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

/**
 * @author Oleg Filimonov
 */

@Named
@ApplicationScoped
public class CasinoUsers implements Serializable {
    private List<Player> players;
    private Random random;
    private HashMap<Player, Bet> bets;
    private int amountOfReady = 0;
    private static final String STATE = "/state";
    private int state;
    private static final String CHAT = "/chat";
    private String message = "";


    public CasinoUsers() {
        this.players = new ArrayList<>();
        this.bets = new HashMap<>();
        this.random = new Random();
    }

    public boolean login(String username, String password) {
        synchronized (players) {

            if (this.contains(username)) return false;

            // boolean valid = SQLDatabaseConnection.checkUser(username, password);
            boolean valid = true;

            if (!valid) return false;

            //int balance = SQLDatabaseConnection.getUserMoney(username);

            int balance = 100;

            return players.add(new Player(username, balance));

        }
    }

    public void logout(String username) {
        synchronized (players) {

            Player loggedout = null;

            for (Player player : players) {
                if (player.username.equals(username)) loggedout = player;
            }

            if (loggedout != null)
                players.remove(loggedout);
        }

    }

    public boolean contains(final String username) {
        return getPlayerByName(username) != null;

    }

    public void addReady(String username) {
        amountOfReady++;
        if (amountOfReady >= players.size()) {
            if (state == 0) {
                setState(1);
            } else if (state == 1) {
                calculateResults();
                setState(0);
            }
        }
        sendMessage(username + " is ready");
        commit();
    }

    public void removeReady(String username) {
        amountOfReady--;
        if (amountOfReady < players.size()) {
            setState(0);
        }
        sendMessage(username + " is not ready");
        commit();
    }

    private void sendCommand(int state) {
        pushContext.push(STATE, state);
    }


    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        if (state == this.state) return;
        this.state = state;
        this.amountOfReady = 0;
        sendCommand(state);
    }

    private Player getPlayerByName(String username) {
        synchronized (players) {
            for (Player player : players) {
                if (player.username.equals(username)) return player;
            }
        }
        return null;
    }

    public void makeBet(String username, Bet bet) {
        Player player = getPlayerByName(username);
        if (player == null) return;
        //todo check if theres enough room
        player.removeBalance(bet.getAmount());
        synchronized (bets) {
            bets.put(player, bet);
        }
        sendMessage(username + " has made a bet of " + bet.getAmount() + "$");

        addReady(username);
        commit();
    }

    private void calculateResults() {


        int winningNum = random.nextInt(10 + 1);
        int winnings = 0;

        sendMessage("<b>Winning number is: + " + winningNum + "</b>");


        for (Map.Entry<Player, Bet> entry : bets.entrySet()) {
            Player player = entry.getKey();
            Bet bet = entry.getValue();
            List<Integer> nums = bet.getNumbers();

            if (nums.contains(winningNum)) {
                winnings = 100 / nums.size();
            }

            player.addBalance(winnings);
            sendMessage(player.getUsername() + " won " + winnings + "$");

        }
    }

    private final PushContext pushContext = PushContextFactory.getDefault().getPushContext();

    public void sendMessage(String userName, String message) {
        sendMessage(userName + ": " + message);
    }

    private void sendMessage(String message) {
        this.message += message + "<br>";

    }

    private void commit() {
        pushContext.push(CHAT, message);
        this.message = "";
    }
}