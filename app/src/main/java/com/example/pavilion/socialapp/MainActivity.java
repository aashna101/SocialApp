package com.example.pavilion.socialapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

// connected with chat app........................

    private EditText textEmail, textPassword, textDisplayName, TextPhone;
    private TextView Email_hide, phoneView;
    private Button regBtn, loginBtn, forgetPasswordBtn;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static String TAG="mAuth State";
    private String Email, Password, Name, Phone;
    private DatabaseReference mRef, mRef1;
    private LinearLayout linearLayout;
    private ProgressDialog progressDialog;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showDialog(this);

        mAuth=FirebaseAuth.getInstance();

        mRef= FirebaseDatabase.getInstance().getReference();
        mRef1= mRef.child("users");

        textEmail=findViewById(R.id.textEmail);
        textPassword=findViewById(R.id.txtPassword);
        textDisplayName=findViewById(R.id.textDisplayName);
        Email_hide=findViewById(R.id.Email);
        TextPhone=findViewById(R.id.textPhone);
        regBtn=findViewById(R.id.regBtn);
        loginBtn=findViewById(R.id.loginBtn);
        forgetPasswordBtn=findViewById(R.id.forgetPasswordBtn);
        linearLayout=findViewById(R.id.nameLayout);
        phoneView=findViewById(R.id.phnView);

        progressDialog= new ProgressDialog(this);
        progressDialog.setTitle("Performing Action!!!");
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);

        mAuthListener=new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid() );
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        // REGISTRATION OF THE NEW EMPLOYEE/////////////////////////////
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();

                Email=textEmail.getText().toString().trim();
                Password=textPassword.getText().toString().trim();
                Name=textDisplayName.getText().toString().trim();
                Phone=TextPhone.getText().toString().trim();


                if (!TextUtils.isEmpty(Email) && !TextUtils.isEmpty(Password) && !TextUtils.isEmpty(Name) && !TextUtils.isEmpty(Phone)){

                    mAuth.createUserWithEmailAndPassword(Email,Password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()){

                                        String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();

                                        DatabaseReference userRef=mRef1.child(uid);

                                        userRef.child("phone").setValue(Phone);  //setting users node
                                        userRef.child("email_id").setValue(Email);
                                        userRef.child("display_name").setValue(Name);
                                        userRef.child("back_pic").setValue("noImage");
                                        userRef.child("status").setValue("Hello there, I am on SocioApp now...");// setting profile node for modification

                                        //setting phone number in post node  for later reference...
                                        mRef.child("posts").child(Phone).child("user_id").setValue(uid);

                                        progressDialog.dismiss();

                                        Toast.makeText(MainActivity.this, "you are successfully registered.now please login!!!",
                                                Toast.LENGTH_SHORT).show();

                                        showDialog(MainActivity.this);
                                    }/*else {

                                        progressDialog.dismiss();

                                        Toast.makeText(MainActivity.this, "Something went wrong...Please try again later!!!",
                                                Toast.LENGTH_SHORT).show();
                                    }*/

                                }
                            }); }
            } });                                      // REGISTRATION BLOCK ENDS HERE//////////////////////////////////

        // LOGIN BLOCK STARTS HERE//////////////////////////////////////////
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();

                Email=textEmail.getText().toString().trim();
                Password=textPassword.getText().toString().trim();
                Phone=TextPhone.getText().toString().trim();


                if (!TextUtils.isEmpty(Email) && !TextUtils.isEmpty(Password) ){

                    mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){

                                        progressDialog.dismiss();

                                        Toast toast=  Toast.makeText(MainActivity.this, "You are successfully Logged in...", Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER,0,0);
                                        toast.show();

                                        Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                                        startActivity(intent);

                                    }else{ progressDialog.dismiss();

                                        Toast toast=  Toast.makeText(MainActivity.this, "You are new in this app Register first?",Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER,0,0);
                                        toast.show();
                                        showDialog(MainActivity.this); }
                                }
                            }
                    );
                }


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public  void showDialog(Context context){

        final AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(context);

        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view =inflater.inflate(R.layout.ask_choice,null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog alertDialog=alertDialogBuilder.create();
        alertDialog.show();

        Button regClicked= view.findViewById(R.id.registerClicked);
        Button loginClicked=
                view.findViewById(R.id.loginClicked);
        regClicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.VISIBLE);
                forgetPasswordBtn.setVisibility(View.INVISIBLE);
                Email_hide.setVisibility(View.VISIBLE);
                textEmail.setVisibility(View.VISIBLE);
                phoneView.setVisibility(View.VISIBLE);
                TextPhone.setVisibility(View.VISIBLE);
                loginBtn.setVisibility(View.GONE);
                regBtn.setVisibility(View.VISIBLE);
                alertDialog.cancel();
            }
        });
        loginClicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgetPasswordBtn.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
                Email_hide.setVisibility(View.VISIBLE);
                textEmail.setVisibility(View.VISIBLE);
                phoneView.setVisibility(View.GONE);
                TextPhone.setVisibility(View.GONE);
                regBtn.setVisibility(View.GONE);
                loginBtn.setVisibility(View.VISIBLE);
                alertDialog.cancel();

            }
        });

    }


}