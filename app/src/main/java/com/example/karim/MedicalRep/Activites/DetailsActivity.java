package com.example.karim.MedicalRep.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.karim.MedicalRep.Adapter.MsgAdapter;
import com.example.karim.MedicalRep.R;
import com.example.karim.MedicalRep.model.Msg;
import com.example.karim.MedicalRep.model.Order;
import com.example.karim.MedicalRep.shared;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class DetailsActivity extends AppCompatActivity {

    EditText msgTxt;
    RecyclerView rv;
    Order order= shared.order;
    List<Msg>msgList;
    MsgAdapter msgAdapter;
    TextView placeName,location,date,time,placeType,visitStata,conPerson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        rv=findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        msgTxt=findViewById(R.id.msgText);
        placeName=findViewById(R.id.placeName);
        placeName.setText(order.getPlaceName());
        location=findViewById(R.id.location);
        location.setText(order.getLocation());
        date=findViewById(R.id.date);
        date.setText(order.getDate());
        time=findViewById(R.id.time);
        time.setText(order.getTime());
        placeType=findViewById(R.id.placeType);
        placeType.setText(order.getPlacState());
        visitStata=findViewById(R.id.visitStata);
        visitStata.setText(order.getVisitState());
        conPerson=findViewById(R.id.conPerson);
        conPerson.setText(order.getContactPersonName());
        getMsgs();
    }

    public void send(View view) {
        Msg msg=new Msg();
        msg.setMsg(msgTxt.getText().toString());
        if(!msg.getMsg().equals(""))
        FirebaseDatabase.getInstance().getReference("Msg")
                .child(order.getPlaceName())
                .child(order.getTime())
                .child(msg.getMsg()).setValue(msg).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                getMsgs();
            }
        });
    }

    private void getMsgs() {
        msgList=new ArrayList<>();
        msgAdapter=new MsgAdapter(this,msgList);
        FirebaseDatabase.getInstance().getReference("Msg")
                .child(order.getPlaceName())
                .child(order.getTime()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dt:dataSnapshot.getChildren()){
                    Msg msg=dt.getValue(Msg.class);
                    msgList.add(msg);
                    msgTxt.setText("");
                }
                msgAdapter=new MsgAdapter(DetailsActivity.this,msgList);
                rv.setAdapter(msgAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
