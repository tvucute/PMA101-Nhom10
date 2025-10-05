package com.vuntph53431.pma101_nhom10.model;

public class Truyen {
    private int id;
    private String tenTruyen, tacGia, noiDung, anhBia;

    // 1. Constructor đầy đủ nhất - đây là constructor "chính"
    public Truyen(int id, String tenTruyen, String tacGia, String noiDung, String anhBia) {
        this.id = id;
        this.tenTruyen = tenTruyen;
        this.tacGia = tacGia;
        this.noiDung = noiDung;
        this.anhBia = anhBia;
    }

    // 2. Các constructor khác sẽ gọi đến constructor chính ở trên
    // Constructor không có ID
    public Truyen(String tenTruyen, String tacGia, String noiDung, String anhBia) {
        // Gọi constructor chính, truyền 0 làm giá trị mặc định cho id
        this(0, tenTruyen, tacGia, noiDung, anhBia);
    }

    // Constructor không có ID và ảnh bìa
    public Truyen(String tenTruyen, String tacGia, String noiDung) {
        // Gọi constructor chính, truyền id=0 và anhBia=null
        this(0, tenTruyen, tacGia, noiDung, null);
    }

    // Constructor có ID nhưng không có ảnh bìa
    public Truyen(int id, String tenTruyen, String tacGia, String noiDung) {
        // Gọi constructor chính, truyền null cho anhBia
        this(id, tenTruyen, tacGia, noiDung, null);
    }

    // Constructor mặc định (không tham số)
    public Truyen() {
        // Có thể để trống hoặc gọi constructor chính với các giá trị null/mặc định
        // this(0, null, null, null, null);
    }

    // Getters and Setters (Giữ nguyên không đổi)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenTruyen() {
        return tenTruyen;
    }

    public void setTenTruyen(String tenTruyen) {
        this.tenTruyen = tenTruyen;
    }

    public String getTacGia() {
        return tacGia;
    }

    public void setTacGia(String tacGia) {
        this.tacGia = tacGia;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getAnhBia() {
        return anhBia;
    }

    public void setAnhBia(String anhBia) {
        this.anhBia = anhBia;
    }
}