package com.schibsted.android.chatbot;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.schibsted.android.chatbot.model.ApplicationModel;
import com.schibsted.android.chatbot.model.ChatMessage;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by claudiopalumbo on 27/04/2016.
 */
class ChatAdapter extends ArrayAdapter<ChatMessage> {
    ChatAdapter(Context context, ArrayList<ChatMessage> messages) {
        super(context, R.layout.item_message, messages);
    }

    @Override
    public
    @NonNull
    View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ChatMessage chatMessage = getItem(position);

        ViewHolder holder;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_message, parent, false);
            holder = populateViewHolder(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.inView.setVisibility(chatMessage.incoming ? VISIBLE : GONE);
        holder.outView.setVisibility(!chatMessage.incoming ? VISIBLE : GONE);

        // Lookup view for data population
        // Populate the data into the template view using the data object

        if (chatMessage.incoming) {
            holder.nameTimeIn.setText(chatMessage.userName + " - " + chatMessage.time);
            holder.textIn.setText(chatMessage.message);

            ImageLoader imageLoader = ApplicationModel.getApplicationModel(getContext()).getAvatarLoader();
            Bitmap bitmap = imageLoader.getMemoryCache().get(chatMessage.userImageUrl);
            if (bitmap == null) {
                holder.avatar.setImageBitmap(ApplicationModel.getApplicationModel(getContext()).getUserModel().getPlaceholderImage());
                imageLoader.displayImage(chatMessage.userImageUrl, holder.avatar);
            }
        } else {
            holder.textOut.setText(chatMessage.message);
            holder.timeOut.setText(chatMessage.time);
        }

        // Return the completed view to render on screen
        return convertView;
    }

    @NonNull
    private ViewHolder populateViewHolder(View convertView) {
        ViewHolder holder = new ViewHolder();
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
    private static class ViewHolder {
        TextView textIn;
        TextView nameTimeIn;
        TextView textOut;
        TextView timeOut;
        ImageView avatar;
        View inView;
        View outView;
    }
}
