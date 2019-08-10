package com.example.pavilion.socialapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Main2Activity extends AppCompatActivity {

    Toolbar toolbar;
    TextView status, name, email;
    ImageView frntImage;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FirebaseAuth firebaseAuth;
    DatabaseReference userRef;
    FrameLayout fragmentPlace;
    FirebaseUser user;
    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        firebaseAuth= FirebaseAuth.getInstance();

         user = firebaseAuth.getCurrentUser();
        if( user != null){
            uid=user.getUid();
        }
        else{
            Intent intent=new Intent(Main2Activity.this,MainActivity.class);
            startActivity(intent);}

        userRef= FirebaseDatabase.getInstance().getReference().child("users").child(uid);

        toolbar=findViewById(R.id.mtoolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.logo5);
        fragmentPlace=findViewById(R.id.content_frame);

        drawerLayout=findViewById(R.id.drawerLayout);
        navigationView=findViewById(R.id.navigationView);
        frntImage= navigationView.getHeaderView(0).findViewById(R.id.profile_front_pic);
        name=navigationView.getHeaderView(0).findViewById(R.id.nameText);
        status=navigationView.getHeaderView(0).findViewById(R.id.statusText);
        email=navigationView.getHeaderView(0).findViewById(R.id.emailText);
        //backImage=navigationView.getHeaderView(0).findViewById(R.id.profile_back_pic);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String uri = (dataSnapshot.child("back_pic").getValue(String.class));
                name.setText(dataSnapshot.child("display_name").getValue(String.class));
                status.setText(dataSnapshot.child("status").getValue(String.class));
                email.setText(dataSnapshot.child("email_id").getValue(String.class));
                try {



                    if (!"noImage".equals(uri)) {
                        GlideApp
                                .with(Main2Activity.this)
                                .load(Uri.parse(uri))
                                .circleCrop()
                                .placeholder(R.drawable.logo4)
                                .error(R.mipmap.ic_launcher)
                                .priority(Priority.IMMEDIATE)
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                .into(frntImage);



                    } else {
                        GlideApp.with(Main2Activity.this)
                                .load(R.mipmap.ic_launcher)
                                .error(R.mipmap.ic_launcher)
                                .into(frntImage);

                       /* GlideApp.with(Main2Activity.this)
                                .load(R.drawable.logo4)
                                .error(R.mipmap.ic_launcher)
                                .into(backImage);*/
                    }


                }catch (Exception ex){
                    Log.d("EXCEPTION", "onDataChange: <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"+ ex);
                    Toast.makeText(Main2Activity.this, ""+ex, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content_frame, new BlankFragment(), "Status!!!")
                    .disallowAddToBackStack()
                    .commit();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .remove(getSupportFragmentManager().findFragmentById(R.id.content_frame))
                        .commit();
                switch (item.getItemId()){

                    case R.id.profile:
                        Intent profileIntent=new Intent(Main2Activity.this,profile.class);
                        profileIntent.putExtra("checkStatus","Load_my_data");
                        startActivity(profileIntent);
                        break;
                    case R.id.status:

                        getSupportFragmentManager()
                                .beginTransaction()
                                .add(R.id.content_frame, new NewStatus(), "Status!!!")
                                .commit();
                        break;
                    case R.id.home:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .add(R.id.content_frame, new BlankFragment(), "your posts")
                                .commit();
                        break;
                    case R.id.like:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .add(R.id.content_frame, new getAllUsers(), "FRIENDS")
                                .commit();
                        break;
                    case R.id.settings:
                        Intent intent= new Intent(Main2Activity.this,Settings.class);
                        startActivity(intent);

                        Toast toast= Toast.makeText(getApplicationContext(),"Edit your profile settings here...",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                        break;}

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FirebaseAuth.getInstance().signOut();
        Intent goBack=new Intent(Main2Activity.this, MainActivity.class);
        startActivity(goBack);

    }
}
