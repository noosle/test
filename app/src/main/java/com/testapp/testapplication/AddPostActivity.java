package com.testapp.testapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.testapp.testapplication.core.DBHelper;
import com.testapp.testapplication.posts.Post;

/**
 * Created by noosle on 29.03.2017.
 */

@SuppressWarnings("ConstantConditions")
public class AddPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        final EditText title = (EditText) findViewById(R.id.title);
        final EditText body = (EditText) findViewById(R.id.body);

        FloatingActionButton savePostFab = (FloatingActionButton) findViewById(R.id.savePostFab);

        savePostFab.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               if (title.getText().toString().length() == 0) {
                                                   Snackbar.make(body, "Please enter title", Snackbar.LENGTH_SHORT).show();
                                                   return;
                                               }
                                               if (body.getText().toString().length() == 0) {
                                                   Snackbar.make(body, "Please enter title", Snackbar.LENGTH_SHORT).show();
                                                   return;
                                               }
                                               Post post = new Post(0, 0, title.getText().toString(), body.getText().toString());
                                               DBHelper.addPost(post);
                                               Intent intent = new Intent("postsReceiver");
                                               intent.putExtra("post", post);
                                               intent.putExtra("action", "add");
                                               sendBroadcast(intent);
                                               finish();
                                           }

                                       }

        );
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
