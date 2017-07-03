package com.synerit.freshboxclient.app.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.synerit.freshboxclient.R;

public class CartActivity extends AppCompatActivity {

    private LinearLayoutManager linearLayoutManager = null;

    private RecyclerView mRecyclerView;

    private DatabaseReference mSellerData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mSellerData = FirebaseDatabase.getInstance().getReference().child("Sellers");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRecyclerView.setHasFixedSize(false);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        linearLayoutManager.setStackFromEnd(false);
        linearLayoutManager.setReverseLayout(false);

        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
