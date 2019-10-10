package com.example.karim.MedicalRep.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.karim.MedicalRep.R;
import com.example.karim.MedicalRep.model.Order;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;


public class AddOrder extends Fragment implements LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText locationText,placeName,orderDate,orderTime,CotnactPersonName,contactTitle,orderValue
            ,ContactPhoneNumber,keyPersonName,keyTitle,keyPhoneNumber,keyEmail,keycomments,feedback,contactEmail,contactComments;
    RadioGroup statGroupRadioButton,visitStatGroupRadioButton;
    Button btnUpload;
    public void setData(){
        if(longtuide!=-1) {
            Order order=new Order();
            int selectID=statGroupRadioButton.getCheckedRadioButtonId();
            RadioButton rd=getView().findViewById(selectID);
            if(rd!=null)
            order.setPlacState(rd.getText().toString());
            int selectId2=visitStatGroupRadioButton.getCheckedRadioButtonId();
            RadioButton rd2=getView().findViewById(selectId2);
            if(rd2!=null)
            order.setVisitState(rd2.getText().toString());
            order.setFeedback(feedback.getText().toString());
            order.setKeyPersonComments(keycomments.getText().toString());
            order.setKeyPersonEmail(keyEmail.getText().toString());
            order.setContactPersonPhoneNumber(keyPhoneNumber.getText().toString());
            order.setContatTitle(contactTitle.getText().toString());
            order.setTime(orderTime.getText().toString());
            order.setDate(orderDate.getText().toString());
            order.setContactPersonPhoneNumber(ContactPhoneNumber.getText().toString());
            order.setContactPersonName(CotnactPersonName.getText().toString());
            order.setLocation(locationText.getText().toString());
            order.setLituide(latiude);
            order.setLongtuide(longtuide);
            if(placeName.getText().toString().equals("")){
                Toast.makeText(getContext(),"please open GPS and restart app ",Toast.LENGTH_LONG).show();
            }else {
                order.setPlaceName(placeName.getText().toString());
                order.setKeyPersonName(keyPersonName.getText().toString());
                order.setKeyPersonTitle(keyTitle.getText().toString());
                order.setContactEmail(contactEmail.getText().toString());
                order.setContactComments(contactComments.getText().toString());
                order.setOrderValue(orderValue.getText().toString());
                publishOrder(order);
            }
        }else{
            Toast.makeText(getContext(), "please open the gps", Toast.LENGTH_SHORT).show();
        }
    }

    Order order;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkGPS();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }
    LocationManager mLocationManager;
    Double longtuide=-1.0, latiude=-1.0;
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        Locale locale = new Locale("en");
        Geocoder geoCoder = new Geocoder(getActivity(), locale);
        StringBuilder builder = new StringBuilder();
        try {
            List<Address> address = geoCoder.getFromLocation(lat, lng, 1);
            int maxLines = address.get(0).getMaxAddressLineIndex();
            for (int i = 0; i < maxLines; i++) {
                String addressStr = address.get(0).getAddressLine(i);
                builder.append(addressStr);
                builder.append(" ");
            }

            String fnialAddress = address.get(0).getLocality();
            longtuide=address.get(0).getLongitude();
            latiude=address.get(0).getLatitude();
            locationText.setText(fnialAddress);


        } catch (IOException e) {
            // Handle IOException
        } catch (NullPointerException e) {
            // Handle NullPointerException
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

    private FusedLocationProviderClient fusedLocationClient;
    private void checkGPS() {
        mLocationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            onLocationChanged(location);
                        }
                    }
                });
    }
    ProgressDialog progressDialog;
    public void publishOrder(Order order){
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setTitle("Loading..");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        FirebaseDatabase.getInstance().getReference("MedicalOrder")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())
                .child(order.getPlaceName())
                .child(order.getTime())
                .setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
               progressDialog.dismiss();
               reset();
            }
        });
    }



    public void reset(){
        locationText.setText("");
        placeName.setText("");
        orderTime.setText("");
        orderDate.setText("");
        CotnactPersonName.setText("");
        contactTitle.setText("");
        ContactPhoneNumber.setText("");
        keyPersonName.setText("");
        keyTitle.setText("");
        keyPhoneNumber.setText("");
        keyEmail.setText("");
        keycomments.setText("");
        feedback.setText("");
        contactComments.setText("");
        contactEmail.setText("");
        orderValue.setText("");
        statGroupRadioButton.clearCheck();
        visitStatGroupRadioButton.clearCheck();
        checkGPS();
        resetTime();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable  ViewGroup container,
                            @Nullable Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_add_order, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
       visitStatGroupRadioButton=v.findViewById(R.id.visitStatGroupRadioButton);
        statGroupRadioButton=v.findViewById(R.id.statGroupRadioButton);
         locationText=v.findViewById(R.id.locationName);
        placeName=v.findViewById(R.id.placeName);
        orderTime=v.findViewById(R.id.orderTime);
        orderDate=v.findViewById(R.id.orderDate);
        CotnactPersonName=v.findViewById(R.id.CotnactPersonName);
        contactTitle=v.findViewById(R.id.contactTitle);
        ContactPhoneNumber=v.findViewById(R.id.ContactPhoneNumber);
        keyPersonName=v.findViewById(R.id.keyPersonName);
        keyTitle=v.findViewById(R.id.keyTitle);
        keyPhoneNumber=v.findViewById(R.id.keyPhoneNumber);
        keyEmail=v.findViewById(R.id.keyEmail);
        keycomments=v.findViewById(R.id.keycomments);
        feedback=v.findViewById(R.id.feedback);
        contactEmail=v.findViewById(R.id.contactEmail);
        contactComments=v.findViewById(R.id.contactcomments);
        orderValue=v.findViewById(R.id.orderValue);
       resetTime();
        btnUpload=v.findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
            }
        });


         return v;
    }
    private void resetTime(){
        Date C = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat df1 = new SimpleDateFormat("hh:mm");
        orderDate.setText(df.format(C));
        orderTime.setText(df1.format(C));
    }

}
