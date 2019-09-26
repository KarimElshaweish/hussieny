package com.example.karim.muzzafapp.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.karim.muzzafapp.Adapter.orderAdapter;
import com.example.karim.muzzafapp.DataBase.Data;
import com.example.karim.muzzafapp.model.Order;
import com.example.karim.muzzafapp.R;
import com.example.karim.muzzafapp.shared;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;


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
    List<String>keyList;
    private void getData(){
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Loading  data .....");
        progressDialog.show();

        if(shared.admin){
            FirebaseDatabase.getInstance().getReference("MedicalOrder").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    keyList=new ArrayList<>();
                    orderList=new ArrayList<>();
                   for(DataSnapshot dt:dataSnapshot.getChildren())
                       for(DataSnapshot dt1:dt.getChildren())
                           for(DataSnapshot dt2: dt1.getChildren()) {
                               orderList.add(dt2.getValue(Order.class));
                               keyList.add(dt.getKey());
                           }


                    orderAdapter=new orderAdapter(getContext(),orderList,keyList);
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
                final String user=FirebaseAuth.getInstance().getCurrentUser().getUid();
                if(dataSnapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    FirebaseDatabase.getInstance().getReference("MedicalOrder").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot dt:dataSnapshot.getChildren())
                                    {
                                        for(DataSnapshot dt1:dt.getChildren()){

                                            com.example.karim.muzzafapp.model.Order order=dt1.getValue(com.example.karim.muzzafapp.model.Order.class);
                                         orderList.add(order);
                                         keyList.add(user);
                                        }
                                    }
                                    orderAdapter=new orderAdapter(getContext(),orderList,keyList);
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
