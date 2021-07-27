package com.philimonnag.chat.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.philimonnag.chat.Model.Message;
import com.philimonnag.chat.R;
import com.philimonnag.chat.databinding.ItemReceivedBinding;
import com.philimonnag.chat.databinding.ItemSendBinding;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<Message>messagesArrayList;
    private FirebaseUser firebaseUser;
    final int RECEIVE_MSG=1;
    final int SENDER_MSG=2;
    public MessageAdapter(Context context, ArrayList<Message> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        Message model= messagesArrayList.get(position);
        if(model.getUserId().equals(firebaseUser.getUid())){
            return SENDER_MSG;
        }else {
            return RECEIVE_MSG;
        }

    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if(viewType==SENDER_MSG){
            View v = LayoutInflater.from(context).inflate(R.layout.item_send,parent,false);
            return new Sender(v);
        }else {
            View v= LayoutInflater.from(context).inflate(R.layout.item_received,parent,false);
            return new Receiver(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
      Message messages = messagesArrayList.get(position);
      if(holder.getClass()==Sender.class){
          Sender viewHolder=(Sender)holder;
          viewHolder.binding.sentMsg.setText(messages.getMsgTxt());
          viewHolder.binding.sendTime.setText(messages.getTimeStamp());
      }else {
          Receiver viewHolder=(Receiver)holder;
          viewHolder.binding.receivedMsg.setText(messages.getMsgTxt());
          viewHolder.binding.receivedTime.setText(messages.getTimeStamp());
      }

    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }
    public class Sender extends RecyclerView.ViewHolder{
        ItemSendBinding binding;
        public Sender(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = ItemSendBinding.bind(itemView);
        }
    }
    public class Receiver extends RecyclerView.ViewHolder{
        ItemReceivedBinding binding;
        public Receiver(@NonNull @NotNull View itemView) {
            super(itemView);
            binding= ItemReceivedBinding.bind(itemView);
        }
    }
}
