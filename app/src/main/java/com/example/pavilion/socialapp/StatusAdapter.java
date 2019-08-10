package com.example.pavilion.socialapp;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StatusAdapter extends RecyclerView.Adapter{
    private ArrayList<StatusView> dataSet;
    private Context context;
    private String compare="IMAGE_POST";
    private String compare1="TEXT_POST";
    public ImageView img1, img0;

    public StatusAdapter(ArrayList<StatusView> data, Context context){
        this.dataSet = data;
        this.context = context;
    }


    @Override
    public int getItemViewType(int position) {
        String T=dataSet.get(position).tag;
        Log.d("GET  ITEM VIEW TYPE", "REACHED TO GETITEMVIEWTYPE>>>>>>>>>>>"+ T);
        if (compare.equals(T)){
            return 1;}
        else if (compare1.equals(T)){
             return 0; }
        return -1;}



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1){
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item_view,parent,false);
            return new imageRecyclerView(view);
        }
        else if (viewType == 0){

            view=LayoutInflater.from(parent.getContext()).inflate(R.layout.text_item_view,parent,false);
            return  new textRecyclerView(view);
        }else{
        return null;}
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final StatusView statusView = dataSet.get(position);
        String str = statusView.tag;

        String uid = statusView.getUser_id();
       // color = Integer.parseInt(statusView.background_color);

        if (uid != null) {
            if (compare.equals(str)) {   //image type view handling

                final DatabaseReference DPandName = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                DPandName.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String imgURL = dataSnapshot.child("back_pic").getValue(String.class);
                        String name = dataSnapshot.child("display_name").getValue(String.class);

                        ImageView img = ((imageRecyclerView) holder).imageView;
                        img0 = ((imageRecyclerView) holder).thumbnail1;

                        ((imageRecyclerView) holder).name.setText(name);
                        ((imageRecyclerView) holder).textView.setBackgroundColor( Integer.parseInt(statusView.background_color));
                        ((imageRecyclerView) holder).textView.setText(statusView.text);
                        ((imageRecyclerView) holder).day_date.setText(statusView.day_date_time);
                        GlideApp.with(context)
                                .load(statusView.image)
                                .placeholder(R.mipmap.ic_launcher)
                                .fitCenter()
                                .priority(Priority.HIGH)
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                .into(img);


                        if ("noImage".equals(imgURL)) {

                            GlideApp.with(context)
                                    .load(R.drawable.logo4)
                                    .thumbnail(0.2f)
                                    .apply(RequestOptions.fitCenterTransform())
                                    .circleCrop()
                                    .skipMemoryCache(true)
                                    .placeholder(R.drawable.logo4)

                                    .into(img0);
                        } else {
                            GlideApp.with(context)
                                    .load(Uri.parse(imgURL))
                                    .thumbnail(0.2f)
                                    .apply(RequestOptions.fitCenterTransform())
                                    .circleCrop()
                                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                    .skipMemoryCache(false)
                                    .placeholder(R.drawable.logo4)
                                    .priority(Priority.HIGH)
                                    .into(img0);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            } else if (compare1.equals(str)) {


                final DatabaseReference DPandName = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                DPandName.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String imgURL = dataSnapshot.child("back_pic").getValue(String.class);
                        String name = dataSnapshot.child("display_name").getValue(String.class);

                        img1 = ((textRecyclerView) holder).thumbnail2;

                        ((textRecyclerView) holder).name2.setText(name);
                        ((textRecyclerView) holder).status_txt.setBackgroundColor( Integer.parseInt(statusView.background_color));
                        ((textRecyclerView) holder).status_txt.setText(statusView.text);
                        ((textRecyclerView) holder).day_date1.setText(statusView.day_date_time);


                        if ("noImage".equals(imgURL)) {

                            GlideApp.with(context)
                                    .load(R.drawable.logo4)
                                    .thumbnail(0.2f)
                                    .apply(RequestOptions.fitCenterTransform())
                                    .circleCrop()
                                    .skipMemoryCache(true)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .placeholder(R.drawable.logo4)
                                    .into(img1);
                        } else {
                            GlideApp.with(context)
                                    .load(Uri.parse(imgURL))
                                    .thumbnail(0.2f)
                                    .apply(RequestOptions.fitCenterTransform())
                                    .circleCrop()
                                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                    .skipMemoryCache(false)
                                    .placeholder(R.drawable.logo4)
                                    .priority(Priority.HIGH)
                                    .into(img1);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}});
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    private class imageRecyclerView extends RecyclerView.ViewHolder{

        private ImageView imageView, thumbnail1;
        private TextView textView, name, day_date;

        private imageRecyclerView(View itemView) {
            super(itemView);
           this.imageView=itemView.findViewById(R.id.imageStatus);
            this.textView=itemView.findViewById(R.id.image_desc);
            this.thumbnail1=itemView.findViewById(R.id.thumbnailPic1);
            this.name=itemView.findViewById(R.id.name_ImageItem);
            this.day_date=itemView.findViewById(R.id.day_date_ImageType);
          //  Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
        }

        }

    private class textRecyclerView extends RecyclerView.ViewHolder{

        private TextView status_txt, day_date1, name2;
        private ImageView thumbnail2;
        private textRecyclerView(View itemView) {
            super(itemView);
            this.status_txt=itemView.findViewById(R.id.status_txt);
            this.thumbnail2=itemView.findViewById(R.id.thumbnailPic2);
            this.name2=itemView.findViewById(R.id.name_TextItem);
            this.day_date1=itemView.findViewById(R.id.day_date_TextItem);
        }
    }
}
