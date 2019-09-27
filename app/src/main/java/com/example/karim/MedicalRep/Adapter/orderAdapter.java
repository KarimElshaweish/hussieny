package com.example.karim.MedicalRep.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.karim.MedicalRep.Activites.DetailsActivity;
import com.example.karim.MedicalRep.model.Order;
import com.example.karim.MedicalRep.R;
import com.example.karim.MedicalRep.shared;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class orderAdapter extends RecyclerView.Adapter<orderAdapter.ViewHolder> {
    Context _ctx;
    List<Order> orderList;
    List<String>keyList;
    public orderAdapter(Context _ctx, List<Order> orderList,List<String>keyList) {
        this._ctx = _ctx;
        this.orderList = orderList;
        this.keyList=keyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(_ctx).inflate(R.layout.order_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final boolean oldseen=orderList.get(position).isSeen();
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderList.get(position).setSeen(true);
                FirebaseDatabase.getInstance().getReference("MedicalOrder")
                        .child(keyList.get(position))
                        .child(orderList.get(position).getPlaceName())
                        .child(orderList.get(position).getTime())
                        .setValue(orderList.get(position)).addOnSuccessListener(new OnSuccessListener<Void>() {@Override
                    public void onSuccess(Void aVoid) {
                            if(!oldseen)
                                changeUI(holder);
                        Intent intent=new Intent(_ctx, DetailsActivity.class);
                    shared.order=orderList.get(position);
                        _ctx.startActivity(intent);
                    }
                });
            }
        });
        holder.placeNameTxt.setText(orderList.get(position).getPlaceName());
        holder.date.setText(orderList.get(position).getDate());
        holder.placeName.setText(orderList.get(position).getPlaceName());
        holder.feedback.setText(orderList.get(position).getFeedback());
        holder.time.setText(orderList.get(position).getTime());
        holder.order.setText(orderList.get(position).getOrderValue());
        if(!orderList.get(position).isSeen()){
            changeUI(holder);
        }
    }

    private void changeUI(@NonNull ViewHolder holder) {
        holder.placeNameTxt.setTypeface(holder.placeNameTxt.getTypeface(),Typeface.BOLD);
        holder.placeNameTxt.setTextColor(_ctx.getResources().getColor(R.color.redApp));
        holder.date.setTypeface(holder.date.getTypeface(),Typeface.BOLD);
        holder.date.setTextColor(_ctx.getResources().getColor(R.color.redApp));
        holder.placeName.setTypeface(holder.placeName.getTypeface(),Typeface.BOLD);
        holder.placeName.setTextColor(_ctx.getResources().getColor(R.color.redApp));
        holder.feedback.setTypeface(holder.feedback.getTypeface(),Typeface.BOLD);
        holder.feedback.setTextColor(_ctx.getResources().getColor(R.color.redApp));
        holder.time.setTypeface(holder.time.getTypeface(),Typeface.BOLD);
        holder.time.setTextColor(_ctx.getResources().getColor(R.color.redApp));
        holder.order.setTypeface(holder.order.getTypeface(),Typeface.BOLD);
        holder.order.setTextColor(_ctx.getResources().getColor(R.color.redApp));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView placeNameTxt,date,placeName,feedback,time,order;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cv=itemView.findViewById(R.id.cv);
            placeNameTxt=itemView.findViewById(R.id.placeNameTxt);
            date=itemView.findViewById(R.id.date);
            placeName=itemView.findViewById(R.id.placeName);
            feedback=itemView.findViewById(R.id.feedback);
            time=itemView.findViewById(R.id.time);
            order=itemView.findViewById(R.id.order);
        }
    }
}
