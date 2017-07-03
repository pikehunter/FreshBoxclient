package com.synerit.freshboxclient.app.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.synerit.freshboxclient.R;
import com.synerit.freshboxclient.app.Categories;
import com.synerit.freshboxclient.app.DataHolder;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager = null;
    private GridLayoutManager gridLayoutManager = null;

    private FirebaseRecyclerAdapter<Categories, PostViewHolder> firebaseRecyclerAdapter;

    private DatabaseReference mCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mCategory = FirebaseDatabase.getInstance().getReference().child("Categories");
    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Categories, PostViewHolder>(

                Categories.class,
                R.layout.item_main,
                PostViewHolder.class,
                mCategory

        )

        {
            @Override
            protected void populateViewHolder(final PostViewHolder viewHolder, final Categories model, final int position) {

                final String post_key = getRef(position).getKey();

                viewHolder.setImage(getApplicationContext(), model.getImage());
                viewHolder.setItemName(model.getName());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DataHolder.getInstance().setCategory(post_key);
                        Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                        startActivity(intent);
                    }
                });
            }

        };

        firebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mRecyclerView.smoothScrollToPosition(firebaseRecyclerAdapter.getItemCount());

            }
        });

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    public static class PostViewHolder extends RecyclerView.ViewHolder{

        View mView;
        ImageView mItemImage;
        TextView mCategoryTxt;

        public PostViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            mCategoryTxt = (TextView) mView.findViewById(R.id.categoryTxt);
            mItemImage = (ImageView) mView.findViewById(R.id.itemImage);

        }

        public void setImage(final Context ctx, final String url){

            Picasso.with(ctx).load(url).networkPolicy(NetworkPolicy.OFFLINE).resize(100,100).centerCrop().into(mItemImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ctx).load(url).resize(100,100).centerCrop().into(mItemImage);
                }
            });
        }

        public void setItemName(String itemName){
            mCategoryTxt.setText(itemName);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mRecyclerView.setHasFixedSize(false);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        linearLayoutManager.setStackFromEnd(false);
        linearLayoutManager.setReverseLayout(false);

        gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setReverseLayout(true);

        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        finish();
    }
}
