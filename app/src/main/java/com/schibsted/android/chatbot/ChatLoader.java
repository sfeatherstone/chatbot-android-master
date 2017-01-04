package com.schibsted.android.chatbot;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.schibsted.android.chatbot.data.ChatAPI;
import com.schibsted.android.chatbot.data.Chats;

import java.io.IOException;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChatLoader extends AsyncTaskLoader<Chats> {

    private Chats cachedData = null;

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
    public Chats loadInBackground() {
        Retrofit retrofit = ChatAPI.RetrofitFactory.createRetrofitInstance(HttpLoggingInterceptor.Level.BODY);

        ChatAPI.API service = retrofit.create(ChatAPI.API.class);

        final Call<Chats> call = service.getChats();

        Response<Chats> callResponse = null;
        try {
            callResponse = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        switch (callResponse.code()) {
            case 200:
                return callResponse.body();
            default:
                break;
        }
        return new Chats();
    }

    @Override
    public void deliverResult(Chats data) {
        cachedData = data;
        if (isStarted()) {
            super.deliverResult(data);
        }
    }

}
