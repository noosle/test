package com.testapp.testapplication.posts;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.testapp.testapplication.R;
import com.testapp.testapplication.core.MyApp;

import java.util.List;

/**
 * Created by noosle on 29.03.2017.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private List<Post> posts;

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView body;
        LinearLayout mainLayout;

        ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            body = (TextView) v.findViewById(R.id.body);
            mainLayout = (LinearLayout) v.findViewById(R.id.mainLayout);

        }
    }


    public PostAdapter(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Post post = posts.get(position);
        holder.title.setText(post.getTitle());
        holder.body.setText(post.getBody());

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("postsReceiver");
                intent.putExtra("position", position);
                intent.putExtra("action", "remove");
                intent.putExtra("post", post);
                MyApp.getContext().sendBroadcast(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


}