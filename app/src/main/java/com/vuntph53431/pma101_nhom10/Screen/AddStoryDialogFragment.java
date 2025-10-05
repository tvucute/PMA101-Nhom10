package com.vuntph53431.pma101_nhom10.Screen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.vuntph53431.pma101_nhom10.model.Truyen;
import com.vuntph53431.pma101_nhom10.R;


public class AddStoryDialogFragment extends DialogFragment {

    private EditText editTextTenTruyen, editTextTacGia, editTextNoiDung;
    private Button buttonAddStory, buttonCancel; // Thêm buttonCancel
    private AddStoryDialogListener listener;

    public interface AddStoryDialogListener {
        void onStoryAdded(Truyen truyen);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_truyen, container, false);

        editTextTenTruyen = view.findViewById(R.id.truyenDialog_edTenTruyen);
        editTextTacGia = view.findViewById(R.id.truyenDialog_edTacGia);
        editTextNoiDung = view.findViewById(R.id.truyenDialog_edNoiDung);
        buttonAddStory = view.findViewById(R.id.truyenDialog_btnDongY);
        buttonCancel = view.findViewById(R.id.truyenDialog_btnTuChoi); // Ánh xạ button Hủy

        buttonAddStory.setOnClickListener(v -> {
            String tenTruyen = editTextTenTruyen.getText().toString().trim();
            String tacGia = editTextTacGia.getText().toString().trim();
            String noiDung = editTextNoiDung.getText().toString().trim();

            if (!tenTruyen.isEmpty() && !tacGia.isEmpty() && !noiDung.isEmpty()) {
                Truyen newStory = new Truyen(tenTruyen, tacGia, noiDung);
                if (listener != null) {
                    listener.onStoryAdded(newStory);
                }
                dismiss();
            } else {
                Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            }
        });

        buttonCancel.setOnClickListener(v -> {
            dismiss();
        });

        return view;
    }

    public void setAddStoryDialogListener(AddStoryDialogListener listener) {
        this.listener = listener;
    }

    public static AddStoryDialogFragment newInstance() {
        return new AddStoryDialogFragment();
    }
}
