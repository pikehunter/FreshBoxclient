package com.synerit.freshboxclient.app.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.synerit.freshboxclient.R;
import com.synerit.freshboxclient.app.DataHolder;
import com.synerit.freshboxclient.app.Goods;

public class CategoryActivity extends AppCompatActivity {

    private String category_id;

    private RecyclerView mRecyclerView;
    private ImageView mCartBtn;

    private GridLayoutManager gridLayoutManager = null;

    private FirebaseRecyclerAdapter<Goods, PostViewHolder> firebaseRecyclerAdapter;
    private DatabaseReference mDatabase, mSellerData;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mCartBtn = (ImageView) findViewById(R.id.cartBtn);

        category_id = DataHolder.getInstance().getCategory();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Goods");
        mDatabase.keepSynced(true);

        mSellerData = FirebaseDatabase.getInstance().getReference().child("Sellers");
        mSellerData.keepSynced(true);

        mCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        category_id = DataHolder.getInstance().getCategory();

        if (!category_id.isEmpty()) {
            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Categories").child(category_id);

            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        toolbar.setTitle(dataSnapshot.child("name").getValue().toString());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            mRecyclerView.setHasFixedSize(false);

            gridLayoutManager = new GridLayoutManager(this, 2);
            gridLayoutManager.setReverseLayout(false);

            mRecyclerView.setLayoutManager(gridLayoutManager);
        } else {
            finish();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Goods, PostViewHolder>(

                Goods.class,
                R.layout.item_box,
                PostViewHolder.class,
                mDatabase.orderByChild("category").equalTo(category_id)

        )

        {
            @Override
            protected void populateViewHolder(final PostViewHolder viewHolder, final Goods model, final int position) {

                final String post_key = getRef(position).getKey();
                final String image_url = model.getItem_image();
                final String item_desc = model.getItem_desc();

                viewHolder.setImage(getApplicationContext(), image_url);

                viewHolder.setItemDesc(item_desc);

                viewHolder.setItemCost(model.getItem_cost());

                viewHolder.setLikesCount(model.getLikes());


                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CategoryActivity.this, ItemActivity.class).
                        putExtra("category_id", category_id).
                        putExtra("item_id", post_key).
                        putExtra("item_name", model.getItem_title()).
                        putExtra("image_url", image_url).
                        putExtra("seller_id", model.getSeller_id()).
                        putExtra("item_desc", item_desc).
                        putExtra("item_cost", model.getItem_cost());
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
        ImageView mMainImageView;
        ImageView mImageView2;
        ImageView mLikes;
        TextView mItemDesc, mItemCost;
        TextView mLikesTxt;

        public PostViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            mLikesTxt = (TextView) mView.findViewById(R.id.likesTxt);
            mItemCost = (TextView) mView.findViewById(R.id.itemCost);
            mItemDesc = (TextView) mView.findViewById(R.id.itemDesc);
            mLikes = (ImageView) mView.findViewById(R.id.likes);
            mImageView2 = (ImageView) mView.findViewById(R.id.imageView2);
            mMainImageView = (ImageView) mView.findViewById(R.id.mainImageView);

        }

        public void setImage(final Context ctx, final String url){

            Picasso.with(ctx).load(url).networkPolicy(NetworkPolicy.OFFLINE).resize(300,300).centerCrop().into(mMainImageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ctx).load(url).resize(300,300).centerCrop().into(mMainImageView);
                }
            });
        }

        public void setItemDesc(String item_desc){
            mItemDesc.setText(item_desc);
        }

        public void setLikesCount(long likesCount){
            mLikesTxt.setText("" + likesCount);
        }

        public void setItemCost(long itemCost){
            mItemCost.setText("" + itemCost + " руб.");
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        finish();
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
    }
}
