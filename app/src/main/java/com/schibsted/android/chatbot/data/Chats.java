package com.schibsted.android.chatbot.data;

/**
 * Created by simonfea on 28/10/2016.
 */

import java.util.ArrayList;
import java.util.List;

public class Chats {

    private List<Chat> chats = new ArrayList<Chat>();

    /**
     * No args constructor for use in serialization
     *
     */
    public Chats() {
    }

    /**
     *
     * @param chats
     */
    public Chats(List<Chat> chats) {
        this.chats = chats;
    }

    /**
     *
     * @return
     * The chats
     */
    public List<Chat> getChats() {
        return chats;
    }

    /**
     *
     * @param chats
     * The chats
     */
    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }

}
