package com.example.karim.muzzafapp;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;


import com.example.karim.muzzafapp.Fragment.AddOrder;
import com.example.karim.muzzafapp.Fragment.GraphFragment;
import com.example.karim.muzzafapp.Fragment.MapFragment;
import com.example.karim.muzzafapp.Fragment.OrderList;
import com.example.karim.muzzafapp.model.PlaceData;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.Locale;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;


public class MainActivity extends AppCompatActivity implements LocationListener {





    ViewPager vp;
    private void __init__() {
        tab=findViewById(R.id.tb);
        vp=findViewById(R.id.vp);
        vp.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        tab.setupWithViewPager(vp);
        tab.getTabAt(0).setText("Order List");
        if(!shared.admin)
        tab.getTabAt(1).setText("New Order");
        tab.getTabAt(2).setText("Map");
        if(shared.admin)
        tab.getTabAt(1).setText("Graph");
    }

    LocationManager mLocationManager;
    Geocoder gecoder;
    Double longtuide,lontuide;
    List<Address>list;
    TabLayout tab;
    @Override
    public void onLocationChanged(Location location) {
        if(placeData!=null) {
            longtuide = location.getLongitude();
            lontuide = location.getLatitude();

            Location loc1 = new Location("");
            loc1.setLatitude( Double.parseDouble(placeData.getLituide()));
            loc1.setLongitude(Double.parseDouble(placeData.getLongtuide()));

            Location loc2 = new Location("");
            loc2.setLatitude(lontuide);
            loc2.setLongitude(longtuide);

            float distanceInMeters = loc1.distanceTo(loc2);
            gecoder = new Geocoder(getBaseContext(), Locale.getDefault());
            //finLocation.setText(String.valueOf(distanceInMeters));

        }
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
PlaceData placeData;
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkGPS() {
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location1 = mLocationManager.getLastKnownLocation(mLocationManager.NETWORK_PROVIDER);
        onLocationChanged(location1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w=getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_main);
            __init__();



    }


    public class  PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 2:
                    return new MapFragment();
                case 1:
                    if(!shared.admin)
                    return new AddOrder();
                    return new GraphFragment();
                case 0:
                    default:
                    return new OrderList();
            }
        }
        @Override
        public int getCount() {
            return 3;
        }
    }


}
