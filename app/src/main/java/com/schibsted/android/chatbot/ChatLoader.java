package com.schibsted.android.chatbot;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.schibsted.android.chatbot.model.ChatMessage;

import java.util.ArrayList;

public class ChatLoader extends AsyncTaskLoader<ArrayList<ChatMessage>> {

    private ArrayList<ChatMessage> cachedData = null;

    public ChatLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (cachedData == null) {
            onForceLoad();
        } else {
            super.deliverResult(cachedData);
        }
    }

    @Override
    public ArrayList<ChatMessage> loadInBackground() {
        return new ChatFetcher().fetchChats(ChatActivity.jsonUrl);
    }

    @Override
    public void deliverResult(ArrayList<ChatMessage> data) {
        cachedData = data;
        if (isStarted()) {
            super.deliverResult(data);
        }
    }

}
