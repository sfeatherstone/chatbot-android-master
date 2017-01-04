package com.gsx_r750.android.chatbot.model;

import android.content.Context;
import android.graphics.Color;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.gsx_r750.android.chatbot.data.Chat;

import java.util.ArrayList;

/**
 * Created by simonfea on 11/10/2016.
 */

public class ApplicationModel {
    static private ApplicationModel instance;
    static private ImageLoader imageLoader;
    private Context context;
    private ArrayList<Chat> additionalMessages = new ArrayList<>();

    private ApplicationModel(Context context) {
        this.context = context;
    }

    static public ApplicationModel getApplicationModel(Context context) {
        if (instance == null) {
            instance = new ApplicationModel(context);
        }
        return instance;
    }

    public UserModel getUserModel() {
        return new UserModel(context);
    }

    public ImageLoader getAvatarLoader() {
        if (imageLoader == null) {
            DisplayImageOptions defualtOption = new DisplayImageOptions.Builder()
                    .displayer(new CircleBitmapDisplayer(Color.WHITE, 4.0f))
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                    .defaultDisplayImageOptions(defualtOption)
                    .build();
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(config);
        }
        return imageLoader;
    }

    public void addMessage(Chat newMessage) {
        //TODO : In the real world we would send this to a server. And we might want to persist the messages already fetched.
        //TODO : So for this task, refetch the messages from the server each time, and add in what we added - but only keeping added messages in memory
        additionalMessages.add(newMessage);
    }

    public ArrayList<Chat> getAdditionalMessages() {
        return additionalMessages;
    }

    void clearUserData() {
        additionalMessages.clear();
        getAvatarLoader().clearMemoryCache();
        getAvatarLoader().clearDiskCache();
    }
}
