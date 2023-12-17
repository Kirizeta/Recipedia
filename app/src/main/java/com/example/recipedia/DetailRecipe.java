package com.example.recipedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailRecipe extends AppCompatActivity {

    TextView detailName;
    TextView detailIngredient;
    TextView detailTime;
    TextView detailServe;
    TextView detailCalories;
    TextView detailDirection;
    TextView detailUser;
    TextView detailRating;
    YouTubePlayerView detailVideo;
    ImageView detailPhoto, bookmark;
    ImageView back;
    Button button;
    FirebaseAuth auth;
    String name, user;
    private List<DataClass> dataList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recipe);

        back = findViewById(R.id.recipe_back);

        detailName = findViewById(R.id.detail_name);
        detailIngredient = findViewById(R.id.detail_ingredient);
        detailTime = findViewById(R.id.detail_time);
        detailPhoto = findViewById(R.id.detail_photo);
        detailServe = findViewById(R.id.detail_serving);
        detailCalories = findViewById(R.id.detail_calories);
        detailVideo = findViewById(R.id.detail_video);
        detailDirection = findViewById(R.id.detail_direction);
        detailUser = findViewById(R.id.detail_user);
        button = findViewById(R.id.detail_comment);
        detailRating = findViewById(R.id.detail_rating);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            detailName.setText(bundle.getString("Name"));
            detailIngredient.setText(bundle.getString("Ingredient"));
            detailTime.setText(bundle.getString("Time")+" Min");
            detailServe.setText(bundle.getString("Serving"));
            detailCalories.setText(bundle.getString("Calories")+" Calories");
            detailDirection.setText(bundle.getString("Direction"));
            detailUser.setText(bundle.getString("User"));
            detailRating.setText(bundle.getString("Rating")+"");

            detailVideo.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    String videoId = bundle.getString("Video");
                    youTubePlayer.loadVideo(videoId, 0);

                }
            });

            Glide.with(this).load(bundle.getString("Image")).into(detailPhoto);
        }

        if (detailIngredient.getText().toString().contains(".. ")){
            detailIngredient.setText(detailIngredient.getText().toString().replace(".. ", "\n"));
        }

        if (detailDirection.getText().toString().contains(".. ")){
            detailDirection.setText(detailDirection.getText().toString().replace(".. ", "\n"));
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailRecipe.this, ReviewRecipe.class);
                intent.putExtra("Name", bundle.getString("Name"));
                intent.putExtra("Image", bundle.getString("Image"));
                intent.putExtra("User",bundle.getString("User"));
                intent.putExtra("Category", bundle.getString("Category"));
                intent.putExtra("Rating",bundle.getString("Rating"));

                startActivity(intent);

            }
        });

    }




}