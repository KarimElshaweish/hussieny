package com.example.karim.MedicalRep.Fragment;

import android.app.ProgressDialog;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.karim.MedicalRep.MyItem;
import com.example.karim.MedicalRep.R;
import com.example.karim.MedicalRep.model.Order;
import com.example.karim.MedicalRep.shared;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View rootView= inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(this);
        return rootView;
    }

    HashMap<LatLng, com.example.karim.MedicalRep.model.Order>hashMap;
    List<LatLng>latLngList;
    ProgressDialog progressDialog;
    private void   getData(){


        FirebaseDatabase.getInstance().getReference("MedicalOrder").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                latLngList=new ArrayList<>();
                hashMap=new HashMap<>();
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
                                               LatLng latLng = new LatLng(order.getLituide(), order.getLongtuide());
                                               hashMap.put(latLng, order);
                                               latLngList.add(latLng);
                                               MyItem offsetItem = new MyItem(order.getLituide(), order.getLongtuide());
                                               mClusterManager.addItem(offsetItem);
                                           }
                                        }
                                    }
                                    if(latLngList.size()!=0)
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLngList.get(0).latitude,latLngList.get(0).longitude),7));
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



    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    GoogleMap mMap;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        mClusterManager = new ClusterManager<MyItem>(getContext(), mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
       // addItems();
        if(!shared.admin)
        getData();
        else
            getAllData();

    }

    private void getAllData() {

        FirebaseDatabase.getInstance().getReference("MedicalOrder").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mMap.clear();
                progressDialog=new ProgressDialog(getContext());
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setTitle("Loading  data .....");
                progressDialog.show();
                latLngList=new ArrayList<>();
                hashMap=new HashMap<>();
                for(DataSnapshot dt:dataSnapshot.getChildren())
                {
                    for(DataSnapshot dt1:dt.getChildren()) {

                        for (DataSnapshot dt2 : dt1.getChildren()) {
                            for(DataSnapshot dt3:dt2.getChildren()) {
                                com.example.karim.MedicalRep.model.Order order = dt3.getValue(com.example.karim.MedicalRep.model.Order.class);
                                LatLng latLng = new LatLng(order.getLituide(), order.getLongtuide());
                                hashMap.put(latLng, order);
                                latLngList.add(latLng);

                            }
                        }
                    }
                }
                if(latLngList.size()!=0) {
                    for(LatLng lat :latLngList) {
                        MyItem offsetItem = new MyItem(lat.latitude, lat.longitude);
                        mClusterManager.addItem(offsetItem);
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLngList.get(0).latitude, latLngList.get(0).longitude), 8));
                }progressDialog.dismiss();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private ClusterManager<MyItem> mClusterManager;
}
