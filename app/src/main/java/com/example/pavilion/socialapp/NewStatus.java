package com.example.pavilion.socialapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class NewStatus extends Fragment {

     DatabaseReference refPost, phoneRef, refToRefPost, refFriends;
    private ImageView status_image;
     Button postBtn, blk, red, green, sea, blue, purple, pink, yellow, orange, drkPink;
    private EditText postEdit;
    private Context context;
    private StorageReference displayImageStorageRef;
    DateFormat dt;
     FirebaseAuth firebaseAuth;
    private String post, phn , uid, date;
    int postColor;
    private  Uri img, uri;
    private ArrayList<String> friends;
    int i=0;
    ProgressDialog mDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        displayImageStorageRef= FirebaseStorage.getInstance().getReference();
        firebaseAuth= FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if( user != null){
        uid= user.getUid();}

        phoneRef= FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        phoneRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                phn=dataSnapshot.child("phone").getValue(String.class);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


           }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_new_status, container, false);
        postEdit=rootView.findViewById(R.id.statusTxt);
       status_image=rootView.findViewById(R.id.imgView);

       postBtn=rootView.findViewById(R.id.postStatus);
       blk=rootView.findViewById(R.id.black);
       red=rootView.findViewById(R.id.red);
       green=rootView.findViewById(R.id.green);
       sea=rootView.findViewById(R.id.sea);
       blue=rootView.findViewById(R.id.blue);
       purple=rootView.findViewById(R.id.purple);
       pink=rootView.findViewById(R.id.pink);
       yellow=rootView.findViewById(R.id.yellow);
       orange=rootView.findViewById(R.id.orange);
       drkPink=rootView.findViewById(R.id.drkPink);

       mDialog = new ProgressDialog(getContext());
        mDialog.setTitle("New Post");
        mDialog.setMessage("Updating your Post...");
        mDialog.setCancelable(false);
        mDialog.setIndeterminate(true);

       status_image.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               Intent gallery= new Intent(Intent.ACTION_PICK);
               gallery.setType("image/*");
               gallery.setAction(Intent.ACTION_GET_CONTENT);
               startActivityForResult(Intent.createChooser(gallery,"share picture"),1);
 }
       });
       blk.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               //int colourId=((ColorDrawable)blk.getBackground()).getColor();
               postEdit.setBackgroundColor(ContextCompat.getColor(context,R.color.black));


           }
       });
       red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               postEdit.setBackgroundColor(ContextCompat.getColor(context,R.color.red));
               // postEdit.setTextColor(Color.argb(255,34,151,9));

                 }
        });

        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postEdit.setBackgroundColor(ContextCompat.getColor(context,R.color.green));

                // postEdit.setBackgroundColor(ContextCompat.getColor(context,clr));

            }
        });
        sea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postEdit.setBackgroundColor(ContextCompat.getColor(context,R.color.sea));

            }
        });
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postEdit.setBackgroundColor(ContextCompat.getColor(context,R.color.blue));

            }
        });
        purple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postEdit.setBackgroundColor(ContextCompat.getColor(context,R.color.purple));

            }
        });
        pink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postEdit.setBackgroundColor(ContextCompat.getColor(context,R.color.pink));

            }
        });
        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postEdit.setBackgroundColor(ContextCompat.getColor(context,R.color.yellow));

            }
        });
        orange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postEdit.setBackgroundColor(ContextCompat.getColor(context,R.color.orange));

            }
        });
        drkPink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postEdit.setBackgroundColor(ContextCompat.getColor(context,R.color.drkPink));

            }
        });

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDialog.show();
                refPost = FirebaseDatabase.getInstance().getReference().child("posts").child(phn);  //getting post node...
                refFriends=refPost.child("friends");   // setting friend node
                friends=new ArrayList<>();

                post=postEdit.getText().toString().trim();

                ColorDrawable cd=(ColorDrawable)postEdit.getBackground();
                postColor=cd.getColor();

                postBtn.setBackgroundColor(postColor);

                dt=new SimpleDateFormat("EEE, dd.MM.yyyy, HH:MM");
                date=dt.format(Calendar.getInstance().getTime());    // getting day date and time format
                if(img != null) {

                    StorageReference imgRef = displayImageStorageRef.child("PostsImages").child(img.getLastPathSegment());
                    imgRef.putFile(img).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                             uri = taskSnapshot.getDownloadUrl();
                            if (uri != null) {
                                 refToRefPost = refPost.child("all_posts").push();

                                refToRefPost.child("image").setValue(uri.toString());
                                refToRefPost.child("text").setValue(post);
                                refToRefPost.child("background_color").setValue(String.valueOf(postColor));
                                refToRefPost.child("user_id").setValue(uid);
                                // setting day date time...

                                refToRefPost.child("day_date_time").setValue(date);

                                refToRefPost.child("tag").setValue("IMAGE_POST");           //to identify either post is consisting image or not.

                                // posting on friend's database ..........................................................
                                refFriends.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot != null){
                                            for (DataSnapshot ds: dataSnapshot.getChildren()){
                                                String phnPost=ds.child("friend_number").getValue(String.class);
                                                String agree=ds.child("request_status").getValue(String.class);
                                                Log.d("GET_NODE", "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< "+ phnPost);

                                                if ("REQUEST_ACCEPTED".equals(agree)){
                                                    friends.add(phnPost);
                                                    Log.d("saved in array", "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< "+ friends);

                                                }  }
                                            Log.d("size of array", "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< "+ friends.size());
                                            for (i=0; i < friends.size(); i++) {
                                                DatabaseReference updateFriendPost = FirebaseDatabase.getInstance().getReference()
                                                        .child("posts").child(friends.get(i)).child("all_posts").push();
                                                updateFriendPost.child("image").setValue(uri.toString());
                                                updateFriendPost.child("text").setValue(post);
                                                updateFriendPost.child("background_color").setValue(String.valueOf(postColor));
                                                updateFriendPost.child("user_id").setValue(uid);
                                                updateFriendPost.child("day_date_time").setValue(date);
                                                updateFriendPost.child("tag").setValue("IMAGE_POST");
                                                Toast.makeText(getContext(), " post updated... :) ", Toast.LENGTH_LONG).show();
                                            }

                                        }else{
                                            Toast.makeText(getContext(),"YOU DO NOT HAVE ANY FRIENDS YET!!!",Toast.LENGTH_LONG).show();
                                        }
                                        mDialog.dismiss();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        mDialog.dismiss();

                                    }
                                }) ;

                                     }
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress= (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            mDialog.setMessage("uploaded "+(int)progress+"%");
                            Log.d("progress****", "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"+ progress+"%");
                        }
                    });
                }else
                    {

                    refToRefPost = refPost.child("all_posts").push();

                    refToRefPost.child("image").setValue("no_image");
                    refToRefPost.child("text").setValue(post);
                    refToRefPost.child("background_color").setValue(String.valueOf(postColor));
                    refToRefPost.child("user_id").setValue(uid);
                    // setting day date time...
                    refToRefPost.child("day_date_time").setValue(date);
                    refToRefPost.child("tag").setValue("TEXT_POST");          //to identify either post is consisting image or not.

                    // posting on friend's database ..........................................................
                    refFriends.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null){
                                for (DataSnapshot ds: dataSnapshot.getChildren()){
                                    String phnPost=ds.child("friend_number").getValue(String.class);
                                    String agree=ds.child("request_status").getValue(String.class);
                                    Log.d("GET_NODE", "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< "+ phnPost);

                                        if ("REQUEST_ACCEPTED".equals(agree)){
                                            friends.add(phnPost);
                                            Log.d("saved in array", "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< "+ friends);

                                        }  }
                                Log.d("size of array", "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< "+ friends.size());
                                        for (i=0; i < friends.size(); i++) {
                                      DatabaseReference updateFriendPost = FirebaseDatabase.getInstance().getReference()
                                                    .child("posts").child(friends.get(i)).child("all_posts").push();
                                            updateFriendPost.child("image").setValue("no_image");
                                            updateFriendPost.child("text").setValue(post);
                                            updateFriendPost.child("background_color").setValue(String.valueOf(postColor));
                                            updateFriendPost.child("user_id").setValue(uid);
                                            updateFriendPost.child("day_date_time").setValue(date);
                                            updateFriendPost.child("tag").setValue("TEXT_POST");
                                            Toast.makeText(getContext(), " post updated on friend node....:)(:   ", Toast.LENGTH_LONG).show();
                                        }

                            }else{
                                Toast.makeText(getContext(),"YOU DO NOT HAVE ANY FRIENDS YET!!!",Toast.LENGTH_LONG).show();
                            }
                            mDialog.dismiss();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}}) ;
                     }
                Toast.makeText(getContext(), " post updated...   ", Toast.LENGTH_LONG).show();

            } });


                    return rootView;
                 }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1 && data!= null){
           img=data.getData();
            GlideApp.with(context)
                    .asBitmap()
                    .load(img)
                    .fitCenter()
                    .into(status_image);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Main2Activity){
            this.context=(Main2Activity)context;
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



   /* /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
