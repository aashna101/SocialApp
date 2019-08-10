package com.example.pavilion.socialapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class profile extends AppCompatActivity {
LinearLayout hideLayout;
Button expandBtn, friendBtn;
private String btnText, uid;
TextView Name, Email, Phone, Status, toolbarName;
ImageView Pic, toolbarImage;
DatabaseReference mRef1, getPosts, reference,   CheckIfFriend, mDR;
FirebaseAuth firebaseAuth;
RecyclerView recyclerView;
RecyclerView.Adapter mAdapter;
Uri PicUri;
ArrayList<StatusView> list;
String getIntentData, idRef, friend_num,  phoneVal;
String compareDta="Load_my_data", frndBtnText="Friend Request Send(*click to cancel)", acceptReq="Accept Request", friend="Friend(*click to unfriend)", addFriend="+Add Friend+";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final Intent get_intent = getIntent();
        getIntentData = get_intent.getStringExtra("checkStatus");
        Log.d("getIntentData", "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< "+ getIntentData);
       // numRef = getIntentData;   // getting number passed by intent of a friend
        friendBtn = findViewById(R.id.addFriendBtn);
        Phone = findViewById(R.id.showPhone);



        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            uid = user.getUid();
            Log.d("REACHED TO UID", " reached to uid<<<<<<<<<<<<<<<<<< <<<<<<<<" + uid);
        } else {
            Intent intent = new Intent(profile.this, MainActivity.class);
            startActivity(intent);
        }
        mRef1 = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        btnText = "Expand";

        toolbarName = findViewById(R.id.getNAme);

        toolbarImage = findViewById(R.id.getImage);
        hideLayout = findViewById(R.id.hideDetails);
        expandBtn = findViewById(R.id.ExpandView);
        hideLayout.setVisibility(View.VISIBLE);


        expandBtn.setText(btnText);

        Name = findViewById(R.id.showName);
        Email = findViewById(R.id.showEmail);

        Status = findViewById(R.id.showStatus);
        recyclerView = findViewById(R.id.minePostsRecyclerView);

        Pic = findViewById(R.id.showPic);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(profile.this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        if (getIntentData.equals(compareDta)) {



            mRef1.child("phone").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    phoneVal=dataSnapshot.getValue(String.class);
                    Log.d("REACHED TO my phone", "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< <<<<<<<<" + phoneVal);


            friendBtn.setVisibility(View.GONE);
            Phone.setVisibility(View.VISIBLE);

        mRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                        Name.setText(dataSnapshot.child("display_name").getValue(String.class));

                        toolbarName.setText(dataSnapshot.child("display_name").getValue(String.class)); // toolbar name.
                        Phone.setText(dataSnapshot.child("phone").getValue(String.class));
                        Status.setText(dataSnapshot.child("status").getValue(String.class));
                        Email.setText(dataSnapshot.child("email_id").getValue(String.class));

                        PicUri =Uri.parse(dataSnapshot.child("back_pic").getValue(String.class));
                        Log.d("GET VALUES", " " + dataSnapshot.toString());

                        if(PicUri.toString().equals("noImage")){
                            GlideApp
                                    .with(profile.this)
                                    .load(R.drawable.logo4)
                                    .fitCenter()
                                    .error(R.mipmap.ic_launcher)
                                    .into(Pic);

                            GlideApp
                                    .with(profile.this)
                                    .load(R.drawable.logo4)
                                    .thumbnail(0.2f)
                                    .apply(RequestOptions.fitCenterTransform())
                                    .circleCrop()
                                    .placeholder(R.mipmap.ic_launcher)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .into(toolbarImage);

                        }else{
                        GlideApp
                                .with(getApplicationContext())
                                .load(PicUri)
                                .fitCenter()
                                .placeholder(R.drawable.logo4)
                                .error(R.mipmap.ic_launcher)
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                .into(Pic);

                        GlideApp
                                .with(profile.this)
                                .load(PicUri)
                                .thumbnail(0.1f)
                                .apply(RequestOptions.fitCenterTransform())
                                .circleCrop()
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                .placeholder(R.drawable.logo4)
                                .into(toolbarImage);}
                    }
                                         @Override
                                         public void onCancelled(DatabaseError databaseError) {} });





            getPosts = FirebaseDatabase.getInstance().getReference().child("posts").child(phoneVal).child("all_posts");
            getPosts.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    list = new ArrayList<>();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if ( uid.equals(ds.child("user_id").getValue(String.class))){
                             StatusView statusView = ds.getValue(StatusView.class);
                            list.add(statusView);}
                    }
                    mAdapter = new StatusAdapter(list, getApplicationContext());
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
                    }   else {

            mRef1.child("phone").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    phoneVal=dataSnapshot.getValue(String.class);
                    Log.d("REACHED TO my phone", "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< <<<<<<<<" + phoneVal);

                                 if (phoneVal != null){

      //----------------------------setting "friends or not ? " status on button-----------------------------------------------------------

                friendBtn.setVisibility(View.VISIBLE);
                Phone.setVisibility(View.GONE);

                CheckIfFriend= FirebaseDatabase.getInstance().getReference().child("posts").child(phoneVal).child("friends");// checking for friends status
                CheckIfFriend.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot != null){
                            for (DataSnapshot ds: dataSnapshot.getChildren()){
                                String num=ds.child("friend_number").getValue(String.class);
                                String sta=ds.child("request_status").getValue(String.class);
                                if (getIntentData.equals(num)){
                                    if ("REQUEST_SEND".equals(sta)){
                                        friendBtn.setBackgroundColor(ContextCompat.getColor(profile.this,R.color.green));
                                        friendBtn.setText(frndBtnText);

                                    }else if ("REQUEST_RECEIVED".equals(sta)){
                                        friendBtn.setBackgroundColor(ContextCompat.getColor(profile.this,R.color.yellow));
                                        friendBtn.setText(acceptReq);
                                    }else if ("REQUEST_ACCEPTED".equals(sta)){
                                        friendBtn.setBackgroundColor(ContextCompat.getColor(profile.this,R.color.blue));
                                        friendBtn.setText(friend);}
                                }} }

                        else {    friendBtn.setText(addFriend);
                            friendBtn.setBackgroundColor(ContextCompat.getColor(profile.this,R.color.clr));}}

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }); }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        //-----------------------------------------------------------------------------------------------------------------------------------------------
            // loading friend's profile
             reference = FirebaseDatabase.getInstance().getReference() .child("posts").child(getIntentData);

                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                idRef = dataSnapshot.child("user_id").getValue(String.class);
          Log.d("number passed", " number passed by intent "+ idRef+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

                                DatabaseReference profileDetailRef = FirebaseDatabase.getInstance().getReference().child("users").child(idRef);
                                profileDetailRef.addValueEventListener(new ValueEventListener() {
                                    @Override      // getting profile of friend
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Name.setText(dataSnapshot.child("display_name").getValue(String.class));
                                        toolbarName.setText(dataSnapshot.child("display_name").getValue(String.class)); // toolbar name.
                                        friend_num=dataSnapshot.child("phone").getValue(String.class);
                                        Status.setText(dataSnapshot.child("status").getValue(String.class));
                                        Email.setText(dataSnapshot.child("email_id").getValue(String.class));

                                        PicUri =Uri.parse(dataSnapshot.child("back_pic").getValue(String.class));

                                        Log.d("GET VALUES", " " + dataSnapshot.toString());

                                        if(PicUri.toString().equals("noImage")){
                                            GlideApp
                                                    .with(profile.this)
                                                    .load(R.drawable.logo4)
                                                    .centerCrop()
                                                    .error(R.mipmap.ic_launcher)
                                                    .into(Pic);
                                                                                    // if pic is not available
                                            GlideApp
                                                    .with(profile.this)
                                                    .load(R.drawable.logo4)
                                                    .thumbnail(0.1f)
                                                    .apply(RequestOptions.fitCenterTransform())
                                                    .centerCrop()
                                                    .placeholder(R.mipmap.ic_launcher)
                                                    .into(toolbarImage);

                                        }else{
                                        GlideApp
                                                .with(getApplicationContext())
                                                .asBitmap()
                                                .load(PicUri)
                                                .fitCenter()
                                                .placeholder(R.drawable.logo4)
                                                .error(R.mipmap.ic_launcher)
                                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                                .into(Pic);

                                        GlideApp
                                                .with(getApplicationContext())
                                                .asBitmap()
                                                .load(PicUri)
                                                .thumbnail(0.2f)
                                                .apply(RequestOptions.fitCenterTransform())
                                                .fitCenter()
                                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                                .placeholder(R.drawable.logo4)
                                                .into(toolbarImage);}
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                reference.child("all_posts").addValueEventListener(new ValueEventListener() { // all posts of friend
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        list = new ArrayList<>();

                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            if ( idRef.equals(ds.child("user_id").getValue(String.class))){
                                                StatusView statusView = ds.getValue(StatusView.class);
                                                list.add(statusView);}
                                        }
                                        mAdapter = new StatusAdapter(list, getApplicationContext());
                                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                                        recyclerView.setAdapter(mAdapter);

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                        Log.d("friends posts", "<<<<<<< "+ databaseError.toString()); }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

            mDR=  FirebaseDatabase.getInstance().getReference().child("posts").child(getIntentData);


        }



        expandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getBtnText;
                getBtnText = expandBtn.getText().toString();
                if (getBtnText.equals("Expand")) {
                    hideLayout.setVisibility(View.GONE);
                    btnText = "Shrink";
                    expandBtn.setText(btnText);
                } else if (getBtnText.equals("Shrink")) {
                    hideLayout.setVisibility(View.VISIBLE);
                    btnText = "Expand";
                    expandBtn.setText(btnText);
                }
                         }    });


        friendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btnText=friendBtn.getText().toString();
                if (btnText.equals(addFriend)){
                DatabaseReference addFrnReqRef= FirebaseDatabase.getInstance().getReference().child("posts").child(phoneVal).child("friends").push();

                addFrnReqRef.child("friend_number").setValue(getIntentData);
                addFrnReqRef.child("request_status").setValue("REQUEST_SEND");
                                                                               // updating friends node as well...
                DatabaseReference receiveFrnReqRef= FirebaseDatabase.getInstance().getReference().child("posts").child(getIntentData).child("friends").push();
                receiveFrnReqRef.child("friend_number").setValue(phoneVal);
                receiveFrnReqRef.child("request_status").setValue("REQUEST_RECEIVED");
                    friendBtn.setBackgroundColor(ContextCompat.getColor(profile.this,R.color.green));
                friendBtn.setText(frndBtnText);
                Toast.makeText(profile.this, "Friend request send!!!", Toast.LENGTH_SHORT).show(); }

                else if (btnText.equals(frndBtnText) || btnText.equals(friend)){
                    CheckIfFriend.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds: dataSnapshot.getChildren()){
                                if (getIntentData.equals(ds.child("friend_number").getValue(String.class))){
                                    CheckIfFriend.child(ds.getKey()).child("friend_number").setValue(null);
                                    CheckIfFriend.child(ds.getKey()).child("request_status").setValue(null);
                                    Log.d("DELETE THIS NODE", "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< "+ ds.getKey());} }

                                    mDR.child("friends").addValueEventListener(new ValueEventListener() {  // friend's node delete request-received
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot ds: dataSnapshot.getChildren()){
                                                if (phoneVal.equals(ds.child("friend_number").getValue(String.class))){
                                                    mDR.child("friends").child(ds.getKey()).child("friend_number").setValue(null);
                                                    mDR.child("friends").child(ds.getKey()).child("request_status").setValue(null);
                                                    Log.d("DELETE FRIEND NODE", "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< "+ ds.getKey());} }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                            friendBtn.setBackgroundColor(ContextCompat.getColor(profile.this,R.color.clr));
                                    friendBtn.setText(addFriend); }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }}); }
                else if (btnText.equals(acceptReq)){
                   // final DatabaseReference ReqRef= FirebaseDatabase.getInstance().getReference().child("posts").child(phoneVal).child("friends");

                    CheckIfFriend.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds: dataSnapshot.getChildren()){
                                if (getIntentData.equals(ds.child("friend_number").getValue(String.class))){
                                    String  ref=ds.getKey();
                                   CheckIfFriend.child(ref).child("request_status").setValue("REQUEST_ACCEPTED");
                                    Log.d("SET FRIEND VALUE", "AT THE REFERENCE AT<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"+ ref); } }

                           mDR.child("friends").addValueEventListener(new ValueEventListener() {
                               @Override
                               public void onDataChange(DataSnapshot dataSnapshot) {
                                   for (DataSnapshot ds: dataSnapshot.getChildren()){
                                       if (phoneVal.equals(ds.child("friend_number").getValue(String.class))){
                                           String  ref=ds.getKey();
                                           mDR.child("friends").child(ref).child("request_status").setValue("REQUEST_ACCEPTED");
                                           Log.d("SET FRIEND VALUE", "AT THE REFERENCE OF FRIEND<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"+ ref); } }
                                   friendBtn.setBackgroundColor(ContextCompat.getColor(profile.this,R.color.blue));
                                   friendBtn.setText(friend);
                                   Toast.makeText(profile.this, "YOU ARE NOW FRIENDS ", Toast.LENGTH_SHORT).show();
                               }

                               @Override
                               public void onCancelled(DatabaseError databaseError) {

                               }
                           });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    }
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        profile.this.finish();
        Intent intent=new Intent(profile.this,Main2Activity.class);
        startActivity(intent);
    }

}


