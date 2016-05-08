package com.olegfilimonov.fakecasino;

import org.primefaces.context.RequestContext;

import javax.enterprise.context.ApplicationScoped;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Oleg Filimonov
 */
@ApplicationScoped
public class PlayersManager implements Serializable {
    private final Set<String> players;


    public PlayersManager() {
        this.players = new HashSet<String>();
    }

    public boolean login(String username,String password) {
        synchronized (players) {

            if(players.contains(username)) return false;

            boolean valid = SQLDatabaseConnection.checkUser(username,password);

            if(!valid) return false;

            RequestContext.getCurrentInstance().execute("subscriber.connect()");

            boolean b = players.add(username);
            return b;
        }
    }

    public void logout(String user) {
        synchronized (players) {

            players.remove(user);
        }
    }
}