package com.vuntph53431.pma101_nhom10.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vuntph53431.pma101_nhom10.database.DBHelper;
import com.vuntph53431.pma101_nhom10.model.NguoiDung;

import java.util.ArrayList;
public class NguoiDungDAO {
    private DBHelper dbHelper;
    private SQLiteDatabase database;

    public NguoiDungDAO(Context context) {
        dbHelper=new DBHelper(context);
        database=dbHelper.getWritableDatabase();
    }

    public ArrayList<NguoiDung> toanBoNguoiDung(int vaiTro){
        ArrayList<NguoiDung> arrayList=new ArrayList<>();
        Cursor cursor=database.rawQuery("SELECT * FROM NGUOIDUNG WHERE VAITRO=?",
                new String[]{(vaiTro+"")});
        while (cursor.moveToNext()){
            NguoiDung nguoiDung=new NguoiDung();
            nguoiDung.setTenNguoiDung(cursor.getString(0));
            nguoiDung.setMatKhau(cursor.getString(1));
            nguoiDung.setVaiTro(cursor.getInt(2));
            arrayList.add(nguoiDung);
        }
        return arrayList;
    }

    public NguoiDung traVeNguoiDung(String tenNguoiDung){
        NguoiDung nguoiDung=new NguoiDung();
        Cursor cursor=database.rawQuery("SELECT * FROM NGUOIDUNG WHERE TENNGUOIDUNG=?",
                new String[]{tenNguoiDung});
        while (cursor.moveToNext()){
            nguoiDung.setTenNguoiDung(cursor.getString(0));
            nguoiDung.setMatKhau(cursor.getString(1));
            nguoiDung.setVaiTro(cursor.getInt(2));
        }
        return nguoiDung;
    }

    public boolean themNguoiDung(NguoiDung nguoiDung){
        ContentValues values=new ContentValues();
        values.put("TENNGUOIDUNG",nguoiDung.getTenNguoiDung());
        values.put("MATKHAU",nguoiDung.getMatKhau());
        values.put("VAITRO",nguoiDung.getVaiTro());
        long check=database.insert("NGUOIDUNG",null,values);
        return check>0;
    }

    public boolean suaNguoiDung(NguoiDung nguoiDung){
        ContentValues values=new ContentValues();
        values.put("MATKHAU",nguoiDung.getMatKhau());
        int check=database.update("NGUOIDUNG",values,"TENNGUOIDUNG=?",
                new String[]{nguoiDung.getTenNguoiDung()});
        return check>0;
    }

    public boolean xoaNguoiDung(NguoiDung nguoiDung){
        int check= database.delete("NGUOIDUNG","TENNGUOIDUNG=?",
                new String[]{nguoiDung.getTenNguoiDung()});
        return check>0;
    }
}
