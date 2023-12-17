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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyRecipe extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    String uid;
    AdapterMyRecipe adapter;
    List<DataClass> dataList;
    ValueEventListener eventListener;
    ImageView back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipe);

        recyclerView = findViewById(R.id.recipeuserlist);

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");

        back = findViewById(R.id.recipe_back);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MyRecipe.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(MyRecipe.this);
        builder.setCancelable(false);
        builder.setView(R.layout.seaching_progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        dataList = new ArrayList<>();

        adapter = new AdapterMyRecipe(MyRecipe.this, dataList);
        recyclerView.setAdapter(adapter);

        database = FirebaseDatabase.getInstance().getReference("Recipe");
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
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    for(DataSnapshot snapshot1 : itemSnapshot.getChildren()) {
                        DataClass dataClass = snapshot1.child(uid).getValue(DataClass.class);
                        if (dataClass != null) {
                            dataList.add(dataClass);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyRecipe.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}