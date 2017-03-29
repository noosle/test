package com.testapp.testapplication.core;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.testapp.testapplication.posts.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by noosle on 29.03.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static String POSTS_DB = "posts.db";
    private static int DATABASE_VERSION = 1;

    private static final String POSTS_TABLE = "POSTS_TABLE";
    private static final String POST_ID = "POST_ID";
    private static final String USER_ID = "USER_ID";
    private static final String TITLE = "TITLE";
    private static final String BODY = "BODY";

    private static final String POSTS_CREATE = "CREATE TABLE IF NOT EXISTS " + POSTS_TABLE + " (_id INTEGER PRIMARY KEY, " + POST_ID + " INTEGER, " + USER_ID + " INTEGER," + TITLE + " TEXT, " + BODY + " TEXT);";


    public DBHelper(Context context) {
        super(context, POSTS_DB, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(POSTS_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + POSTS_TABLE);
        onCreate(db);

    }

    static void writePosts(JSONArray body) {
        DBHelper dbHelper = new DBHelper(MyApp.getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv;
        JSONObject postObject;
        for (int i = 0; i < body.length(); i++) {
            cv = new ContentValues();
            try {
                postObject = body.getJSONObject(i);
                cv.put(POST_ID, postObject.getInt("id"));
                cv.put(USER_ID, postObject.getInt("userId"));
                cv.put(TITLE, postObject.getString("title"));
                cv.put(BODY, postObject.getString("body"));
                db.insert(POSTS_TABLE, null, cv);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        db.close();

    }

    public static void removeAllPosts() {
        DBHelper dbHelper = new DBHelper(MyApp.getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from " + POSTS_TABLE);
        db.close();
    }

    public static JSONObject deletePost(Post post) {
        DBHelper dbHelper = new DBHelper(MyApp.getContext());
        JSONObject jsonObject = new JSONObject();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from " + POSTS_TABLE + " where " + TITLE + "='" + post.getTitle() + "' AND " + BODY + "='" + post.getBody() + "'");
        db.close();

        return jsonObject;
    }

    public static void addPost(Post post) {
        DBHelper dbHelper = new DBHelper(MyApp.getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TITLE, post.getTitle());
        cv.put(BODY, post.getBody());
        db.insert(POSTS_TABLE, null, cv);

        db.close();

    }

    public static List<Post> getPosts() {
        List<Post> posts = new ArrayList<>();
        DBHelper dbHelper = new DBHelper(MyApp.getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = "SELECT * FROM " + POSTS_TABLE;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                posts.add(new Post(cursor.getInt(cursor.getColumnIndex(POST_ID)), cursor.getInt(cursor.getColumnIndex(USER_ID)), cursor.getString(cursor.getColumnIndex(TITLE)), cursor.getString(cursor.getColumnIndex(BODY))));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        Collections.reverse(posts);
        return posts;

    }

    public static boolean isEmpty() {
        DBHelper dbHelper = new DBHelper(MyApp.getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = "SELECT " + BODY + " FROM " + POSTS_TABLE;
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();

        return count <= 0;

    }
}
