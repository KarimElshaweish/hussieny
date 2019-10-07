package com.example.karim.MedicalRep.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.karim.MedicalRep.Adapter.orderAdapter;
import com.example.karim.MedicalRep.model.Order;
import com.example.karim.MedicalRep.R;
import com.example.karim.MedicalRep.shared;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;


public class OrderList extends Fragment {




    public OrderList() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    RecyclerView rv;
    List<Order>orderList;
    orderAdapter orderAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_order_list, container, false);
        rv=v.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setHasFixedSize(true);
        orderList=new ArrayList<>();
        getData();
        return v;
    }
    ProgressDialog progressDialog;
    List<String>keyList,idKey;
    private void getData(){


        if(shared.admin){
            FirebaseDatabase.getInstance().getReference("MedicalOrder").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    progressDialog=new ProgressDialog(getContext());
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setTitle("Loading  data .....");
                    progressDialog.show();
                    keyList=new ArrayList<>();
                    idKey=new ArrayList<>();
                    orderList=new ArrayList<>();
                   for(DataSnapshot dt:dataSnapshot.getChildren())
                       for(DataSnapshot dt1:dt.getChildren())
                           for(DataSnapshot dt2: dt1.getChildren()) {
                               for(DataSnapshot dt3:dt2.getChildren()) {
                                       orderList.add(dt3.getValue(Order.class));
                                       keyList.add(dt1.getKey());
                                       idKey.add(dt.getKey());

                               }
                           }


                    orderAdapter=new orderAdapter(getContext(),orderList,keyList,idKey);
                    rv.setAdapter(orderAdapter);
                    progressDialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else
        FirebaseDatabase.getInstance().getReference("MedicalOrder").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList=new ArrayList<>();
                keyList=new ArrayList<>();
                idKey=new ArrayList<>();
                final String user=FirebaseAuth.getInstance().getCurrentUser().getUid();
                if(dataSnapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    FirebaseDatabase.getInstance().getReference("MedicalOrder").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    progressDialog=new ProgressDialog(getContext());
                                    progressDialog.setCanceledOnTouchOutside(false);
                                    progressDialog.setTitle("Loading  data .....");
                                    progressDialog.show();
                                    for(DataSnapshot dt:dataSnapshot.getChildren())
                                    {
                                        for(DataSnapshot dt1:dt.getChildren()){
                                            for(DataSnapshot dt2:dt1.getChildren()) {
                                                com.example.karim.MedicalRep.model.Order order = dt2.getValue(com.example.karim.MedicalRep.model.Order.class);
                                                orderList.add(order);
                                                keyList.add(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                                                idKey.add(user);
                                            }
                                        }
                                    }
                                    orderAdapter=new orderAdapter(getContext(),orderList,keyList,idKey);
                                    rv.setAdapter(orderAdapter);
                                    progressDialog.dismiss();
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
