package com.example.recipedia;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.widget.SearchView;

import java.util.List;


public class SearchRecipe extends Fragment {
    private SearchView searchview;
    private LinearLayout meat;
    private LinearLayout noodle;
    private LinearLayout vege;
    private LinearLayout soup;
    private LinearLayout seafood;
    private LinearLayout bakery;
    private LinearLayout lowcarb;
    private LinearLayout diabetic;
    private LinearLayout lowfat;
    private LinearLayout juices;
    private LinearLayout boba;
    private LinearLayout coffee;
    private List<DataClass> dataList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        meat = view.findViewById(R.id.meat_label);
        noodle = view.findViewById(R.id.nood_label);
        searchview = view.findViewById(R.id.search_main);
        vege = view.findViewById(R.id.vege_label);
        soup = view.findViewById(R.id.soup_label);
        seafood = view.findViewById(R.id.seafood_label);
        bakery = view.findViewById(R.id.bakery_label);
        lowcarb = view.findViewById(R.id.lowcarb_label);
        diabetic = view.findViewById(R.id.diabetic_label);
        lowfat = view.findViewById(R.id.lowfat_label);
        juices = view.findViewById(R.id.juices_label);
        boba = view.findViewById(R.id.boba_label);
        coffee = view.findViewById(R.id.coffee_label);


        meat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CategoryFoodList.class);
                intent.putExtra("Category", "Meat");
                startActivity(intent);
            }
        }
        );

        noodle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CategoryFoodList.class);
                intent.putExtra("Category", "Noodle");
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

        diabetic.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          Intent intent = new Intent(getActivity(), CategoryFoodList.class);
                                          intent.putExtra("Category", "Noodle");
                                          startActivity(intent);
                                      }
                                  }
        );

        lowfat.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          Intent intent = new Intent(getActivity(), CategoryFoodList.class);
                                          intent.putExtra("Category", "Low fat");
                                          startActivity(intent);
                                      }
                                  }
        );

        boba.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          Intent intent = new Intent(getActivity(), CategoryFoodList.class);
                                          intent.putExtra("Category", "Boba");
                                          startActivity(intent);
                                      }
                                  }
        );

        juices.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          Intent intent = new Intent(getActivity(), CategoryFoodList.class);
                                          intent.putExtra("Category", "Juices");
                                          startActivity(intent);
                                      }
                                  }
        );

        coffee.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          Intent intent = new Intent(getActivity(), CategoryFoodList.class);
                                          intent.putExtra("Category", "Coffee");
                                          startActivity(intent);
                                      }
                                  }
        );

        searchview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RecipeFoodList.class);
                startActivity(intent);
            }
        });



        return view;
    }
}