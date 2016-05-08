package com.olegfilimonov.fakecasino;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Main Page of the casino
 *
 * @author Oleg Filimonov
 */

@SessionScoped
@Named
public class CasinoPage implements Serializable {
    @Inject
    private PlayersManager playersManager;
    @Inject
    private MessageHelper messageHelper;
    @Inject
    private ChatManager chatManager;

    private String userName;
    private String passWord;
    private boolean loggedIn;
    private String message;

    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void login() {
        loggedIn = playersManager.login(userName, passWord);
        if (!loggedIn) messageHelper.addErrorMessage("Wrong username/password");
        else {
            chatManager.sendMessage(userName, "joined the chat");
        }
    }

    public void sendMessage() {
        chatManager.sendMessage(userName, message);
        this.message = "";
    }

    public void logout() {
        playersManager.logout(userName);
        loggedIn = false;
        chatManager.sendMessage(userName, "left the chat");
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
