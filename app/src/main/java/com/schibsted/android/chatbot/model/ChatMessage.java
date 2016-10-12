package com.schibsted.android.chatbot.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by claudiopalumbo on 27/04/2016.
 */
public class ChatMessage {
    public String userName;
    public String message;
    public String time;
    public String userImageUrl;
    public boolean incoming;

    public ChatMessage(String username, String message, String time, String userImageUrl) {
        this.userName = username;
        this.message = message;
        if (time.charAt(time.length() - 1) == 'h') {
            time = time.substring(0, time.length() - 1);
        }
        this.time = time;
        this.userImageUrl = userImageUrl;
        this.incoming = true;
    }

    public ChatMessage(String message) {
        this.message = message;
        this.time = new SimpleDateFormat("h:mm").format(new Date());
        this.incoming = false;
    }

}
