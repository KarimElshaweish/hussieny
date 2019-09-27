package com.example.karim.MedicalRep.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.karim.MedicalRep.R;


import java.util.ArrayList;
import java.util.List;

import com.example.karim.MedicalRep.model.Order;
import com.example.karim.MedicalRep.model.User;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GraphFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    ProgressDialog progressDialog;
    List<Order> orderList=new ArrayList<>();
    int cnt=0;
    int emp=0;
    ArrayList<String>label,value;
    private void getData() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Loading  data .....");
        progressDialog.show();
        FirebaseDatabase.getInstance().getReference("MedicalOrder").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                label = new ArrayList<>();
                value = new ArrayList<>();
                cnt = 0;
                orderList = new ArrayList<>();
                for (DataSnapshot dt : dataSnapshot.getChildren()) {
                    emp = 0;
                    for (DataSnapshot dt1 : dt.getChildren()) {
                        for (DataSnapshot dt2 : dt1.getChildren()) {
                            orderList.add(dt2.getValue(Order.class));
                            cnt++;
                            emp++;
                        }
                    }
                    label.add(dt.getKey());
                    value.add(String.valueOf(emp));
                }
                compEntry = new ArrayList();
                final ArrayList<String> labelsName = new ArrayList<>();
                FirebaseDatabase.getInstance().getReference("OrderUser").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int i = 0;
                        for (DataSnapshot dt : dataSnapshot.getChildren()) {
                            if (dt.getKey().equals(label.get(i))) {
                                i++;
                                User user = dt.getValue(User.class);
                                labelsName.add(user.getEmail());
                            }
                        }
                        i = 0;
                        for (String s : labelsName) {
                            float val = Float.parseFloat(value.get(i));
                            compEntry.add(new PieEntry(val / Float.parseFloat(String.valueOf(cnt)), s));
                            i++;
                        }
                        PieDataSet dataSet = new PieDataSet(compEntry, " 4 Comp");
                        PieData data = new PieData(dataSet);
                        pieChart.setData(data);
                        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                        pieChart.animateXY(5000, 5000);
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    ArrayList<PieEntry> compEntry;
    PieChart pieChart;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View view= inflater.inflate(R.layout.fragment_graph, container, false);
         pieChart = view.findViewById(R.id.piechart);

getData();



        BarChart chart = view.findViewById(R.id.barchart);

        ArrayList<BarEntry> NoOfEmp = new ArrayList();

        NoOfEmp.add(new BarEntry(945f, 0));
        NoOfEmp.add(new BarEntry(1040f, 1));
        NoOfEmp.add(new BarEntry(1133f, 2));
        NoOfEmp.add(new BarEntry(1240f, 3));

        ArrayList year = new ArrayList();

        year.add("2008");
        year.add("2009");
        year.add("2010");
        year.add("2011");
        year.add("2012");
        year.add("2013");
        year.add("2014");
        year.add("2015");
        year.add("2016");
        year.add("2017");

     //   BarDataSet bardataset = new BarDataSet(NoOfEmp, "No Of Employee");
        chart.animateY(5000);
        ArrayList<IBarDataSet>dataSets=new ArrayList<>();
        BarDataSet bardataset=new BarDataSet(NoOfEmp,"Company");
        dataSets.add(bardataset);
        BarData data1 = new BarData(dataSets);
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        chart.setData(data1);
         return view;
    }
}
