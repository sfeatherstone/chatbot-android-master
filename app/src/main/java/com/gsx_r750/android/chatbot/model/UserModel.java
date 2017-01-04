package com.gsx_r750.android.chatbot.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatDrawableManager;
import android.text.TextUtils;

import com.gsx_r750.android.chatbot.R;

import java.util.regex.Pattern;

/**
 * Created by simonfea on 11/10/2016.
 */

public class UserModel {

    private final static String USER_SETTINGS = "UserSettings";
    private final static String LOGGED_IN_USER = "LoggedInUser";
    private static Bitmap defaultAvatar;
    private String existingUsers[] = {"Carrie", "Anthony", "Eleanor", "Rodney", "Oliva", "Merve", "Lily"};
    private Context context;
    private SharedPreferences userSettings;

    UserModel(Context context) {
        this.context = context;
    }


    private String validateNumberOfNames(String input) {
        Pattern pattern = Pattern.compile("^[a-zA-Z]+\\s[a-zA-Z]+$");
        if (!pattern.matcher(input).matches()) {
            return context.getResources().getString(R.string.error_invalid_name);
        }
        return null;
    }

    private String validateNameAgainstExisting(String input) {
        String[] parts = input.split(" ");
        if (parts.length == 0) {
            return context.getResources().getString(R.string.error_invalid_name);
        }
        for (String existingUser : existingUsers) {
            if (existingUser.compareToIgnoreCase(parts[0]) == 0) {
                return context.getResources().getString(R.string.error_existing_user);
            }
        }
        return null;
    }

    //TODO add some unit test for validator - not sure how with context
    public String validateName(String input) {
        String error = validateNumberOfNames(input);
        if (!TextUtils.isEmpty(error)) {
            return error;
        }
        return validateNameAgainstExisting(input);
    }

    public String getLoggedInUser() {
        return getSharedPreferences().getString(LOGGED_IN_USER, null);
    }

    public void setLoggedInUser(String user) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(LOGGED_IN_USER, user);
        editor.apply();
    }

    public void clearLoggedInUser() {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.remove(LOGGED_IN_USER);
        editor.apply();
        // Clear all stored information on Logout
        ApplicationModel.getApplicationModel(context).clearUserData();
    }

    public Bitmap getPlaceholderImage() {
        if (defaultAvatar == null) {
            Drawable drawable = AppCompatDrawableManager.get().getDrawable(context, R.drawable.ic_person_black_24dp);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                drawable = (DrawableCompat.wrap(drawable)).mutate();
            }

            defaultAvatar = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(defaultAvatar);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }

        return defaultAvatar;
    }


    private SharedPreferences getSharedPreferences() {
        if (userSettings == null) {
            userSettings = context.getSharedPreferences(USER_SETTINGS, Context.MODE_PRIVATE);
        }
        return userSettings;
    }
}
