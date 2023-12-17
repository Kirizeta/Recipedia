package com.example.recipedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PremiumRecipeList extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    Adapter adapter;
    List<DataClass> dataList;
    ValueEventListener eventListener;
    SearchView searchView;
    ImageView back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_food_list);

        recyclerView = findViewById(R.id.premium_recipe);

        back = findViewById(R.id.recipe_back);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(PremiumRecipeList.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(PremiumRecipeList.this);
        builder.setCancelable(false);
        builder.setView(R.layout.seaching_progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        dataList = new ArrayList<>();

        adapter = new Adapter(PremiumRecipeList.this, dataList);
        recyclerView.setAdapter(adapter);

        database = FirebaseDatabase.getInstance().getReference("Premium Recipe");
        dialog.show();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        eventListener = database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot :snapshot.getChildren()){
                        if(dataSnapshot.exists()){
                            for(DataSnapshot shot : dataSnapshot.getChildren()){
                                DataClass dataClass = shot.getValue(DataClass.class);
                                dataList.add(dataClass);
                                Collections.shuffle(dataList);
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });


    }

}

