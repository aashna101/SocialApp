package com.example.pavilion.socialapp;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class frndRequestAdapter extends RecyclerView.Adapter<frndRequestAdapter.userAdapter> {

   public ArrayList<userItem> arrayList;
   public Context context;

   frndRequestAdapter(ArrayList<userItem> array, Context context){
        this.context=context;
        this.arrayList=array;
    }

    @NonNull
    @Override
    public userAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.frnd_recycler_item,parent,false);
        return new userAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull userAdapter holder, int position) {

final TextView tt=holder.mText;
final TextView phoneView=holder.mEmailID;

        tt.setText(arrayList.get(position).getDisplay_name());
        holder.mStatus.setText(arrayList.get(position).getStatus());
        phoneView.setText(arrayList.get(position).getPhone());
        String img2=arrayList.get(position).getBack_pic();
        if ("noImage".equals(img2)){

            GlideApp.with(context)
                    .load(R.mipmap.ic_launcher)
                    .thumbnail(0.2f)
                     .apply(RequestOptions.fitCenterTransform())
                     .circleCrop()
                     .diskCacheStrategy(DiskCacheStrategy.NONE)
                     .skipMemoryCache(true)
                     .into(holder.myImage);
        }else{
        GlideApp.with(context)
                .asBitmap()
                .load(Uri.parse(img2))
                .thumbnail(0.1f)
                .apply(RequestOptions.fitCenterTransform())
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.drawable.logo4)
                .into(holder.myImage);}

        holder.mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, ""+tt.getText().toString(), Toast.LENGTH_SHORT).show(); // call profile intent and pass node to fetch all the user
                // yet to complete... friend request
                if ( ! phoneView.getText().toString().isEmpty()) {
                    Intent intent=new Intent(context, profile.class);
                    intent.putExtra("checkStatus",phoneView.getText().toString());
                    Log.d("showing profile", "<<<<<<<<<<<<<<<<<<< : "+ phoneView.getText().toString());
                    context.startActivity(intent);}
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class userAdapter extends RecyclerView.ViewHolder {

        ImageView myImage;
        TextView mText, mStatus, mEmailID;
        Button mBtn;


        public userAdapter(View itemView) {
            super(itemView);
           this. myImage = itemView.findViewById(R.id.fetchImage);
            this.mText = itemView.findViewById(R.id.fetchName);
            this.mStatus = itemView.findViewById(R.id.fetchStatus);  //yet to set the next one also...
            this.mEmailID = itemView.findViewById(R.id.fetchEmailID);

            this.mBtn = itemView.findViewById(R.id.viewProfileBtn);

        }

    }
}


