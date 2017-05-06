package net.davipatury.fabaobot.controllers;

/**
 *
 * @author Davi
 */
public class Statistics {
    
    private int messagesInSession = 0;
    private int commandsInSession = 0;
    
    public void increaseMessagesInSession(int increaseBy) {
        messagesInSession += 1;
    }
    
    public void increaseCommandsInSession(int increaseBy) {
        commandsInSession += 1;
    }
    
    public int messagesInSession() {
        return messagesInSession;
    }
    
    public int commandsInSession() {
        return commandsInSession;
    }
}
