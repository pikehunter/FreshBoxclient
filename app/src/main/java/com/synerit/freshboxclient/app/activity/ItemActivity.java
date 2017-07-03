package com.synerit.freshboxclient.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.synerit.freshboxclient.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ItemActivity extends AppCompatActivity {

    private TextView mItemCountTxt,
                     mTitleTxt,
                     mItemDesc,
                     mLikesTxt,
                     mItemCostTxt,
                     mShipingTxt,
                     mItemIcon,
                     mSuccessTxt;

    private ImageView mItemImage,
                      mItemAddBtn,
                      mItemRemoveBtn,
                      mCartBtn;

    private CircleImageView mSellerImage;

    private Button mAddBtn, mSellerBtn, mCartBtn2;

    private Toolbar toolbar;

    private LinearLayout mOrderGroup;

    private Context ctx;

    private DatabaseReference mSellerData,
                              mData,
                              mCartData;

    private long cost = 0;

    private int count = 1;

    private long itemCount = 0;

    private String sellerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ctx = this;

        initViews();

        sellerId = getIntent().getStringExtra("seller_id");

        mTitleTxt.setText(getIntent().getStringExtra("item_name"));
        mItemDesc.setText(getIntent().getStringExtra("item_desc"));

        cost = getIntent().getLongExtra("item_cost",0);
        mItemCostTxt.setText("" + cost + " руб.");

        mData = FirebaseDatabase.getInstance().getReference().child("Goods").child(getIntent().getStringExtra("item_id"));
        mData.keepSynced(true);

        mSellerData = FirebaseDatabase.getInstance().getReference().child("Sellers");
        mSellerData.keepSynced(true);

        mCartData = FirebaseDatabase.getInstance().getReference().child("UserData").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Cart");
        mCartData.keepSynced(true);

        Picasso.with(ctx).load(getIntent().getStringExtra("image_url")).networkPolicy(NetworkPolicy.OFFLINE).into(mItemImage, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(ctx).load(getIntent().getStringExtra("image_url")).into(mItemImage);
            }
        });

        initLikes();
        initSeller();
        initCart();

        mItemAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = count + 1;
                mItemCountTxt.setText("" + count);
                mItemCostTxt.setText("" + cost * count + " руб.");
            }
        });

        mItemRemoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count>1) {
                    count = count - 1;
                    mItemCountTxt.setText("" + count);
                    mItemCostTxt.setText("" + cost * count + " руб.");
                }
            }
        });

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               addToCart();
            }
        });

        mCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        mCartBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initViews(){
        mItemCountTxt = (TextView) findViewById(R.id.itemCountTxt);
        mItemImage = (ImageView) findViewById(R.id.itemImage);
        mTitleTxt = (TextView) findViewById(R.id.titleTxt);
        mSellerImage = (CircleImageView) findViewById(R.id.sellerImage);
        mItemDesc = (TextView) findViewById(R.id.itemDesc);
        mLikesTxt = (TextView) findViewById(R.id.likesTxt);
        mItemCostTxt = (TextView) findViewById(R.id.itemCostTxt);
        mItemAddBtn = (ImageView) findViewById(R.id.itemAddBtn);
        mItemRemoveBtn = (ImageView) findViewById(R.id.itemRemoveBtn);
        mShipingTxt = (TextView) findViewById(R.id.shipingTxt);
        mAddBtn = (Button) findViewById(R.id.addBtn);
        mItemIcon = (TextView) findViewById(R.id.itemIcon);
        mCartBtn = (ImageView) findViewById(R.id.cartBtn);
        mOrderGroup = (LinearLayout) findViewById(R.id.orderGroup);
        mSellerBtn = (Button) findViewById(R.id.sellerBtn);
        mCartBtn2 = (Button) findViewById(R.id.cartBtn2);
        mSuccessTxt = (TextView) findViewById(R.id.successTxt);
    }

    private void initCart(){

        mCartData.orderByChild("seller_id").equalTo(sellerId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()){

                    itemCount = itemCount + dataSnapshot.child("item_count").getValue(Long.class);
                    mItemIcon.setText("" + itemCount);
                    mItemIcon.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                        itemCount = itemCount - dataSnapshot.child("item_count").getValue(Long.class);
                        mItemIcon.setText("" + itemCount);

                        if (itemCount==0) {
                            mItemIcon.setText("");
                            mItemIcon.setVisibility(View.INVISIBLE);
                        }

                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void addToCart() {

        DatabaseReference mRef = mCartData.push();

        mRef.child("item_count").setValue(count);
        mRef.child("seller_id").setValue(getIntent().getStringExtra("seller_id"));
        mRef.child("item_id").setValue(getIntent().getStringExtra("item_id")).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mOrderGroup.setVisibility(View.GONE);
                mItemDesc.setVisibility(View.GONE);
                mTitleTxt.setVisibility(View.GONE);
                mSellerBtn.setVisibility(View.VISIBLE);
                mCartBtn2.setVisibility(View.VISIBLE);
                mSuccessTxt.setVisibility(View.VISIBLE);
            }
        });
    }


    private void initLikes() {

        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mLikesTxt.setText(dataSnapshot.child("likes").getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void initSeller(){
        mSellerData.child(getIntent().getStringExtra("seller_id")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Picasso.with(ctx).load(dataSnapshot.child("seller_image").getValue().toString()).networkPolicy(NetworkPolicy.OFFLINE).resize(100,100).into(mSellerImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(ctx).load(dataSnapshot.child("seller_image").getValue().toString()).resize(100,100).into(mSellerImage);
                        }
                    });

                    toolbar.setTitle(dataSnapshot.child("seller_name").getValue().toString());
                    mShipingTxt.setText(dataSnapshot.child("shiping_terms").getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        finish();
    }
}
