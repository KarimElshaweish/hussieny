package com.example.karim.muzzafapp.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.karim.muzzafapp.Adapter.ListAdapter;
import com.example.karim.muzzafapp.model.TimerData;
import com.example.karim.muzzafapp.R;
import com.example.karim.muzzafapp.model.User;
import com.example.karim.muzzafapp.shared;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DayList extends AppCompatActivity {

    TextView nameTextView;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference mReference=database.getReference("User");
    DatabaseReference mReferenceTimer=database.getReference("Timer");
    RecyclerView rv;
    List<TimerData> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_list);
        list=new ArrayList<TimerData>();
        rv=findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));

        nameTextView=findViewById(R.id.nameText);
        mReference.child(shared.phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user;
                user=dataSnapshot.getValue(User.class);
                nameTextView.setText(user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mReferenceTimer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(shared.phoneNumber)){
                    mReferenceTimer.child(shared.phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot shot :dataSnapshot.getChildren()){
                                TimerData td=shot.getValue(TimerData.class);
                                String ph=td.getPhoneNumber();
                                if(ph.equals(shared.phoneNumber))
                                    list.add(td);
                            }
                            ListAdapter adapter=new ListAdapter(DayList.this,list);
                            rv.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
