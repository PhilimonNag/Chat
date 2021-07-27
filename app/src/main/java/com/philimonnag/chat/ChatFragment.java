package com.philimonnag.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philimonnag.chat.Adapters.MessageAdapter;
import com.philimonnag.chat.Model.Message;
import com.philimonnag.chat.Model.User;
import com.philimonnag.chat.databinding.FragmentChatBinding;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class ChatFragment extends Fragment {
private FragmentChatBinding binding;
    MessageAdapter messageAdapter;
    ArrayList<Message> messagesArrayList;
    FirebaseUser firebaseUser;
    String SenderRoom,ReceiverRoom,uEmail;
    String receiverUid;
    String senderUid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentChatBinding.inflate(inflater,container,false);
        View root=binding.getRoot();
        binding.textUserName.setText(getArguments().getString("uName"));
        Picasso.get().load(getArguments().getString("uImg")).into(binding.chatProfileImg);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        senderUid = firebaseUser.getUid();
        receiverUid=getArguments().getString("uId");
        uEmail=getArguments().getString("uEmail");
        SenderRoom =senderUid+receiverUid;
        ReceiverRoom=receiverUid+senderUid;
        messagesArrayList = new ArrayList<>();
        messageAdapter = new MessageAdapter(getContext(),messagesArrayList);
        binding.messageRV.setHasFixedSize(true);
        binding.messageRV.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false ));

        binding.messageRV.setAdapter(messageAdapter);
        FirebaseDatabase.getInstance().getReference().child("chats").
                child(SenderRoom).child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        messagesArrayList.clear();
                        if(snapshot.exists()){
                            for(DataSnapshot me:snapshot.getChildren()){
                                Message messages=me.getValue(Message.class);
                                messagesArrayList.add(messages);
                                binding.messageRV.scrollToPosition(messagesArrayList.lastIndexOf(messages));
                            }
                        }
                        messageAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_chatFragment_to_homeFragment);
            }
        });
        binding.sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mesText=binding.message.getText().toString();
                binding.message.setText("");
                SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
                String time = sdf.format(new Date());
                Message messages= new Message(mesText,SenderRoom,time,senderUid);

                HashMap<String , Object> last=new HashMap<>();
                last.put("lastMsg",messages.getMsgTxt());
                last.put("lastMsgtime",messages.getTimeStamp());
                FirebaseDatabase.getInstance().getReference().child("chats").child(SenderRoom).updateChildren(last);
                FirebaseDatabase.getInstance().getReference().child("chats").child(ReceiverRoom).updateChildren(last);
                FirebaseDatabase.getInstance().getReference().child("chats")
                        .child(SenderRoom)
                        .child("messages")
                        .push()
                        .setValue(messages).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Message messages= new Message(mesText,ReceiverRoom,time,senderUid);
                        FirebaseDatabase.getInstance().getReference().child("chats")
                                .child(ReceiverRoom)
                                .child("messages")
                                .push()
                                .setValue(messages).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
                    }
                });
            }
        });
        return root;
    }
}
