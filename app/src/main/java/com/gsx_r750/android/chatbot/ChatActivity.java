package com.gsx_r750.android.chatbot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.gsx_r750.android.chatbot.data.Chat;
import com.gsx_r750.android.chatbot.data.Chats;
import com.gsx_r750.android.chatbot.model.ApplicationModel;
import com.gsx_r750.android.chatbot.ui.SpacesItemDecoration;

public class ChatActivity extends AppCompatActivity {

    public static final String jsonUrl = "http://samplesofworkserver20170103074058.azurewebsites.net/api/chat";
    private static final String LOWEST_ITEM = "lowestItem";
    // Construct the data source
    private ApplicationModel model;
    private String loggedInUser;
    private RecyclerView recyclerView;
    private ChatAdapter recyclerViewAdaptor;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private int prevLowestItemIndex;
    private RecyclerView.ItemDecoration dividerItemDecoration;

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

        //new FetchChatTask().execute();

        setupRecycleView();
        setupChatButton();

        // Set the title on screen
        setTitle(getResources().getString(R.string.chat_window_pre) + loggedInUser);

        Toolbar toolbarView = (Toolbar) findViewById(R.id.my_toolbar);
        if (toolbarView != null) {
            setSupportActionBar(toolbarView);
        }

        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        callLoader();
    }

    private void setupChatButton() {
        Button sendButton = (Button) findViewById(R.id.send_message_button);
        if (sendButton != null) {
            final AppCompatActivity that = this;
            final TextView textView = (TextView) findViewById(R.id.chat_textview);

            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Chat message = new Chat(textView.getText().toString());
                    textView.setText("");
                    //TODO add message
                    //arrayOfMessages.add(message);
                    //model.addMessage(message);
                    // Check if no view has focus:
                    View view = that.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            // Select the last row so it will scroll into view...
                            //recyclerView.setSelection(mAdapter.getCount() - 1);
                            recyclerView.scrollToPosition(recyclerViewAdaptor.getItemCount() - 1);
                        }
                    });

                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
        //saveInstanceState.putInt(LOWEST_ITEM, recyclerView.getLastVisiblePosition());
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

    private void setupRecycleView() {
        recyclerView = (RecyclerView) findViewById(R.id.lvMessages);
        recyclerView.setHasFixedSize(true);

        recyclerViewLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        dividerItemDecoration = new SpacesItemDecoration(3);
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Create the adapter to convert the array to view
        recyclerViewAdaptor = new ChatAdapter(this);

        // Attach the adapter to a ListView
        recyclerView.setAdapter(recyclerViewAdaptor);

        if (prevLowestItemIndex > 0) {
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    // Select the last row so it will scroll into view...
                    recyclerView.scrollToPosition(prevLowestItemIndex);
                }
            });
        }

    }

    private void callLoader() {
        LoaderManager.LoaderCallbacks<Chats> loaderManager = new LoaderManager.LoaderCallbacks<Chats>() {
            @Override
            public Loader<Chats> onCreateLoader(int id, Bundle args) {
                return new ChatLoader(ChatActivity.this);
            }

            @Override
            public void onLoadFinished(Loader<Chats> loader, Chats data) {
                recyclerViewAdaptor.updateData(data);
            }

            @Override
            public void onLoaderReset(Loader<Chats> loader) {
                recyclerViewAdaptor.updateData(new Chats());
            }
        };
        getSupportLoaderManager().initLoader(0, null, loaderManager);
    }

}

