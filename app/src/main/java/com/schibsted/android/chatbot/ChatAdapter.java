package com.schibsted.android.chatbot;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.schibsted.android.chatbot.model.ChatMessage;
import com.schibsted.android.chatbot.ui.RoundedTransformation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    Context context;
    ArrayList<ChatMessage> messages = new ArrayList<>();

    ChatAdapter(Context context) {
        this.context = context;
    }

    void updateData(ArrayList<ChatMessage> data) {
        this.messages = data;
        notifyDataSetChanged();
    }

    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Check if an existing view is being reused, otherwise inflate the view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return populateViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChatMessage chatMessage = messages.get(position);

        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.inView.setVisibility(chatMessage.incoming ? VISIBLE : GONE);
        holder.outView.setVisibility(!chatMessage.incoming ? VISIBLE : GONE);

        // Lookup view for data population
        // Populate the data into the template view using the data object

        if (chatMessage.incoming) {
            holder.nameTimeIn.setText(chatMessage.userName + " - " + chatMessage.time);
            holder.textIn.setText(chatMessage.message);

            Picasso.with(context)
                    .load(chatMessage.userImageUrl)
                    .placeholder(R.drawable.ic_person_black_24dp)
                    .transform(new RoundedTransformation(0))
                    .resize(35, 35)
                    .into(holder.avatar);
        } else {
            holder.textOut.setText(chatMessage.message);
            holder.timeOut.setText(chatMessage.time);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return messages.size();
    }

    @NonNull
    private ViewHolder populateViewHolder(View convertView) {
        ViewHolder holder = new ViewHolder(convertView);
        holder.inView = convertView.findViewById(R.id.message_incoming);
        holder.outView = convertView.findViewById(R.id.message_outgoing);
        holder.textIn = (TextView) holder.inView.findViewById(R.id.chat_bubble_text);
        holder.nameTimeIn = (TextView) holder.inView.findViewById(R.id.chat_bubble_name_date);
        holder.avatar = (ImageView) holder.inView.findViewById(R.id.avatar_placeholder);
        holder.textOut = (TextView) holder.outView.findViewById(R.id.chat_bubble_text_out);
        holder.timeOut = (TextView) holder.outView.findViewById(R.id.chat_bubble_date_out);
        convertView.setTag(holder);
        return holder;
    }

    // View lookup cache
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textIn;
        TextView nameTimeIn;
        TextView textOut;
        TextView timeOut;
        ImageView avatar;
        View inView;
        View outView;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
