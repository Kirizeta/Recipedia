package com.example.recipedia;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.squareup.picasso.Picasso;


import java.util.List;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder> {
    List<DataClass> items;
    Context context;

    public FoodListAdapter(Context context, List<DataClass> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_food_list, parent, false);
        context = parent.getContext();
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.titleTxt.setText(items.get(position).getRecipe_name());
        holder.timeTxt.setText(items.get(position).getRecipe_time() + " min");
        holder.ScoreTxt.setText("" + items.get(position).getRecipe_rating());
//        Glide.with(context).load(items.get(position)).into(holder.pic);
        Picasso.get().load(items.get(position).getRecipe_photo()).centerCrop().fit().into(holder.pic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailRecipe.class);
                intent.putExtra("Image", items.get(holder.getAdapterPosition()).getRecipe_photo());
                intent.putExtra("Name", items.get(holder.getAdapterPosition()).getRecipe_name());
                intent.putExtra("Ingredient", items.get(holder.getAdapterPosition()).getRecipe_ingredient());
                intent.putExtra("Time", items.get(holder.getAdapterPosition()).getRecipe_time());
                intent.putExtra("Serving", items.get(holder.getAdapterPosition()).getRecipe_serving());
                intent.putExtra("Calories", items.get(holder.getAdapterPosition()).getRecipe_calories());
                intent.putExtra("Video", items.get(holder.getAdapterPosition()).getRecipe_video());
                intent.putExtra("Category", items.get(holder.getAdapterPosition()).getRecipe_category());
                intent.putExtra("Direction", items.get(holder.getAdapterPosition()).getRecipe_direction());
                intent.putExtra("User", items.get(holder.getAdapterPosition()).getRecipe_user());
                intent.putExtra("Rating", items.get(holder.getAdapterPosition()).getRecipe_rating());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTxt, timeTxt, ScoreTxt;
        ImageView pic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.food_name1);
            timeTxt = itemView.findViewById(R.id.food_time1);
            ScoreTxt = itemView.findViewById(R.id.food_rating1);
            pic = itemView.findViewById(R.id.food_image1);


        }
    }
}
