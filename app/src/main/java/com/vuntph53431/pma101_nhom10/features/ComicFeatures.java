package com.vuntph53431.pma101_nhom10.features;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.vuntph53431.pma101_nhom10.R;
import com.vuntph53431.pma101_nhom10.adapter.TruyenAdapter;
import com.vuntph53431.pma101_nhom10.dao.TruyenDAO;
import com.vuntph53431.pma101_nhom10.model.Truyen;
import com.vuntph53431.pma101_nhom10.Screen.ComicScreen;

import java.util.ArrayList;

public class ComicFeatures {

    private Context context;
    private TruyenDAO truyenDAO;
    private String currentRole;

    public ComicFeatures(Context context, String currentRole) {
        this.context = context;
        this.currentRole = currentRole;
        truyenDAO = new TruyenDAO(context);
    }

    public ComicFeatures(Context context) {
        this.context = context;
        truyenDAO = new TruyenDAO(context);
    }

    public void themTruyen(ArrayList<Truyen> arrayList, TruyenAdapter adapter){
        // Kiểm tra vai trò của người dùng. Chỉ admin mới được thêm truyện.
        if ("admin".equalsIgnoreCase(currentRole)) {
           com.vuntph53431.pma101_nhom10.Screen.AddStoryDialogFragment dialogFragment =
                   com.vuntph53431.pma101_nhom10.Screen.AddStoryDialogFragment.newInstance();

            dialogFragment.setAddStoryDialogListener(new com.vuntph53431.pma101_nhom10.Screen.AddStoryDialogFragment.AddStoryDialogListener() {
                @Override
                public void onStoryAdded(Truyen truyen) {
                    boolean check = truyenDAO.themTruyen(truyen);
                    if (check) {arrayList.add(truyen);
                        adapter.notifyItemInserted(arrayList.size() - 1);
                        Toast.makeText(context, "Đã thêm truyện: " + truyen.getTenTruyen(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Lỗi khi thêm truyện vào database hoặc truyện đã tồn tại", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            if (context instanceof FragmentActivity) {
                dialogFragment.show(((FragmentActivity) context).getSupportFragmentManager(), "AddStoryDialog");
            } else {
                Toast.makeText(context, "Lỗi: Không thể hiển thị dialog.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Bạn không có quyền thêm truyện!", Toast.LENGTH_SHORT).show();
        }
    }

    public void suaTruyen(Truyen truyen, TruyenAdapter adapter){
        if ("admin".equalsIgnoreCase(currentRole)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_truyen, null);
            builder.setView(view);
            AlertDialog dialog = builder.create();
            dialog.show();

            Button btnHuy = view.findViewById(R.id.truyenDialog_btnTuChoi);
            Button btnLuu = view.findViewById(R.id.truyenDialog_btnDongY);
            TextView tvTenChucNang = view.findViewById(R.id.truyenDialog_tvTenDialog);
            EditText edTenTruyen = view.findViewById(R.id.truyenDialog_edTenTruyen);
            EditText edTacGia = view.findViewById(R.id.truyenDialog_edTacGia);
            EditText edNoiDung = view.findViewById(R.id.truyenDialog_edNoiDung);

            ImageView imgAnhBia = view.findViewById(R.id.truyenDialog_imgAnhBia);
            Button btnChonAnh = view.findViewById(R.id.truyenDialog_btnChonAnh);

            tvTenChucNang.setText("Sửa truyện");
            edTenTruyen.setText(truyen.getTenTruyen());
            edTacGia.setText(truyen.getTacGia());
            edNoiDung.setText(truyen.getNoiDung());

            if (btnChonAnh != null) {
                btnChonAnh.setOnClickListener(v -> {
                    if (context instanceof FragmentActivity) {
                        Toast.makeText(context, "Tính năng chọn ảnh sẽ được cập nhật sau.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Lỗi: Không thể mở thư viện ảnh.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            btnHuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btnLuu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tenTruyenMoi = edTenTruyen.getText().toString().trim();
                    String tacGiaMoi = edTacGia.getText().toString().trim();
                    String noiDungMoi = edNoiDung.getText().toString().trim();

                    // Validation chi tiết hơn
                    if (tenTruyenMoi.isEmpty()) {
                        edTenTruyen.setError("Vui lòng nhập tên truyện");
                        edTenTruyen.requestFocus();
                        return;
                    }
                    if (tacGiaMoi.isEmpty()) {
                        edTacGia.setError("Vui lòng nhập tác giả");
                        edTacGia.requestFocus();
                        return;
                    }
                    if (noiDungMoi.isEmpty()) {
                        edNoiDung.setError("Vui lòng nhập nội dung");
                        edNoiDung.requestFocus();
                        return;
                    }

                    try {
                        boolean check = truyenDAO.suaTruyen(truyen.getTenTruyen(), tenTruyenMoi, tacGiaMoi, noiDungMoi);
                        if (check) {
                            truyen.setTenTruyen(tenTruyenMoi);
                            truyen.setTacGia(tacGiaMoi);
                            truyen.setNoiDung(noiDungMoi);

                            adapter.notifyDataSetChanged();
                            Toast.makeText(context, "Sửa truyện thành công", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(context, "Lỗi khi sửa truyện hoặc tên truyện đã tồn tại", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, "Có lỗi xảy ra: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(context, "Bạn không có quyền sửa truyện!", Toast.LENGTH_SHORT).show();
        }
    }

    public void xoaTruyen(Truyen truyen, ArrayList<Truyen> arrayList, TruyenAdapter adapter){
        if ("admin".equalsIgnoreCase(currentRole)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Xóa truyện");
            builder.setMessage("Bạn xác nhận xóa cuốn truyện \"" + truyen.getTenTruyen() + "\"?");
            builder.setIcon(R.drawable.ic_warning); // Thêm icon cảnh báo nếu có

            builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        boolean check = truyenDAO.xoaTruyen(truyen);
                        if (check) {
                            // Lưu vị trí của item trước khi xóa để sử dụng notifyItemRemoved
                            int position = arrayList.indexOf(truyen);
                            arrayList.remove(truyen);

                            if (position != -1) {
                                adapter.notifyItemRemoved(position);
                            } else {
                                adapter.notifyDataSetChanged();
                            }

                            Toast.makeText(context, "Xóa truyện thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Lỗi khi xóa truyện", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, "Có lỗi xảy ra: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.show();
        } else {
            Toast.makeText(context, "Bạn không có quyền xóa truyện!", Toast.LENGTH_SHORT).show();
        }
    }

    public void docTruyen(String tenTruyen){
        Intent intent = new Intent(context, ComicScreen.class);
        Bundle bundle = new Bundle();
        bundle.putString("tenTruyen", tenTruyen);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public void closeDatabase() {
        if (truyenDAO != null) {
            truyenDAO.close();
        }
    }
}