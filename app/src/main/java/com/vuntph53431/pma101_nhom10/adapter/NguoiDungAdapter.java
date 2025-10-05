package com.vuntph53431.pma101_nhom10.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vuntph53431.pma101_nhom10.R;
import com.vuntph53431.pma101_nhom10.features.UserFeatures;
import com.vuntph53431.pma101_nhom10.model.NguoiDung;

import java.util.ArrayList;

public class NguoiDungAdapter extends RecyclerView.Adapter<NguoiDungAdapter.NguoiDungHolder>{

    private Context context;
    private ArrayList<NguoiDung> arrayList;

    public NguoiDungAdapter(Context context, ArrayList<NguoiDung> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public ArrayList<NguoiDung> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<NguoiDung> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public NguoiDungHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_truyen,parent,false);
        return new NguoiDungHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NguoiDungHolder holder, int position) {
        NguoiDung nguoiDung = arrayList.get(position);
        UserFeatures userFeatures = new UserFeatures(context);
        holder.imgLogoNguoiDung.setImageResource(R.drawable.user);
        holder.tvTenNguoiDung.setText(nguoiDung.getTenNguoiDung());
        holder.tvMatKhau.setText(nguoiDung.getMatKhau());
        holder.btnXoaNguoiDung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userFeatures.xoaNguoiDung(nguoiDung,arrayList,NguoiDungAdapter.this);
            }
        });
        holder.btnSuaNguoiDung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userFeatures.suaNguoiDung(nguoiDung,NguoiDungAdapter.this);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public  static class NguoiDungHolder extends RecyclerView.ViewHolder {

        private ImageView imgLogoNguoiDung, btnXoaNguoiDung, btnSuaNguoiDung;
        private TextView tvTenNguoiDung, tvMatKhau;
        public NguoiDungHolder(@NonNull View itemView) {
            super(itemView);
            imgLogoNguoiDung = itemView.findViewById(R.id.khoTruyen_imgLogoTruyen);
            btnXoaNguoiDung = itemView.findViewById(R.id.khoTruyen_btnXoatruyen);
            btnSuaNguoiDung = itemView.findViewById(R.id.khoTruyen_btnSuaTruyen);
            tvTenNguoiDung = itemView.findViewById(R.id.khoTruyen_tvTenTruyen);
            tvMatKhau = itemView.findViewById(R.id.khoTruyen_tvTacGia);
        }
    }
}
