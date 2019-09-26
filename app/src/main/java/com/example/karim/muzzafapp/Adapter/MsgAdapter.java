package com.example.karim.muzzafapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.karim.muzzafapp.R;
import com.example.karim.muzzafapp.model.Msg;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {
    Context _ctx;
    List<Msg> list;

    public MsgAdapter(Context _ctx, List<Msg> list) {
        this._ctx = _ctx;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(_ctx).inflate(R.layout.msg,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.msg.setText(list.get(position).getMsg());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView msg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            msg=itemView.findViewById(R.id.msg);
        }
    }
}
