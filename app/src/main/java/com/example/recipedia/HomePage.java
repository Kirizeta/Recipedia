package com.example.recipedia;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codebyashish.autoimageslider.AutoImageSlider;
import com.codebyashish.autoimageslider.Enums.ImageScaleType;
import com.codebyashish.autoimageslider.ExceptionsClass;
import com.codebyashish.autoimageslider.Models.ImageSlidesModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class HomePage extends Fragment {
    private RecyclerView.Adapter adapterFoodList;
    private TextView textName, textView, textMember;
    private ImageView photo;
    private String names, photos, members;
    private EditText search;
    private FirebaseAuth auth;
    private LinearLayout meat, seafood, soup, vege, bakery, lowfat, lowcarb, more;
    private RecyclerView recyclerView;
    private DatabaseReference database;
    private List<DataClass> dataList;
    private ValueEventListener eventListener;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        meat = view.findViewById(R.id.meat_label);
        vege = view.findViewById(R.id.vege_label);
        soup = view.findViewById(R.id.soup_label);
        seafood = view.findViewById(R.id.seafood_label);
        bakery = view.findViewById(R.id.bakery_label);
        lowcarb = view.findViewById(R.id.lowcarb_label);
        textName = view.findViewById(R.id.name_home);
        lowfat = view.findViewById(R.id.lowfat_label);
        search = view.findViewById(R.id.searchHome);
        photo = view.findViewById(R.id.home_photo);
        textView = view.findViewById(R.id.view_all);
        textMember = view.findViewById(R.id.member_home);


        auth = FirebaseAuth.getInstance();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecipeFoodList.class);
                startActivity(intent);
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RecipeFoodList.class);
                startActivity(intent);
            }
        });

        String id = auth.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered Users");
        reference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readWriteUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
            if (readWriteUserDetails != null) {
                    names = readWriteUserDetails.name;
                    photos = readWriteUserDetails.photo;
                    members = readWriteUserDetails.member;

                    textName.setText(names + "...");
                    Glide.with(getContext()).load(photos).into(photo);
                    textMember.setText(members);

                if(textMember.getText().toString().contains("VIP")) {
                    LinearLayout.LayoutParams slider = new LinearLayout.LayoutParams(0, 0);
                    AutoImageSlider autoImageSlider = (AutoImageSlider) view.findViewById(R.id.autoImageSlider);
                    autoImageSlider.setLayoutParams(slider);

                }
                else {
                    AutoImageSlider autoImageSlider = view.findViewById(R.id.autoImageSlider);
                    ArrayList<ImageSlidesModel> autoImageList = new ArrayList<>();
                    try {
                        autoImageList.add(new ImageSlidesModel("https://broonet.com/wp-content/uploads/2020/04/10-contoh-iklan-produk-sabun-cuci-piring.jpg", ImageScaleType.FIT));
                        autoImageList.add(new ImageSlidesModel("https://retailasia.com/sites/default/files/styles/opengraph/public/2022-11/new-project-2.png?h=758679fc&itok=OlM9R4Fo", ImageScaleType.FIT));
                        autoImageList.add(new ImageSlidesModel("https://broonet.com/wp-content/uploads/2020/04/5-contoh-iklan-produk-minuman.jpg", ImageScaleType.FIT));
                        autoImageSlider.setImageList(autoImageList, ImageScaleType.FIT);

                        autoImageSlider.setDefaultAnimation();

                    } catch (ExceptionsClass e) {
                        throw new RuntimeException(e);
                    }

                }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        recyclerView = view.findViewById(R.id.recipeuserlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));

        dataList = new ArrayList<>();

        adapterFoodList = new FoodListAdapter(getActivity(), dataList);
        recyclerView.setAdapter(adapterFoodList);

        database = FirebaseDatabase.getInstance().getReference("Recipe");

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
                adapterFoodList.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        meat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CategoryFoodList.class);
                intent.putExtra("Category", "Meat");
                startActivity(intent);
            }
        }
        );

        vege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CategoryFoodList.class);
                intent.putExtra("Category", "Vegetarian");
                startActivity(intent);
            }
        }
        );

        soup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CategoryFoodList.class);
                intent.putExtra("Category", "Soup");
                startActivity(intent);
            }
        }
        );

        seafood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CategoryFoodList.class);
                intent.putExtra("Category", "Seafood");
                startActivity(intent);
            }
        }
        );

        bakery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CategoryFoodList.class);
                intent.putExtra("Category", "Bakery");
                startActivity(intent);
            }
        }
        );

        lowcarb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CategoryFoodList.class);
                intent.putExtra("Category", "LowCarb");
                startActivity(intent);
            }
        }
        );

        lowfat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CategoryFoodList.class);
                intent.putExtra("Category", "LowFat");
                startActivity(intent);
            }
        }
        );


        return view;
    }

}