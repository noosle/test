package com.testapp.testapplication.core;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by noosle on 29.03.2017.
 */

public class MainManager {

    public static void loadPostsFromServer(final CallBack callBack) {

        Server.getAsyncHttpClient().get(Server.POSTS_ADDRESS, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                DBHelper.removeAllPosts();
                DBHelper.writePosts(response);
                callBack.onSuccess();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                try {
                    callBack.onFail(errorResponse.getString("error"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });

    }

}
