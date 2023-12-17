package com.example.recipedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ReviewRecipe extends AppCompatActivity {

    ImageView backButton;
    ImageView reviewPhoto, detailPhoto;
    TextView reviewName, reviewStar, star;
    RatingBar ratingBar;
    EditText reviewBox;
    Button postButton;
    TextView reviewUser;
    float ratingValue, ratingSum;
    DatabaseReference reference;
    DetailRecipe detailRecipe;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_recipe);

        reference = FirebaseDatabase.getInstance().getReference("Recipe");

        backButton = findViewById(R.id.review_back);
        reviewPhoto = findViewById(R.id.review_photo);
        reviewName = findViewById(R.id.review_name);
        reviewUser = findViewById(R.id.review_user);
        ratingBar = findViewById(R.id.review_rating);
        reviewBox = findViewById(R.id.review_comment);
        postButton = findViewById(R.id.review_button);
        reviewStar = findViewById(R.id.review_star);
        star = findViewById(R.id.star);




        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            reviewName.setText(bundle.getString("Name"));
            reviewUser.setText(bundle.getString("User"));
            star.setText(bundle.getString("Rating"));
            Glide.with(this).load(bundle.getString("Image")).into(reviewPhoto);
        }


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratingValue = ratingBar.getRating();
                reviewStar.setText(""+ratingValue);

            }
        });


        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


}