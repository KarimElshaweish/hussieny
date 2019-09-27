package com.example.karim.MedicalRep.Fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.karim.MedicalRep.R;
import com.example.karim.MedicalRep.model.Order;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GraphFragment extends Fragment {





    private void visitCount(){

        FirebaseDatabase.getInstance().getReference("MedicalOrder")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
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

        FirebaseDatabase.getInstance().getReference("MedicalOrder").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setTitle("Loading  data .....");
                progressDialog.show();
                label = new ArrayList<>();
                value = new ArrayList<>();
                cnt = 0;
                orderList = new ArrayList<>();
                for (DataSnapshot dt : dataSnapshot.getChildren()) {
                    emp = 0;
                    for (DataSnapshot dt1 : dt.getChildren()) {

                            label.add(dt1.getKey());
                            value.add(String.valueOf(dt1.getChildrenCount()));


                    }

                }
                progressDialog.dismiss();

                ArrayList<BarEntry>valueSets=new ArrayList<>();
                int i=0;
                for(String val:value){
                    valueSets.add(new BarEntry(Float.parseFloat(val),i++));
                }

                BarDataSet barDataSet1 = new BarDataSet(valueSets, "visits");
                barDataSet1.setColor(Color.rgb(0, 155, 0));
                ArrayList<BarDataSet> dataSets = new ArrayList<>();
                dataSets.add(barDataSet1);
                BarData data = new BarData(label, dataSets);
                barChart.setData(data);
                barChart.setDescription("Visits");
                barChart.animateXY(2000, 2000);
                barChart.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

 //   ArrayList<PieEntry> compEntry;
    BarChart barChart;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View view= inflater.inflate(R.layout.fragment_graph, container, false);
         barChart = view.findViewById(R.id.piechart);


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

//     //   BarDataSet bardataset = new BarDataSet(NoOfEmp, "No Of Employee");
//        chart.animateY(5000);
//        ArrayList<IBarDataSet>dataSets=new ArrayList<>();
//        BarDataSet bardataset=new BarDataSet(NoOfEmp,"Company");
//        dataSets.add(bardataset);
//        BarData data1 = new BarData(dataSets);
//        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
//        chart.setData(data1);
         return view;
    }
}
