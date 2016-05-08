package com.olegfilimonov.fakecasino;

import org.primefaces.push.PushContext;
import org.primefaces.push.PushContextFactory;

import javax.enterprise.context.ApplicationScoped;


/**
 * @author Oleg Filimonov
 */
@ApplicationScoped
public class ChatManager {

    private static final String CHANNEL = "/chat";

    private final PushContext pushContext = PushContextFactory.getDefault().getPushContext();

    public void sendMessage(String userName, String message) {
        sendMessage(userName + ": " + message);
    }

    private void sendMessage(String message) {
        pushContext.push(CHANNEL, message);
    }


}
