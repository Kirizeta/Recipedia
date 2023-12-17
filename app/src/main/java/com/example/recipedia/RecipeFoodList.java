package com.example.recipedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RecipeFoodList extends AppCompatActivity {

    RecyclerView recyclerView, recyclerViewP;
    DatabaseReference database, databaseP;
    Adapter adapter;
    AdapterPremium adapterP;
    List<DataClass> dataList ;
    List<DataClassPremium> dataListP;
    ValueEventListener eventListener, eventListenerP;
    SearchView searchView;
    ImageView back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_food_list);
        recyclerViewP = findViewById(R.id.premium_recipe);

        recyclerView = findViewById(R.id.recipeuserlist);
        searchView = findViewById(R.id.search_main_recipe);
        searchView.clearFocus();

        back = findViewById(R.id.recipe_back);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(RecipeFoodList.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(RecipeFoodList.this);
        builder.setCancelable(false);
        builder.setView(R.layout.seaching_progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        dataList = new ArrayList<>();

        adapter = new Adapter(RecipeFoodList.this, dataList);

        recyclerView.setAdapter(adapter);

        database = FirebaseDatabase.getInstance().getReference("Recipe");

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
                                if(shot.exists()) {
                                    for(DataSnapshot shotting : shot.getChildren()) {
                                        DataClass dataClass = shotting.getValue(DataClass.class);
                                        dataList.add(dataClass);
                                        Collections.shuffle(dataList);
                                    }
                                }
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

        recyclerViewP.setLayoutManager(new LinearLayoutManager(RecipeFoodList.this, LinearLayoutManager.HORIZONTAL,false));

        dataListP = new ArrayList<>();
        adapterP = new AdapterPremium(RecipeFoodList.this, dataListP);

        recyclerViewP.setAdapter(adapterP);

        databaseP = FirebaseDatabase.getInstance().getReference("Premium Recipe");
        eventListenerP = databaseP.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataListP.clear();
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot :snapshot.getChildren()){
                        if(dataSnapshot.exists()){
                            for (DataSnapshot shot : dataSnapshot.getChildren()){
                                DataClassPremium dataClassPremium = shot.getValue(DataClassPremium.class);
                                dataListP.add(dataClassPremium);
                            }
                        }
                    }
                }
                adapterP.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }

        });

    }


    public String searchList(String text) {
        ArrayList<DataClass> searchList = new ArrayList<>();

        for (DataClass dataClass : dataList) {
            if (dataClass.getRecipe_name().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(dataClass);
            }
        }
        adapter.searchDataList(searchList);

        return null;
    }
}

