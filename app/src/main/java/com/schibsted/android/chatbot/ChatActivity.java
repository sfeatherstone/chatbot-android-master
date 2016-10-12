package com.schibsted.android.chatbot;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.schibsted.android.chatbot.model.ApplicationModel;
import com.schibsted.android.chatbot.model.ChatMessage;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private static final String jsonUrl = "https://s3-eu-west-1.amazonaws.com/rocket-interview/chat.json";
    private static final String LOWEST_ITEM = "lowestItem";
    private static ArrayAdapter mAdapter;
    // Construct the data source
    private static ArrayList<ChatMessage> arrayOfMessages = new ArrayList<>();
    private ApplicationModel model;
    private String loggedInUser;
    private ListView messageListView;
    private int prevLowestItemIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        model = ApplicationModel.getApplicationModel(getApplicationContext());
        loggedInUser = model.getUserModel().getLoggedInUser();
        if (TextUtils.isEmpty(loggedInUser)) {
            finish();
        }

        //Scroll to last position
        if (savedInstanceState != null) {
            prevLowestItemIndex = savedInstanceState.getInt(LOWEST_ITEM, 0);
        } else {
            prevLowestItemIndex = 0;
        }

        new FetchChatTask().execute();

        // Set the title on screen
        setTitle(getResources().getString(R.string.chat_window_pre) + loggedInUser);

        Toolbar toolbarView = (Toolbar) findViewById(R.id.my_toolbar);
        if (toolbarView != null) {
            setSupportActionBar(toolbarView);
        }

        setupChatButton();
    }

    private void setupChatButton() {
        Button sendButton = (Button) findViewById(R.id.send_message_button);
        if (sendButton != null) {
            final AppCompatActivity that = this;
            final TextView textView = (TextView) findViewById(R.id.chat_textview);
            messageListView = (ListView) findViewById(R.id.lvMessages);

            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChatMessage message = new ChatMessage(textView.getText().toString());
                    textView.setText("");
                    arrayOfMessages.add(message);
                    model.addMessage(message);
                    // Check if no view has focus:
                    View view = that.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    messageListView.post(new Runnable() {
                        @Override
                        public void run() {
                            // Select the last row so it will scroll into view...
                            messageListView.setSelection(mAdapter.getCount() - 1);
                        }
                    });

                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
        saveInstanceState.putInt(LOWEST_ITEM, messageListView.getLastVisiblePosition());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.chat_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.menu_item_logout:
                model.getUserModel().clearLoggedInUser();
                finish();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        return true;
    }

    private void setupAdapter() {
        // Create the adapter to convert the array to view
        mAdapter = new ChatAdapter(this, arrayOfMessages);

        // Attach the adapter to a ListView
        messageListView.setAdapter(mAdapter);

        if (prevLowestItemIndex > 0) {
            messageListView.post(new Runnable() {
                @Override
                public void run() {
                    // Select the last row so it will scroll into view...
                    messageListView.setSelection(prevLowestItemIndex);
                }
            });
        }

    }

    private class FetchChatTask extends AsyncTask<Void, Void, ArrayList<ChatMessage>> {
        @Override
        protected ArrayList<ChatMessage> doInBackground(Void... params) {
            return new ChatFetcher().fetchChats(jsonUrl);
        }

        @Override
        protected void onPostExecute(ArrayList<ChatMessage> items) {
            arrayOfMessages = items;
            //Add in prev messages
            arrayOfMessages.addAll(model.getAdditionalMessages());
            setupAdapter();
        }
    }
}
