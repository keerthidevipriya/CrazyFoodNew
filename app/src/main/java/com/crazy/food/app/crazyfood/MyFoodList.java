package com.crazy.food.app.crazyfood;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MyFoodList extends RecyclerView.Adapter<MyFoodList.MyFoodView> {
    Context c;
    ArrayList<MyUserMeal> jsonMealsImages;
    ArrayList<String> jsonMealDetails = new ArrayList<String>();
    public MyFoodList(MainActivity mainActivity, ArrayList<MyUserMeal> jsonFoodImages) {
        this.c=mainActivity;
        this.jsonMealsImages=jsonFoodImages;
    }

    @Override
    public MyFoodList.MyFoodView onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.activity_image, parent, false);
        return new MyFoodView(v);
    }

    @Override
    public void onBindViewHolder(MyFoodList.MyFoodView holder, int position) {
        Picasso.with(c).load(jsonMealsImages.get(position).getStrMealThumb()).placeholder(R.mipmap.ic_launcher_round).into(holder.imageView);
        holder.textView.setText(jsonMealsImages.get(position).getStrMeal());
    }

    @Override
    public int getItemCount() {
        return jsonMealsImages.size();
    }

    public class MyFoodView extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public MyFoodView(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.id_first_image);
            textView=itemView.findViewById(R.id.id_first_name);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = getLayoutPosition();
                        jsonMealDetails.add(0, jsonMealsImages.get(pos).getStrMealThumb());
                        jsonMealDetails.add(1, jsonMealsImages.get(pos).getStrMeal());
                        jsonMealDetails.add(2, jsonMealsImages.get(pos).getIdMeal());
                        Intent i = new Intent(c, SecondFoodDetails.class);
                        i.putStringArrayListExtra("jTitle", jsonMealDetails);
                        v.getContext().startActivity(i);
                    }
                });

        }
    }
}
