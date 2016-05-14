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
public class CasinoManager implements Serializable {
    private final List<Player> players;
    private Random random;
    private final HashMap<Player, Bet> playerToBet;
    private final HashMap<String, Boolean> playerToReady;
    private static final String STATE = "/state";
    private int state;
    private static final String CHAT = "/chat";
    private String message = "";


    public CasinoManager() {
        this.players = new ArrayList<Player>();
        this.playerToBet = new HashMap<Player, Bet>();
        this.random = new Random();
        this.playerToReady = new HashMap<String, Boolean>();
    }

    public boolean login(String username, String password) {
        synchronized (players) {

            if (this.contains(username)) return false;

            int id = SQLDatabaseConnection.checkUser(username, password);
//            boolean valid = true;

            if (id == -1) return false;

            int balance = SQLDatabaseConnection.getUserMoney(username);

//            int balance = 100;
            Player player = new Player(id, balance, username);
            playerToReady.put(username, false);
            return players.add(player);

        }
    }

    public void logout(String username) {
        synchronized (players) {

            Player loggedout = null;

            for (Player player : players) {
                if (player.getUsername().equals(username)) loggedout = player;
            }

            if (loggedout != null)
                players.remove(loggedout);
        }

    }

    private boolean allReady() {
        if (playerToReady.size() == 0) return false;

        boolean allReady = true;
        for (Map.Entry<String, Boolean> entry : playerToReady.entrySet()) {
            if (!entry.getValue()) allReady = false;
        }

        return allReady;
    }

    public boolean contains(final String username) {
        return getPlayerByName(username) != null;

    }

    public void addReady(String username) {
        playerToReady.put(username, true);

        sendMessage(username + " is ready");

        boolean changeState = false;

        if (allReady()) {
            changeState = true;

            if (state == 1) {
                calculateResults();
            }
        }
        commit();


        if (changeState) {
            if (state == 0) {
                setState(1);
            } else if (state == 1) {
                setState(0);
            }
        }
    }

    public void removeReady(String username) {
        playerToReady.put(username, false);

        sendMessage(username + " is not ready");
        commit();
    }

    private void sendCommand(int state) {
        pushContext.push(STATE, state);
    }


    public List<Player> getPlayers() {
        return players;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        if (state == this.state) return;
        this.state = state;
        resetReady();
        sendCommand(state);
    }

    private void resetReady() {
        for (Map.Entry<String, Boolean> entry : playerToReady.entrySet()) {
            entry.setValue(false);
        }
    }

    private Player getPlayerByName(String username) {
        synchronized (players) {
            for (Player player : players) {
                if (player.getUsername().equals(username)) return player;
            }
        }
        return null;
    }

    public boolean makeBet(String username, Bet bet) {
        Player player = getPlayerByName(username);
        if (player == null) return false;

        if (bet.getAmount() > player.getBalance()) return false;

        player.removeBalance(bet.getAmount());

        synchronized (playerToBet) {
            playerToBet.put(player, bet);
        }

        String bets = Arrays.toString(bet.getNumbers().toArray());

        sendMessage(username + " has made a bet of " + bet.getAmount() + "$ on " + bets);

        addReady(username);
        return true;
    }

    private void calculateResults() {


        int winningNum = random.nextInt(10) + 1;
        int winnings = 0;

        sendMessage("<b>Winning number is: " + winningNum + "</b>");


        for (Map.Entry<Player, Bet> entry : playerToBet.entrySet()) {
            Player player = entry.getKey();
            Bet bet = entry.getValue();
            List<Integer> nums = bet.getNumbers();

            if (nums.contains(winningNum)) {
                winnings = (int) (bet.amount * ((float)10 / (float)nums.size()));
            }

            player.addBalance(winnings);
            sendMessage(player.getUsername() + " has won " + winnings + "$");

        }

    }

    private final PushContext pushContext = PushContextFactory.getDefault().getPushContext();

    public void sendMessage(String userName, String message) {
        sendMessage(userName + ": " + message);
    }

    private void sendMessage(String message) {
        this.message += "<br>" + message;

    }

    private void commit() {
        if (message.equals("")) return;
        message = message.replaceFirst("<br>", "");
        pushContext.push(CHAT, message);
        this.message = "";
    }
}