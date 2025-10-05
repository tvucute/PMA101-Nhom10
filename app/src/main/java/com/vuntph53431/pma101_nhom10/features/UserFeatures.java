package com.vuntph53431.pma101_nhom10.features;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;

import com.vuntph53431.pma101_nhom10.MainActivity;
import com.vuntph53431.pma101_nhom10.R;
import com.vuntph53431.pma101_nhom10.adapter.NguoiDungAdapter;
import com.vuntph53431.pma101_nhom10.dao.NguoiDungDAO;
import com.vuntph53431.pma101_nhom10.model.NguoiDung;
import com.vuntph53431.pma101_nhom10.Screen.ComicStoreScreen;

import java.util.ArrayList;

public class UserFeatures {

    private Context context;
    private NguoiDungDAO nguoiDungDAO;

    public UserFeatures(Context context) {
        this.context = context;
        nguoiDungDAO=new NguoiDungDAO(context);
    }

    public boolean kiemTraDangNhap(String tenNguoiDung, String matKhau) {
        if (tenNguoiDung.isEmpty() || matKhau.isEmpty()) {
            Toast.makeText(context, "Không được để trống!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            NguoiDung nguoiDung = nguoiDungDAO.traVeNguoiDung(tenNguoiDung);
            if (nguoiDung != null && matKhau.equals(nguoiDung.getMatKhau())) {

                // Lưu thông tin người dùng đã đăng nhập vào file "USER_DATA"
                context.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)
                        .edit()
                        .putString("USERNAME", tenNguoiDung)
                        .putString("ROLE", nguoiDung.getVaiTro() == 0 ? "admin" : "user")
                        .apply();

                dangNhap(tenNguoiDung);
                return true;
            } else {
                Toast.makeText(context, "Sai tên hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }


    public void dangNhap(String tenNguoiDung){
        Intent intent=new Intent(context, ComicStoreScreen.class);
        Bundle bundle=new Bundle();
        bundle.putString("tenNguoiDung",tenNguoiDung);
        intent.putExtras(bundle);
        context.startActivity(intent);
        ((Activity)context).finish();
    }

    public boolean dangKy(String tenNguoiDung, String matKhau, String matKhau2) {
        if (tenNguoiDung.isEmpty() || matKhau.isEmpty() || matKhau2.isEmpty()) {
            Toast.makeText(context, "Thông tin không hợp lệ", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!matKhau.equals(matKhau2)) {
            Toast.makeText(context, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            NguoiDung nguoiDung = new NguoiDung(tenNguoiDung, matKhau2, 1);
            boolean check = nguoiDungDAO.themNguoiDung(nguoiDung);
            if (check) {
                Toast.makeText(context, "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Toast.makeText(context, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }


    public void themNguoiDung(ArrayList<NguoiDung> arrayList,NguoiDungAdapter adapter){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View view= LayoutInflater.from(context).inflate(R.layout.dialog_nguoidung,null);
        builder.setView(view);
        AlertDialog dialog=builder.create();
        dialog.show();

        Button btnHuy=view.findViewById(R.id.nguoiDungDialog_btnTuChoi);
        Button btnThem=view.findViewById(R.id.nguoiDungDialog_btnDongY);
        EditText edTenNguoiDung=view.findViewById(R.id.nguoiDungDialog_edTenNguoiDung);
        EditText edMatKhau=view.findViewById(R.id.truyenDialog_edMatKhau);

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenNguoiDung = edTenNguoiDung.getText().toString();
                String matKhau = edMatKhau.getText().toString();

                if (tenNguoiDung.isEmpty() || matKhau.isEmpty()){
                    Toast.makeText(context, "Không được để trống !", Toast.LENGTH_SHORT).show();
                }else {
                    NguoiDung nguoiDung = new NguoiDung(tenNguoiDung,matKhau,1);
                    boolean check = nguoiDungDAO.themNguoiDung(nguoiDung);
                    if (check){
                        Toast.makeText(context, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        arrayList.add(nguoiDung);
                        adapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(context, "Người dùng đã tồn tại!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void xoaNguoiDung(NguoiDung nguoiDung, ArrayList<NguoiDung> arrayList, NguoiDungAdapter adapter){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xóa người dùng");
        builder.setMessage("Bạn có chắc chắn xóa người dùng này ?");
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                boolean check = nguoiDungDAO.xoaNguoiDung(nguoiDung);
                if (check){
                    arrayList.remove(nguoiDung);
                    Toast.makeText(context, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                }
            }
        });
        builder.show();
    }

    public void suaNguoiDung(NguoiDung nguoiDung, NguoiDungAdapter adapter){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View view= LayoutInflater.from(context).inflate(R.layout.dialog_nguoidung,null);
        builder.setView(view);
        AlertDialog dialog=builder.create();
        dialog.show();

        Button btnHuy=view.findViewById(R.id.nguoiDungDialog_btnTuChoi);
        Button btnThem=view.findViewById(R.id.nguoiDungDialog_btnDongY);
        EditText edTenNguoiDung=view.findViewById(R.id.nguoiDungDialog_edTenNguoiDung);
        EditText edMatKhau=view.findViewById(R.id.truyenDialog_edMatKhau);
        TextView tvTenDialog = view.findViewById(R.id.nguoiDungDialog_tvTenDialog);
        tvTenDialog.setText("Sửa Người Dùng");
        btnThem.setText("Lưu");
        edTenNguoiDung.setVisibility(View.GONE);

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String matKhauMoi = edMatKhau.getText().toString();
                if (matKhauMoi.isEmpty()){
                    Toast.makeText(context, "Không được để trống!", Toast.LENGTH_SHORT).show();
                } else if (matKhauMoi.equals(nguoiDung.getMatKhau())) {
                    Toast.makeText(context, "Mật khẩu phải khác mật khẩu cũ!", Toast.LENGTH_SHORT).show();
                } else {
                    nguoiDung.setMatKhau(matKhauMoi);
                    boolean check = nguoiDungDAO.suaNguoiDung(nguoiDung);
                    if (check){
                        Toast.makeText(context, "Thay đổi thành công!", Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }
            }
        });
    }

    public void hienThiMatKhau(ImageView imageView,TextView textView,String matKhau){
        if (matKhau.equals(textView.getText().toString())){
            imageView.setImageResource(R.drawable.eye_close_svgrepo_com);
            textView.setText("**********");
        }else {
            imageView.setImageResource(R.drawable.eye_open_svgrepo_com);
            textView.setText(matKhau);
        }
    }

    public void doiMatKhau(NguoiDung nguoiDung){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View view= LayoutInflater.from(context).inflate(R.layout.dialog_nguoidung,null);
        builder.setView(view);
        AlertDialog dialog=builder.create();
        dialog.show();

        Button btnHuy=view.findViewById(R.id.nguoiDungDialog_btnTuChoi);
        Button btnLuu =view.findViewById(R.id.nguoiDungDialog_btnDongY);
        EditText edMatKhau=view.findViewById(R.id.nguoiDungDialog_edTenNguoiDung);
        EditText edMatKhau2 =view.findViewById(R.id.truyenDialog_edMatKhau);
        TextView tvTenDialog = view.findViewById(R.id.nguoiDungDialog_tvTenDialog);
        tvTenDialog.setText("Thay đổi mật khẩu");
        btnLuu.setText("Lưu");
        edMatKhau.setHint("Nhập mật khẩu");
        edMatKhau2.setHint("Nhập lại mật khẩu");
        edMatKhau.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        edMatKhau2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String matKhau = edMatKhau.getText().toString();
                String matKhau2 = edMatKhau2.getText().toString();
                if (matKhau.isEmpty() || matKhau2.isEmpty()){
                    Toast.makeText(context, "Không được để trống!", Toast.LENGTH_SHORT).show();
                } else if (!matKhau.equals(matKhau2)) {
                    Toast.makeText(context, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                } else if (matKhau.equals(nguoiDung.getMatKhau())) {
                    Toast.makeText(context, "Mật khẩu phải khác mật khẩu cũ", Toast.LENGTH_SHORT).show();
                } else {
                    nguoiDung.setMatKhau(matKhau2);
                    boolean check = nguoiDungDAO.suaNguoiDung(nguoiDung);
                    if (check){
                        Toast.makeText(context, "Thay đổi thành công!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        new BasicFeatures(context).chuyenMan(MainActivity.class);
                    }
                }
            }
        });
    }

    public void kiemTraVaiTro(PopupMenu popupMenu,NguoiDung nguoiDung){
        Menu menu = popupMenu.getMenu();
        MenuItem quanLyNguoiDung = menu.findItem(R.id.quanLyNguoiDung);
        if (nguoiDung.getVaiTro()!=0){
            quanLyNguoiDung.setVisible(false);
        }
    }
}

