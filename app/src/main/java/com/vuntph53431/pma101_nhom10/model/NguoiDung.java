package com.vuntph53431.pma101_nhom10.model;

public class NguoiDung {

    private String tenNguoiDung,matKhau;
    private int vaiTro;

    public NguoiDung() {
    }

    public NguoiDung(String tenNguoiDung, String matKhau, int vaiTro) {
        this.tenNguoiDung = tenNguoiDung;
        this.matKhau = matKhau;
        this.vaiTro = vaiTro;
    }

    public String getTenNguoiDung() {
        return tenNguoiDung;
    }

    public void setTenNguoiDung(String tenNguoiDung) {
        this.tenNguoiDung = tenNguoiDung;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public int getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(int vaiTro) {
        this.vaiTro = vaiTro;
    }
}