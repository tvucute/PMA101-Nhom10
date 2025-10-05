package com.vuntph53431.pma101_nhom10.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vuntph53431.pma101_nhom10.database.DBHelper;
import com.vuntph53431.pma101_nhom10.model.Truyen;

import java.util.ArrayList;

public class TruyenDAO {

    private DBHelper dbHelper;
    private SQLiteDatabase database;

    public TruyenDAO(Context context) {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public ArrayList<Truyen> toanBoTruyen(){
        ArrayList<Truyen> arrayList = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = database.rawQuery("SELECT * FROM TRUYEN", null);
            while (cursor.moveToNext()){
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String tenTruyen = cursor.getString(cursor.getColumnIndexOrThrow("TENTRUYEN"));
                String tacGia = cursor.getString(cursor.getColumnIndexOrThrow("TACGIA"));
                String noiDung = cursor.getString(cursor.getColumnIndexOrThrow("NOIDUNG"));

                Truyen truyen = new Truyen(id, tenTruyen, tacGia, noiDung);
                arrayList.add(truyen);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return arrayList;
    }

    public Truyen truyenCuThe(String tenTruyen) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT id, TENTRUYEN, TACGIA, NOIDUNG FROM TRUYEN WHERE TENTRUYEN = ?",
                    new String[]{tenTruyen});
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(0);
                String ten = cursor.getString(1);
                String tacGia = cursor.getString(2);
                String noiDung = cursor.getString(3);
                return new Truyen(id, ten, tacGia, noiDung);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public boolean themTruyen(Truyen truyen){
        try {
            // Kiểm tra xem truyện đã tồn tại chưa
            if (kiemTraTruyenTonTai(truyen.getTenTruyen())) {
                return false; // Truyện đã tồn tại
            }

            ContentValues values = new ContentValues();
            values.put("TENTRUYEN", truyen.getTenTruyen());
            values.put("TACGIA", truyen.getTacGia());
            values.put("NOIDUNG", truyen.getNoiDung());

            long check = database.insert("TRUYEN", null, values);
            return check > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean suaTruyen(String tenTruyenCu, String tenTruyenMoi, String tacGiaMoi, String noiDungMoi){
        try {
            // Kiểm tra xem tên truyện mới có trung với truyện khác không (trừ chính nó)
            if (!tenTruyenCu.equals(tenTruyenMoi) && kiemTraTruyenTonTai(tenTruyenMoi)) {
                return false; // Tên truyện mới đã tồn tại
            }

            ContentValues values = new ContentValues();
            values.put("TENTRUYEN", tenTruyenMoi);
            values.put("TACGIA", tacGiaMoi);
            values.put("NOIDUNG", noiDungMoi);

            int check = database.update("TRUYEN", values, "TENTRUYEN=?",
                    new String[]{tenTruyenCu});
            return check > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean xoaTruyen(Truyen truyen){
        try {
            int check = database.delete("TRUYEN", "TENTRUYEN=?",
                    new String[]{truyen.getTenTruyen()});
            return check > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    private boolean kiemTraTruyenTonTai(String tenTruyen) {
        Cursor cursor = null;
        try {
            cursor = database.rawQuery("SELECT COUNT(*) FROM TRUYEN WHERE TENTRUYEN = ?",
                    new String[]{tenTruyen});
            if (cursor.moveToFirst()) {
                return cursor.getInt(0) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return false;
    }


    public void close() {
        if (database != null && database.isOpen()) {
            database.close();
        }
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
