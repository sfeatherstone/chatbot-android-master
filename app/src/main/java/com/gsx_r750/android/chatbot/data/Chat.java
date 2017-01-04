package com.gsx_r750.android.chatbot.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by simonfea on 28/10/2016.
 */

public class Chat {

    private String username;
    private String content;
    @SerializedName("userImage_url")
    private String userImageUrl;
    private String time;
    @Expose(serialize = false, deserialize = false)
    private boolean incoming = true;

    /**
     * No args constructor for use in serialization
     *
     */
    public Chat() {
    }

    public Chat(String message) {
        this.content = message;
        this.time = new SimpleDateFormat("h:mm").format(new Date());
        this.incoming = false;
    }
    /**
     *
     * @param content
     * @param time
     * @param username
     * @param userImageUrl
     */
    public Chat(String username, String content, String userImageUrl, String time) {
        this.username = username;
        this.content = content;
        this.userImageUrl = userImageUrl;
        this.time = time;
    }

    /**
     *
     * @return
     * The username
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     * The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return
     * The content
     */
    public String getContent() {
        return content;
    }

    /**
     *
     * @param content
     * The content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     *
     * @return
     * The userImageUrl
     */
    public String getUserImageUrl() {
        return userImageUrl;
    }

    /**
     *
     * @param userImageUrl
     * The userImage_url
     */
    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    /**
     *
     * @return
     * The time
     */
    public String getTime() {
        return time;
    }

    /**
     *
     * @param time
     * The time
     */
    public void setTime(String time) {
        this.time = time;
    }

    public boolean getIncoming() {
        return this.incoming;
    }

}