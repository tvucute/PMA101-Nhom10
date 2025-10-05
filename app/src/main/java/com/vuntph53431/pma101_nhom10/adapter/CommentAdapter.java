package com.vuntph53431.pma101_nhom10.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vuntph53431.pma101_nhom10.database.DBHelper;
import com.vuntph53431.pma101_nhom10.model.Comment;
import com.vuntph53431.pma101_nhom10.R;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private final List<Comment> commentList;
    private final Context context;
    private final String currentUser;
    private final DBHelper dbHelper;

    public CommentAdapter(Context context, List<Comment> commentList, String currentRole, String currentUser, DBHelper dbHelper) {
        this.context = context;
        this.commentList = commentList;
        this.currentUser = currentUser;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);

        holder.tvUsername.setText(comment.getUsername());
        holder.tvTime.setText(comment.getCreatedAt());
        holder.tvContent.setText(comment.getContent());
        holder.ratingBar.setRating(comment.getRating());

        boolean isOwner = comment.getUsername().equals(currentUser);

        // Hiển thị nút Sửa/Xóa chỉ khi người dùng là chủ sở hữu của bình luận
        if (isOwner) {
            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);

            holder.btnDelete.setOnClickListener(v -> {
                new AlertDialog.Builder(context)
                        .setTitle("Xoá bình luận")
                        .setMessage("Bạn có chắc chắn muốn xoá bình luận này không?")
                        .setPositiveButton("Xoá", (dialog, which) -> {
                            boolean deleted = dbHelper.deleteComment(comment.getId(), currentUser);
                            if (deleted) {
                                commentList.remove(position);
                                notifyItemRemoved(position);
                                Toast.makeText(context, "Bình luận đã được xóa", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Không thể xóa bình luận này", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Huỷ", null)
                        .show();
            });

            holder.btnEdit.setOnClickListener(v -> {
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(50, 40, 50, 10);

                EditText edtEditComment = new EditText(context);
                edtEditComment.setHint("Chỉnh sửa bình luận");
                edtEditComment.setText(comment.getContent());

                RatingBar ratingEditBar = new RatingBar(context, null, android.R.attr.ratingBarStyleSmall);
                ratingEditBar.setNumStars(5);
                ratingEditBar.setStepSize(1.0f);
                ratingEditBar.setRating(comment.getRating());

                layout.addView(edtEditComment);
                layout.addView(ratingEditBar);

                new AlertDialog.Builder(context)
                        .setTitle("Chỉnh sửa bình luận")
                        .setView(layout)
                        .setPositiveButton("Lưu", (dialog, which) -> {
                            String newContent = edtEditComment.getText().toString().trim();
                            float newRating = ratingEditBar.getRating();

                            if (!newContent.isEmpty()) {
                                boolean updated = dbHelper.updateComment(comment.getId(), newContent, (int) newRating, currentUser);
                                if (updated) {
                                    comment.setContent(newContent);
                                    comment.setRating(newRating);
                                    notifyItemChanged(position);
                                    Toast.makeText(context, "Bình luận đã được cập nhật", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Không thể cập nhật bình luận này", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Huỷ", null)
                        .show();
            });
        } else {
            holder.btnEdit.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvContent,tvTime;
        RatingBar ratingBar;
        Button btnEdit, btnDelete;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUser);
            tvContent = itemView.findViewById(R.id.tvContent);
            ratingBar = itemView.findViewById(R.id.ratingView);
            btnEdit = itemView.findViewById(R.id.btnEditComment);
            btnDelete = itemView.findViewById(R.id.btnDeleteComment);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}
