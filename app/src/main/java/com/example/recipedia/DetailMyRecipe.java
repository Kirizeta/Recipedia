package com.example.recipedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.List;


public class DetailMyRecipe extends AppCompatActivity {

    TextView detailName;
    TextView detailIngredient;
    TextView detailTime;
    TextView detailServe;
    TextView detailCalories;
    TextView detailDirection;
    TextView detailUser;
    TextView detailRating, detailVideo1, detailCategory, detailUid;
    YouTubePlayerView detailVideo;
    ImageView detailPhoto, bookmark;
    ImageView back;
    Button button, edit_recipe;
    FirebaseAuth auth;
    String name, user;
    private List<DataClass> dataList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_my_recipe);

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
        edit_recipe = findViewById(R.id.edit_recipe);
        detailVideo1 = findViewById(R.id.detail_video1);
        detailCategory = findViewById(R.id.detail_category);
        detailUid = findViewById(R.id.detail_uid);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            detailName.setText(bundle.getString("Name"));
            detailIngredient.setText(bundle.getString("Ingredient"));
            detailTime.setText(bundle.getString("Time")+" Min");
            detailServe.setText(bundle.getString("Serving"));
            detailCalories.setText(bundle.getString("Calories")+" Calories");
            detailDirection.setText(bundle.getString("Direction"));
            detailUser.setText(bundle.getString("User"));
            detailCategory.setText(bundle.getString("Category"));
            detailVideo1.setText(bundle.getString("Video"));
            detailUid.setText(bundle.getString("Uid"));
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

        edit_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailMyRecipe.this, EditRecipe.class);
                intent.putExtra("Name",detailName.getText().toString());
                intent.putExtra("Ingredient",detailIngredient.getText().toString());
                intent.putExtra("Direction",detailDirection.getText().toString());
                intent.putExtra("Time", detailTime.getText().toString());
                intent.putExtra("Serving", detailServe.getText().toString());
                intent.putExtra("Calories", detailCalories.getText().toString());
                intent.putExtra("Video", detailVideo1.getText().toString());
                intent.putExtra("Category", detailCategory.getText().toString());
                intent.putExtra("Image", bundle.getString("Image"));
                intent.putExtra("Uid",detailUid.getText().toString());
                startActivity(intent);
            }
        });

    }
}