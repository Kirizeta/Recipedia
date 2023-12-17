package com.example.recipedia;

import static com.example.recipedia.R.id.food_image;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private Context context;
    private List<DataClass> dataList;

    public Adapter(Context context, List<DataClass> datalist) {
        this.context = context;
        this.dataList = datalist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(dataList.get(position).getRecipe_photo()).into(holder.foodImage);
        holder.foodName.setText(dataList.get(position).getRecipe_name());
        holder.foodIngredients.setText(dataList.get(position).getRecipe_ingredient());
        holder.foodTime.setText(dataList.get(position).getRecipe_time()+" Min");
        holder.foodServ.setText(dataList.get(position).getRecipe_serving());
        holder.foodCalories.setText(dataList.get(position).getRecipe_calories()+ "Calorie");
        holder.foodVideo.setText(dataList.get(position).getRecipe_video());
        holder.foodCategory.setText(dataList.get(position).getRecipe_category());
        holder.foodDirection.setText(dataList.get(position).getRecipe_direction());
        holder.foodUser.setText(dataList.get(position).getRecipe_user());
        holder.foodRating.setText(dataList.get(position).getRecipe_rating()+"");
        holder.foodUid.setText(dataList.get(position).getRecipe_uid());
        holder.cardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailRecipe.class);
                intent.putExtra("Image", dataList.get(holder.getAdapterPosition()).getRecipe_photo());
                intent.putExtra("Name", dataList.get(holder.getAdapterPosition()).getRecipe_name());
                intent.putExtra("Ingredient", dataList.get(holder.getAdapterPosition()).getRecipe_ingredient());
                intent.putExtra("Time", dataList.get(holder.getAdapterPosition()).getRecipe_time());
                intent.putExtra("Serving", dataList.get(holder.getAdapterPosition()).getRecipe_serving());
                intent.putExtra("Calories", dataList.get(holder.getAdapterPosition()).getRecipe_calories());
                intent.putExtra("Video", dataList.get(holder.getAdapterPosition()).getRecipe_video());
                intent.putExtra("Category", dataList.get(holder.getAdapterPosition()).getRecipe_category());
                intent.putExtra("Direction", dataList.get(holder.getAdapterPosition()).getRecipe_direction());
                intent.putExtra("Rating", dataList.get(holder.getAdapterPosition()).getRecipe_rating()+"");
                intent.putExtra("Uid", dataList.get(holder.getAdapterPosition()).getRecipe_uid());
                intent.putExtra("User", dataList.get(holder.getAdapterPosition()).getRecipe_user());

                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(ArrayList<DataClass> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }

    public void checkDataList(ArrayList<DataClass> checkList) {
        dataList = checkList;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView foodName, foodIngredients, foodTime,foodRating, foodServe, foodUser, foodCalories, foodVideo, foodServ, foodCategory, foodDirection, foodUid;
        ImageView foodImage;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            foodImage = itemView.findViewById(food_image);
            foodName = itemView.findViewById(R.id.food_name);
            foodIngredients = itemView.findViewById(R.id.food_ingredient);
            foodTime = itemView.findViewById(R.id.food_time);
            cardView = itemView.findViewById(R.id.card_view);
            foodServ = itemView.findViewById(R.id.food_serve);
            foodCalories = itemView.findViewById(R.id.food_calories);
            foodVideo = itemView.findViewById(R.id.food_video);
            foodCategory = itemView.findViewById(R.id.food_category);
            foodDirection = itemView.findViewById(R.id.food_direction);
            foodUser = itemView.findViewById(R.id.food_user);
            foodRating = itemView.findViewById(R.id.food_rating);
            foodUid = itemView.findViewById(R.id.food_uid);

        }
    }

}
