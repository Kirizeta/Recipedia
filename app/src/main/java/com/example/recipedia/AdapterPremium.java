package com.example.recipedia;

import static androidx.core.content.ContextCompat.startActivity;
import static com.example.recipedia.R.id.access;
import static com.example.recipedia.R.id.food_image;
import static com.example.recipedia.R.id.food_imageP;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterPremium extends RecyclerView.Adapter<AdapterPremium.MyViewHolder> {

    private Context context;
    private List<DataClassPremium> dataListP;
    FirebaseAuth auth;
    String names, mobiles, dobs, emails, photos, members;

    public AdapterPremium(Context context, List<DataClassPremium> datalistP) {
        this.context = context;
        this.dataListP = datalistP;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_premium, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        auth = FirebaseAuth.getInstance();
        String id = auth.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered Users");
        reference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readWriteUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readWriteUserDetails != null) {
                    names = readWriteUserDetails.name;
                    mobiles = readWriteUserDetails.mobile;
                    dobs = readWriteUserDetails.dob;
                    emails = readWriteUserDetails.email;
                    photos = readWriteUserDetails.photo;
                    members = readWriteUserDetails.member;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Picasso.get().load(dataListP.get(position).getRecipe_photo()).into(holder.foodImage);
        holder.foodName.setText(dataListP.get(position).getRecipe_name());
        holder.foodIngredients.setText(dataListP.get(position).getRecipe_ingredient());
        holder.foodTime.setText(dataListP.get(position).getRecipe_time()+" Min");
        holder.foodServ.setText(dataListP.get(position).getRecipe_serving());
        holder.foodCalories.setText(dataListP.get(position).getRecipe_calories()+ "Calorie");
        holder.foodVideo.setText(dataListP.get(position).getRecipe_video());
        holder.foodCategory.setText(dataListP.get(position).getRecipe_category());
        holder.foodDirection.setText(dataListP.get(position).getRecipe_direction());
        holder.foodUser.setText(dataListP.get(position).getRecipe_user());
        holder.foodRating.setText(dataListP.get(position).getRecipe_rating()+"");
        holder.cardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(members.contains("VIP-Member")) {
                    Intent intent = new Intent(context, DetailRecipe.class);
                    intent.putExtra("Image", dataListP.get(holder.getAdapterPosition()).getRecipe_photo());
                    intent.putExtra("Name", dataListP.get(holder.getAdapterPosition()).getRecipe_name());
                    intent.putExtra("Ingredient", dataListP.get(holder.getAdapterPosition()).getRecipe_ingredient());
                    intent.putExtra("Time", dataListP.get(holder.getAdapterPosition()).getRecipe_time());
                    intent.putExtra("Serving", dataListP.get(holder.getAdapterPosition()).getRecipe_serving());
                    intent.putExtra("Calories", dataListP.get(holder.getAdapterPosition()).getRecipe_calories());
                    intent.putExtra("Video", dataListP.get(holder.getAdapterPosition()).getRecipe_video());
                    intent.putExtra("Category", dataListP.get(holder.getAdapterPosition()).getRecipe_category());
                    intent.putExtra("Direction", dataListP.get(holder.getAdapterPosition()).getRecipe_direction());
                    intent.putExtra("Rating", dataListP.get(holder.getAdapterPosition()).getRecipe_rating() + "");
                    intent.putExtra("User", dataListP.get(holder.getAdapterPosition()).getRecipe_user());
                    context.startActivity(intent);
                }
                else{
                    showMembership();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataListP.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView foodName, foodIngredients, foodTime,foodRating, foodUser, foodCalories, foodVideo, foodServ, foodCategory, foodDirection;
        ImageView foodImage;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            foodImage = itemView.findViewById(food_imageP);
            foodName = itemView.findViewById(R.id.food_nameP);
            foodIngredients = itemView.findViewById(R.id.food_ingredientP);
            foodTime = itemView.findViewById(R.id.food_timeP);
            cardView = itemView.findViewById(R.id.card_viewP);
            foodServ = itemView.findViewById(R.id.food_serveP);
            foodCalories = itemView.findViewById(R.id.food_caloriesP);
            foodVideo = itemView.findViewById(R.id.food_videoP);
            foodCategory = itemView.findViewById(R.id.food_categoryP);
            foodDirection = itemView.findViewById(R.id.food_directionP);
            foodUser = itemView.findViewById(R.id.food_userP);
            foodRating = itemView.findViewById(R.id.food_ratingP);

        }
    }
    private void showMembership(){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.membership);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.yellow_round);

        Button access = dialog.findViewById((R.id.access));
        Button cancel = dialog.findViewById((R.id.cancel));

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        access.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                members = "VIP-Member";
                ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(names, mobiles, dobs, emails, photos, members);
                FirebaseDatabase.getInstance().getReference("Registered Users").child(auth.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast toast = Toast.makeText(context, "Congratulation, Now you are VIP-Member of RECIPEDIA", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.dismiss();
            }
        });
        dialog.show();

    }

}
