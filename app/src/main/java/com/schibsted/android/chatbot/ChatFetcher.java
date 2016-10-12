package com.schibsted.android.chatbot;

import android.util.Log;

import com.schibsted.android.chatbot.model.ChatMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by simonfea on 12/10/2016.
 */

class ChatFetcher {
    private static final String TAG = "ChatFetcher";


    private String getUrlPayload(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage());
            }

            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toString();
        } finally {
            connection.disconnect();
        }
    }

    ArrayList<ChatMessage> fetchChats(String urlSpec) {
        ArrayList<ChatMessage> messages = new ArrayList<>();
        try {
            String jsonString = getUrlPayload(urlSpec);

            JSONObject jsonObject = new JSONObject(jsonString);

            parseMessages(messages, jsonObject);

        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }
        return messages;

    }

    private void parseMessages(ArrayList<ChatMessage> messages, JSONObject jsonObject) throws JSONException {

        JSONArray chatsJsonArray = jsonObject.getJSONArray("chats");

        for (int i = 0; i < chatsJsonArray.length(); i++) {

            JSONObject chat = chatsJsonArray.getJSONObject(i);
            String name = chat.getString("username");
            String messageText = chat.getString("content");
            String time = chat.getString("time");
            String pictureUrl = chat.getString("userImage_url");

            messages.add(new ChatMessage(name, messageText, time, pictureUrl));

        }
    }
}
