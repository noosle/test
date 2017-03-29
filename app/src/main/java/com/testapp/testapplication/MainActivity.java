package com.testapp.testapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.testapp.testapplication.core.CallBack;
import com.testapp.testapplication.core.DBHelper;
import com.testapp.testapplication.core.MainManager;
import com.testapp.testapplication.core.MyApp;
import com.testapp.testapplication.core.Server;
import com.testapp.testapplication.posts.Post;
import com.testapp.testapplication.posts.PostAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout refreshListViewLayout;
    private RecyclerView postsRecyclerView;
    private PostAdapter postAdapter;
    private List<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        postsRecyclerView = (RecyclerView) findViewById(R.id.postsRecyclerView);
        postsRecyclerView.setHasFixedSize(true);

        refreshListViewLayout = (SwipeRefreshLayout) findViewById(R.id.refreshListViewLayout);
        refreshListViewLayout.setOnRefreshListener(this);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(MyApp.getContext());
        postsRecyclerView.setLayoutManager(layoutManager);

        FloatingActionButton addPostFab = (FloatingActionButton) findViewById(R.id.addPostFab);

        addPostFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddPostActivity.class));
            }
        });

        if (!Server.isOnline()) {
            setupPosts();
            return;
        }

        if (!DBHelper.isEmpty())
            setupPosts();
        else {
            loadPosts();
        }
    }

    private void loadPosts() {
        refreshListViewLayout.setRefreshing(true);
        MainManager.loadPostsFromServer(new CallBack() {
            @Override
            public void onSuccess() {
                refreshListViewLayout.setRefreshing(false);
                setupPosts();
            }

            @Override
            public void onFail(String error) {
                refreshListViewLayout.setRefreshing(false);
                Snackbar.make(postsRecyclerView, error, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        loadPosts();
    }

    void setupPosts() {
        posts = DBHelper.getPosts();
        postAdapter = new PostAdapter(posts);
        postsRecyclerView.setAdapter(postAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(postsReceiver, new IntentFilter("postsReceiver"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(postsReceiver);
    }


    private BroadcastReceiver postsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent.getStringExtra("action").equals("add")) {
                posts.add(0, (Post) intent.getSerializableExtra("post"));

                postAdapter.notifyDataSetChanged();
                return;
            }
            if (intent.getStringExtra("action").equals("remove")) {

                AlertDialog alertDialog;

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.AppCompatAlertDialogStyle);
                alertDialogBuilder.setMessage("Delete post?");

                alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        posts.remove(intent.getIntExtra("position", 0));
                        DBHelper.deletePost((Post) intent.getSerializableExtra("post"));
                        postAdapter.notifyDataSetChanged();
                    }
                });

                alertDialogBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });


                alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        }
    };
}
