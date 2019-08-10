package com.example.pavilion.socialapp;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class getAllUsers extends Fragment {


    frndRequestAdapter adapter;
   // List<String> names;
    TextView mTextView;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<userItem> userList;
    FirebaseAuth firebaseAuth;
    DatabaseReference mRefToUser;
    DatabaseReference phoneRef;
    String uid, phn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_get_all_users, container, false);

        recyclerView=view.findViewById(R.id.UsersRecyclerView);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        mTextView = view.findViewById(R.id.autoComplete);

        userList = new ArrayList<>();
        mRefToUser = FirebaseDatabase.getInstance().getReference().child("users");
        mRefToUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (phn != null){
                        if ( ! phn.equals( ds.child("phone").getValue(String.class))){
                    userItem itemList= ds.getValue(userItem.class);
                    userList.add(itemList);
                    }}}
                adapter = new frndRequestAdapter( userList,getContext());
                recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

}
