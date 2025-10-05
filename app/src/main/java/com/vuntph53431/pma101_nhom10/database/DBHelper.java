package com.vuntph53431.pma101_nhom10.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.vuntph53431.pma101_nhom10.model.Comment;
import com.vuntph53431.pma101_nhom10.model.DownloadedStory;
import com.vuntph53431.pma101_nhom10.model.Truyen;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "DBHelper";
    public static final String NAME_DB = "MANAGER.db";
    public static final int VERSION_DB = 3;

    public DBHelper(@Nullable Context context) {
        super(context, NAME_DB, null, VERSION_DB);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            // Tạo bảng người dùng
            db.execSQL("CREATE TABLE IF NOT EXISTS NGUOIDUNG (" +
                    "TENNGUOIDUNG TEXT PRIMARY KEY," +
                    "MATKHAU TEXT NOT NULL," +
                    "VAITRO INTEGER NOT NULL)");

            db.execSQL("INSERT OR IGNORE INTO NGUOIDUNG VALUES('ADMIN','ADMIN',0)");

            // Tạo bảng truyện với cột mới là ANHBIA
            db.execSQL("CREATE TABLE IF NOT EXISTS TRUYEN(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "TENTRUYEN TEXT NOT NULL," +
                    "TACGIA TEXT NOT NULL," +
                    "NOIDUNG TEXT NOT NULL," +
                    "ANHBIA TEXT)");

            // Thêm dữ liệu mẫu cho bảng TRUYEN
            insertSampleData(db);

            // Tạo bảng truyện đã tải
            db.execSQL("CREATE TABLE IF NOT EXISTS DownloadedStories (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "storyId INTEGER, " +
                    "title TEXT, " +
                    "content TEXT)");

            // Tạo bảng bình luận
            db.execSQL("CREATE TABLE IF NOT EXISTS Comments (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "storyId INTEGER, " +
                    "rating INTEGER, " +
                    "comment TEXT, " +
                    "username TEXT, " +
                    "created_at TEXT)");

            Log.d(TAG, "Database created successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error creating database", e);
        }
    }

    private void insertSampleData(SQLiteDatabase db) {
        // Thêm một số truyện mẫu để test
        db.execSQL("INSERT OR IGNORE INTO TRUYEN (TENTRUYEN, TACGIA, NOIDUNG, ANHBIA) VALUES " +
                "('Truyện Kiều', 'Nguyễn Du', 'Trăm năm trong cõi người ta, chữ tài chữ mệnh khéo là ghét nhau...', ''), " +
                "('Lão Hạc', 'Nam Cao', 'Lão Hạc là một người nông dân nghèo, sống cô đơn với chú chó cưng...', ''), " +
                "('Chí Phèo', 'Nam Cao', 'Chí Phèo là một tác phẩm nổi tiếng của nhà văn Nam Cao...', '')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            // Thêm cột ANHBIA nếu phiên bản cũ hơn 3
            if (oldVersion < 3) {
                db.execSQL("ALTER TABLE TRUYEN ADD COLUMN ANHBIA TEXT");
            }
            Log.d(TAG, "Database upgraded from version " + oldVersion + " to " + newVersion);
        } catch (Exception e) {
            Log.e(TAG, "Error upgrading database", e);
        }
    }


    public boolean isUserAdmin(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery("SELECT VAITRO FROM NGUOIDUNG WHERE TENNGUOIDUNG = ?", new String[]{username});
            if (cursor.moveToFirst()) {
                int role = cursor.getInt(cursor.getColumnIndexOrThrow("VAITRO"));
                return role == 0;
            }
            return false;
        } catch (Exception e) {
            Log.e(TAG, "Error checking user admin status", e);
            return false;
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }

    public boolean insertStory(Truyen story, String username) {
        if (!isUserAdmin(username) || story == null) {
            return false;
        }

        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("TENTRUYEN", story.getTenTruyen());
            values.put("TACGIA", story.getTacGia());
            values.put("NOIDUNG", story.getNoiDung());
            values.put("ANHBIA", story.getAnhBia() != null ? story.getAnhBia() : "");
            long result = db.insert("TRUYEN", null, values);
            return result != -1;
        } catch (Exception e) {
            Log.e(TAG, "Error inserting story", e);
            return false;
        } finally {
            if (db != null) db.close();
        }
    }

    public boolean updateStory(Truyen story, String username) {
        if (!isUserAdmin(username) || story == null) {
            return false;
        }

        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("TENTRUYEN", story.getTenTruyen());
            values.put("TACGIA", story.getTacGia());
            values.put("NOIDUNG", story.getNoiDung());
            values.put("ANHBIA", story.getAnhBia() != null ? story.getAnhBia() : "");
            int result = db.update("TRUYEN", values, "id = ?", new String[]{String.valueOf(story.getId())});
            return result > 0;
        } catch (Exception e) {
            Log.e(TAG, "Error updating story", e);
            return false;
        } finally {
            if (db != null) db.close();
        }
    }

    public boolean deleteStory(int storyId, String username) {
        if (!isUserAdmin(username)) {
            return false;
        }

        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            int result = db.delete("TRUYEN", "id = ?", new String[]{String.valueOf(storyId)});
            return result > 0;
        } catch (Exception e) {
            Log.e(TAG, "Error deleting story", e);
            return false;
        } finally {
            if (db != null) db.close();
        }
    }

    public List<Truyen> getAllStories() {
        List<Truyen> list = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM TRUYEN", null);
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("TENTRUYEN"));
                String author = cursor.getString(cursor.getColumnIndexOrThrow("TACGIA"));
                String content = cursor.getString(cursor.getColumnIndexOrThrow("NOIDUNG"));
                String anhBia = cursor.getString(cursor.getColumnIndexOrThrow("ANHBIA"));
                list.add(new Truyen(id, title, author, content, anhBia != null ? anhBia : ""));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting all stories", e);
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return list;
    }

    public Truyen getStoryByName(String storyName) {
        if (storyName == null || storyName.trim().isEmpty()) {
            return null;
        }

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM TRUYEN WHERE TENTRUYEN = ?", new String[]{storyName});
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("TENTRUYEN"));
                String author = cursor.getString(cursor.getColumnIndexOrThrow("TACGIA"));
                String content = cursor.getString(cursor.getColumnIndexOrThrow("NOIDUNG"));
                String anhBia = cursor.getString(cursor.getColumnIndexOrThrow("ANHBIA"));
                return new Truyen(id, title, author, content, anhBia != null ? anhBia : "");
            }
            return null;
        } catch (Exception e) {
            Log.e(TAG, "Error getting story by name", e);
            return null;
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }

    public boolean insertComment(int storyId, int rating, String comment, String username) {
        if (username == null || comment == null) {
            return false;
        }

        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("storyId", storyId);
            values.put("rating", rating);
            values.put("comment", comment);
            values.put("username", username);
            values.put("created_at", new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date()));
            long result = db.insert("Comments", null, values);
            return result != -1;
        } catch (Exception e) {
            Log.e(TAG, "Error inserting comment", e);
            return false;
        } finally {
            if (db != null) db.close();
        }
    }

    public List<Comment> getCommentsByStoryId(int storyId) {
        List<Comment> list = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM Comments WHERE storyId = ? ORDER BY created_at DESC",
                    new String[]{String.valueOf(storyId)});
            while (cursor.moveToNext()) {
                Comment comment = new Comment();
                comment.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                comment.setStoryId(cursor.getInt(cursor.getColumnIndexOrThrow("storyId")));
                comment.setRating(cursor.getInt(cursor.getColumnIndexOrThrow("rating")));
                comment.setContent(cursor.getString(cursor.getColumnIndexOrThrow("comment")));
                comment.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("username")));
                comment.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow("created_at")));
                list.add(comment);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting comments by story id", e);
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return list;
    }

    public boolean updateComment(int commentId, String newContent, int newRating, String username) {
        if (username == null || newContent == null) {
            return false;
        }

        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("comment", newContent);
            values.put("rating", newRating);
            int result = db.update("Comments", values, "id = ? AND username = ?",
                    new String[]{String.valueOf(commentId), username});
            return result > 0;
        } catch (Exception e) {
            Log.e(TAG, "Error updating comment", e);
            return false;
        } finally {
            if (db != null) db.close();
        }
    }

    public boolean deleteComment(int commentId, String username) {
        if (username == null) {
            return false;
        }

        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            int result = db.delete("Comments", "id = ? AND username = ?",
                    new String[]{String.valueOf(commentId), username});
            return result > 0;
        } catch (Exception e) {
            Log.e(TAG, "Error deleting comment", e);
            return false;
        } finally {
            if (db != null) db.close();
        }
    }

    public List<Comment> getAllComments() {
        List<Comment> list = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM Comments ORDER BY created_at DESC", null);
            while (cursor.moveToNext()) {
                Comment comment = new Comment();
                comment.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                comment.setStoryId(cursor.getInt(cursor.getColumnIndexOrThrow("storyId")));
                comment.setRating(cursor.getInt(cursor.getColumnIndexOrThrow("rating")));
                comment.setContent(cursor.getString(cursor.getColumnIndexOrThrow("comment")));
                comment.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("username")));
                comment.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow("created_at")));
                list.add(comment);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting all comments", e);
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return list;
    }

    public boolean insertDownloadedStory(int storyId, String title, String content) {
        if (title == null || content == null) {
            return false;
        }

        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("storyId", storyId);
            values.put("title", title);
            values.put("content", content);
            long result = db.insert("DownloadedStories", null, values);
            return result != -1;
        } catch (Exception e) {
            Log.e(TAG, "Error inserting downloaded story", e);
            return false;
        } finally {
            if (db != null) db.close();
        }
    }

    public boolean deleteDownloadedStory(int storyId) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            int result = db.delete("DownloadedStories", "storyId=?", new String[]{String.valueOf(storyId)});
            return result > 0;
        } catch (Exception e) {
            Log.e(TAG, "Error deleting downloaded story", e);
            return false;
        } finally {
            if (db != null) db.close();
        }
    }

    public List<DownloadedStory> getAllDownloadedStories() {
        List<DownloadedStory> list = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM DownloadedStories", null);
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("storyId"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String content = cursor.getString(cursor.getColumnIndexOrThrow("content"));
                list.add(new DownloadedStory(id, title, content));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting all downloaded stories", e);
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return list;
    }

    public boolean isStoryAlreadyDownloaded(int storyId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery("SELECT 1 FROM DownloadedStories WHERE storyId = ?", new String[]{String.valueOf(storyId)});
            return cursor.moveToFirst();
        } catch (Exception e) {
            Log.e(TAG, "Error checking if story is downloaded", e);
            return false;
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }

    public boolean authenticateUser(String username, String password) {
        if (username == null || password == null) {
            return false;
        }

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM NGUOIDUNG WHERE TENNGUOIDUNG = ? AND MATKHAU = ?",
                    new String[]{username, password});
            return cursor.moveToFirst();
        } catch (Exception e) {
            Log.e(TAG, "Error authenticating user", e);
            return false;
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }

    public boolean insertUser(String username, String password, int role) {
        if (username == null || password == null) {
            return false;
        }

        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("TENNGUOIDUNG", username);
            values.put("MATKHAU", password);
            values.put("VAITRO", role);
            long result = db.insert("NGUOIDUNG", null, values);
            return result != -1;
        } catch (Exception e) {
            Log.e(TAG, "Error inserting user", e);
            return false;
        } finally {
            if (db != null) db.close();
        }
    }

    public int getUserRole(String username) {
        if (username == null || username.trim().isEmpty()) {
            return -1; // Invalid role
        }

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery("SELECT VAITRO FROM NGUOIDUNG WHERE TENNGUOIDUNG = ?", new String[]{username});
            if (cursor.moveToFirst()) {
                return cursor.getInt(cursor.getColumnIndexOrThrow("VAITRO"));
            }
            return -1; // User not found
        } catch (Exception e) {
            Log.e(TAG, "Error getting user role", e);
            return -1;
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }
}
