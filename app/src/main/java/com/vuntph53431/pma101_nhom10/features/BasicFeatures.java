package com.vuntph53431.pma101_nhom10.features;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vuntph53431.pma101_nhom10.MainActivity;
import com.vuntph53431.pma101_nhom10.R;
import com.vuntph53431.pma101_nhom10.Screen.DownloadedStoriesActivity;
import com.vuntph53431.pma101_nhom10.Screen.ManagerUserScreen;
import com.vuntph53431.pma101_nhom10.Screen.PolicyScreen;
import com.vuntph53431.pma101_nhom10.Screen.UserSettingScreen;

public class BasicFeatures {

    private Context context;

    public BasicFeatures(Context context) {
        this.context = context;
    }

    public void chuyenMan(Class mClass){
        Intent intent=new Intent(context,mClass);
        context.startActivity(intent);
        ((Activity)context).finish();
    }

    public void chuyenManNangCao(String tenNguoidung,Class mClass){
        Intent intent=new Intent(context, mClass);
        Bundle bundle = new Bundle();
        bundle.putString("tenNguoiDung",tenNguoidung);
        intent.putExtras(bundle);
        context.startActivity(intent);
        ((Activity)context).finish();
    }

    public void xacNhanThoat(){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setMessage("Bạn xác nhận muốn thoát ứng dụng?");
        builder.setNegativeButton("Ở lại", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("Thoát", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((Activity)context).finish();
            }
        });
        builder.show();
    }

    public void xacNhanDangXuat(){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setMessage("Bạn xác nhận muốn đăng xuất?");
        builder.setNegativeButton("Ở lại", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                chuyenMan(MainActivity.class);
            }
        });
        builder.show();
    }

    public PopupMenu caiDatMenu(ImageView imageView,String tenNguoiDung){
        PopupMenu popupMenu=new PopupMenu(context,imageView);
        popupMenu.getMenuInflater().inflate(R.menu.nav_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId()== R.id.khoTruyen){
                    new UserFeatures(context).dangNhap(tenNguoiDung);
                } else if (item.getItemId()== R.id.dangXuat) {
                    xacNhanDangXuat();
                } else if (item.getItemId()== R.id.thoat) {
                    xacNhanThoat();
                } else if (item.getItemId()== R.id.quanLyNguoiDung) {
                    chuyenManNangCao(tenNguoiDung, ManagerUserScreen.class);
                } else if (item.getItemId()== R.id.caiDat) {
                    chuyenManNangCao(tenNguoiDung, UserSettingScreen.class);
                } else if (item.getItemId()== R.id.dieuKhoanDichVu) {
                    chuyenManNangCao(tenNguoiDung, PolicyScreen.class);
                }else if (item.getItemId()== R.id.taixuong) {
                    chuyenManNangCao(tenNguoiDung, DownloadedStoriesActivity.class);
                }
                return true;
            }
        });

        return popupMenu;
    }

    public void caiDatRecycleView(RecyclerView recyclerView){
        LinearLayoutManager manager=new LinearLayoutManager(context);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
    }

}

