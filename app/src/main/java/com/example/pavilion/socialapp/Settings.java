package com.example.pavilion.socialapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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


//
//
// import de.hdodenhof.circleimageview.CircleImageView;


public class Settings extends AppCompatActivity {

    private static  final int Gal=1;
   // private static  final int new_Gal=2;
    private EditText name, phone, status, emaail;
     FirebaseAuth firebaseAuth;
    private DatabaseReference userRef;
   // private FirebaseAuth.AuthStateListener authStateListener;
    String txtName, txtPhone, txtEmail,txtStatus;
    private ImageView back_pic;
   // private CircleImageView front_pic;
    private Uri imgData, uri;  // frontPic;
    private String uid,  oldPicUri;
    private StorageReference displayImageStorageRef;
     ProgressDialog pd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        pd=new ProgressDialog(this);
        oldPicUri=null;
        //backPic=null;
        pd= new ProgressDialog(Settings.this);
        pd.setTitle("Saving Your Data");
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        displayImageStorageRef= FirebaseStorage.getInstance().getReference();

        imgData=null;
            firebaseAuth= FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if( user != null){
            uid=user.getUid();
            }
        else{
            Intent intent=new Intent(Settings.this,MainActivity.class);
            startActivity(intent);}

     userRef=FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        Log.d("GOT THE USERS", "REACHED TO THE UID>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");

        back_pic=findViewById(R.id.background_pic);
       // front_pic=findViewById(R.id.foreground_pic);

        name=findViewById(R.id.edit_name);
        phone=findViewById(R.id.edit_phone);
        status=findViewById(R.id.edit_status);
        emaail=findViewById(R.id.edit_email);



        emaail.setEnabled(false);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    name.setText(dataSnapshot.child("display_name").getValue(String.class));
                    phone.setText(dataSnapshot.child("phone").getValue(String.class));
                    status.setText(dataSnapshot.child("status").getValue(String.class));
                    emaail.setText(dataSnapshot.child("email_id").getValue(String.class));
                    final String uri=(dataSnapshot.child("back_pic").getValue(String.class));

                    if ( ! "noImage".equals(uri)){
                        oldPicUri=dataSnapshot.child("back_pic").getValue(String.class);
                        Log.d("old pic uri", "<<<<<<<<<<<<<<<<<<<<<<<<<<<<< "+ oldPicUri);
                    GlideApp
                            .with(Settings.this)
                            .load(Uri.parse(oldPicUri))
                            .circleCrop()
                            .placeholder(R.drawable.logo4)
                            .error(R.mipmap.ic_launcher)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .into(back_pic);}
                            else {
                        GlideApp.with(Settings.this)
                                .load(R.mipmap.ic_launcher_round)
                                .circleCrop()
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .error(R.mipmap.ic_launcher)
                                .into(back_pic);
                    }

                    /*GlideApp
                            .with(Settings.this)
                            .load(dataSnapshot.child("front_pic").getValue(String.class))
                            .placeholder(R.mipmap.ic_launcher_round)
                            .error(R.mipmap.ic_launcher)
                            .into(front_pic);*/


                }catch (Exception ex){
                   // Toast.makeText(Settings.this, "EXCEPTION!!!   "+ex, Toast.LENGTH_LONG).show();
                    Log.d("ERROR", ">>"+ ex+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


 }
//--------------------------------------------------------------------------------------------------------------

    public void goBack(View view){
        Intent intent= new Intent(Settings.this,Main2Activity.class);
        startActivity(intent);
    }
//--------------------------------------------------------------------------------------------------------------

  /*  public void get_front_image(View view)  {

        //getImage(0);

        Intent gallery= new Intent(Intent.ACTION_PICK);
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallery,"select your Display picture"),0);}*/
//--------------------------------------------------------------------------------------------------------------

    public void get_back_image(View view) {

       // getImage(1)

        Intent gallery= new Intent(Intent.ACTION_PICK);
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallery,"select your Display picture"),1);}
//--------------------------------------------------------------------------------------------------------------

    public void saveAllChanges(View view){

       pd.show();

        txtName=name.getText().toString().trim();
        txtPhone=phone.getText().toString().trim();
        txtStatus=status.getText().toString().trim();
        txtEmail=emaail.getText().toString().trim();


        if (imgData != null){
            StorageReference imgRef = displayImageStorageRef.child("Image").child(imgData.getLastPathSegment());
            imgRef.putFile(imgData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        uri = taskSnapshot.getDownloadUrl();
                      userRef.child("back_pic").setValue(uri.toString());
                      userRef.child("phone").setValue(txtPhone);
                      userRef.child("display_name").setValue(txtName);
                      userRef.child("status").setValue(txtStatus);

                      pd.dismiss();

                    Toast.makeText(Settings.this, "Your Settings have been saved Successfully", Toast.LENGTH_SHORT).show();}   })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(Settings.this, "updated failed   "+ e.toString(), Toast.LENGTH_SHORT).show();}   })

                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress= (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            pd.setMessage("uploaded "+(int)progress+"%");
                            Log.d("progress****", "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"+ progress+"%");
                        }   });


        }else{
            Toast.makeText(this, "no pic found to upload!!!!!!", Toast.LENGTH_SHORT).show();

            userRef.child("phone").setValue(txtPhone);
            userRef.child("display_name").setValue(txtName);
            userRef.child("status").setValue(txtStatus);
            pd.dismiss();
        }
        }

      //  public void deleteOldPic(String deletePic){


//--------------------------------------------------------------------------------------------------------------

  // public void getImage(int request){ }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

            if(requestCode == 1){
                if (resultCode == RESULT_OK && data != null) {
                    //  imgData = data.getData();
                    imgData = data.getData();
                   // backPic = imgData;
                    GlideApp
                            .with(Settings.this)
                            .asBitmap()
                            .load(imgData)
                            .circleCrop()
                            .placeholder(R.drawable.logo4)
                            .error(R.mipmap.ic_launcher)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(back_pic);

                }}else {
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
            }

           /* case 0:
 if (resultCode == RESULT_OK && imgData != null) {
                    // imgData = data.getData();
                    frontPic = imgData;
                    GlideApp
                            .with(Settings.this)
                            .load(imgData)
                            .error(R.mipmap.ic_launcher)
                            .into(front_pic);
                break; }*/

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent= new Intent(Settings.this,Main2Activity.class);
        startActivity(intent);
    }
}

/*
pro guard rules
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

-dontwarn com.bumptech.glide.load.resource.bitmap.VideoDecoder*/
