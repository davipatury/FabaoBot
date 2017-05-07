package net.davipatury.fabaobot.controllers;

/**
 *
 * @author Davi
 */
public class Statistics {
    
    private int messagesInSession = 0;
    private int commandsInSession = 0;
    private int requestedMemes = 0;
    
    public void increaseMessagesInSession(int increaseBy) {
        messagesInSession += increaseBy;
    }
    
    public void increaseCommandsInSession(int increaseBy) {
        commandsInSession += increaseBy;
    }
    
    public void increaseRequestedMemes(int increaseBy) {
        requestedMemes += increaseBy;
    }
    
    public int messagesInSession() {
        return messagesInSession;
    }
    
    public int commandsInSession() {
        return commandsInSession;
    }
    
    public int requestedMemes() {
        return requestedMemes;
    }
}
