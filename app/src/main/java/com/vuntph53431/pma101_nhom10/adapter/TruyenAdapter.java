package com.vuntph53431.pma101_nhom10.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vuntph53431.pma101_nhom10.features.ComicFeatures;
import com.vuntph53431.pma101_nhom10.model.Truyen;
import com.vuntph53431.pma101_nhom10.R;

import java.util.ArrayList;

public class TruyenAdapter extends RecyclerView.Adapter<TruyenAdapter.TruyenHolder>{

    private Context context;
    private ArrayList<Truyen> arrayList;
    private int vaiTro; // Vai trò của người dùng: 0 cho quản trị viên, 1 cho người dùng thường.
    private ComicFeatures comicFeatures; // Tạo instance duy nhất
    private String currentUsername; // Thêm username để truyền vào ComicFeatures

    public TruyenAdapter(Context context, ArrayList<Truyen> arrayList, int vaiTro) {
        this.context = context;
        this.arrayList = arrayList;
        this.vaiTro = vaiTro;
        this.currentUsername = "ADMIN"; // Mặc định là ADMIN, có thể thay đổi qua setter

        // Khởi tạo ComicFeatures một lần trong constructor
        try {
            this.comicFeatures = new ComicFeatures(context, currentUsername);
        } catch (Exception e) {
            // Nếu constructor ComicFeatures yêu cầu khác, thử các cách khác
            try {
                this.comicFeatures = new ComicFeatures(context);
            } catch (Exception ex) {
                // Log lỗi hoặc xử lý theo cách khác
                ex.printStackTrace();
            }
        }
    }


    public TruyenAdapter(Context context, ArrayList<Truyen> arrayList, int vaiTro, String username) {
        this.context = context;
        this.arrayList = arrayList;
        this.vaiTro = vaiTro;
        this.currentUsername = username != null ? username : "ADMIN";

        // Khởi tạo ComicFeatures với username
        try {
            this.comicFeatures = new ComicFeatures(context, currentUsername);
        } catch (Exception e) {
            try {
                this.comicFeatures = new ComicFeatures(context);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // Setter cho username
    public void setCurrentUsername(String username) {
        this.currentUsername = username;
        // Khởi tạo lại ComicFeatures với username mới
        try {
            this.comicFeatures = new ComicFeatures(context, currentUsername);
        } catch (Exception e) {
            try {
                this.comicFeatures = new ComicFeatures(context);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // Các phương thức getter và setter cho ArrayList
    public ArrayList<Truyen> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<Truyen> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged(); // Cần gọi để cập nhật RecyclerView khi dữ liệu thay đổi.
    }

    @NonNull
    @Override
    public TruyenHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Khởi tạo view cho mỗi item trong RecyclerView
        View view = LayoutInflater.from(context).inflate(R.layout.item_truyen, parent, false);
        return new TruyenHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TruyenHolder holder, int position) {
        try {
            // Gán dữ liệu cho các view trong mỗi item
            Truyen truyen = arrayList.get(position);

            // Đặt tên truyện và tên tác giả
            holder.tvTenTruyen.setText(truyen.getTenTruyen());
            holder.tvTacGia.setText(truyen.getTacGia());

            // Ẩn nút Xóa và Sửa nếu vai trò không phải là quản trị viên
            if (vaiTro != 0) {
                holder.layoutNutBam.setVisibility(View.GONE);
            } else {
                holder.layoutNutBam.setVisibility(View.VISIBLE);
            }

            // Xử lý sự kiện click cho các nút chức năng
            if (comicFeatures != null) {
                holder.btnXoa.setOnClickListener(v -> {
                    try {
                        comicFeatures.xoaTruyen(truyen, arrayList, this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                holder.btnSua.setOnClickListener(v -> {
                    try {
                        comicFeatures.suaTruyen(truyen, this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                // Xử lý sự kiện click để đọc truyện
                View.OnClickListener readClickListener = v -> {
                    try {
                        comicFeatures.docTruyen(truyen.getTenTruyen());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                };

                holder.tvTenTruyen.setOnClickListener(readClickListener);
                holder.itemView.setOnClickListener(readClickListener);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        // Trả về tổng số lượng item trong danh sách
        return arrayList != null ? arrayList.size() : 0;
    }


    public static class TruyenHolder extends RecyclerView.ViewHolder {

        private TextView tvTenTruyen, tvTacGia;
        private ImageView btnSua, btnXoa;
        private LinearLayout layoutNutBam;

        public TruyenHolder(@NonNull View itemView) {
            super(itemView);
            try {
                // Ánh xạ các view từ layout item_truyen
                tvTenTruyen = itemView.findViewById(R.id.khoTruyen_tvTenTruyen);
                tvTacGia = itemView.findViewById(R.id.khoTruyen_tvTacGia);
                btnSua = itemView.findViewById(R.id.khoTruyen_btnSuaTruyen);
                btnXoa = itemView.findViewById(R.id.khoTruyen_btnXoatruyen);
                layoutNutBam = itemView.findViewById(R.id.khoTruyen_layoutNutBam);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Phương thức helper để refresh adapter
    public void refreshData(ArrayList<Truyen> newData) {
        if (newData != null) {
            this.arrayList.clear();
            this.arrayList.addAll(newData);
            notifyDataSetChanged();
        }
    }

    // Phương thức để remove item
    public void removeItem(int position) {
        if (position >= 0 && position < arrayList.size()) {
            arrayList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, arrayList.size());
        }
    }

    // Phương thức để remove item theo object
    public void removeItem(Truyen truyen) {
        int position = arrayList.indexOf(truyen);
        if (position != -1) {
            removeItem(position);
        }
    }
}

