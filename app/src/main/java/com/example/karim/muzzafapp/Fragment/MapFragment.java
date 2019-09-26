package com.example.karim.muzzafapp.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.karim.muzzafapp.DataBase.Order;
import com.example.karim.muzzafapp.MyItem;
import com.example.karim.muzzafapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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

    HashMap<LatLng, com.example.karim.muzzafapp.model.Order>hashMap;
    List<LatLng>latLngList;
    ProgressDialog progressDialog;
    private void getData(){

        progressDialog=new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Loading  data .....");
        progressDialog.show();
        FirebaseDatabase.getInstance().getReference("MedicalOrder").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                latLngList=new ArrayList<>();
                hashMap=new HashMap<>();
                if(dataSnapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    FirebaseDatabase.getInstance().getReference("MedicalOrder").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot dt:dataSnapshot.getChildren())
                                    {
                                        for(DataSnapshot dt1:dt.getChildren()){

                                            com.example.karim.muzzafapp.model.Order order=dt1.getValue(com.example.karim.muzzafapp.model.Order.class);
                                            LatLng latLng=new LatLng(order.getLituide(),order.getLongtuide());
                                            hashMap.put(latLng,order);
                                            latLngList.add(latLng);
                                            MyItem offsetItem = new MyItem(order.getLituide(), order.getLongtuide());
                                            mClusterManager.addItem(offsetItem);
                                        }
                                    }
                                    if(latLngList.size()!=0)
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLngList.get(0).latitude,latLngList.get(0).longitude),6));
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

    private void setMarkers() {

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
        getData();

    }
    private ClusterManager<MyItem> mClusterManager;
    private void addItems() {

        // Set some lat/lng coordinates to start with.
        double lat = 51.5145160;
        double lng = -0.1270060;

        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < 10; i++) {
            double offset = i / 60d;
            lat = lat + offset;
            lng = lng + offset;
            MyItem offsetItem = new MyItem(lat, lng);
            mClusterManager.addItem(offsetItem);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
