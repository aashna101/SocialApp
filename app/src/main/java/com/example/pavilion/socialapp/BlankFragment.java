package com.example.pavilion.socialapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BlankFragment extends Fragment {
   ArrayList<StatusView> list;
   DatabaseReference ref1, ref2;
   FirebaseAuth firebaseAuth;
   private String uid;// getUserID;
  private String ref_phn, my_name, myBackPic;
    private RecyclerView myStatusView;
    private RecyclerView.Adapter statusAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth= FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if( user != null){
            uid= user.getUid();
            Log.d("REACHED TO UID", " reached reached reached<<<<<<<<<<<<<<<<<<< <<<<<<<<"+ uid);}
 }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_blank, container, false);
        ref1= FirebaseDatabase.getInstance().getReference().child("users").child(uid);


        myStatusView=view.findViewById(R.id.recyclerView);
        myStatusView.setHasFixedSize(true);
        myStatusView.setLayoutManager(new LinearLayoutManager(getContext()));
        list=new ArrayList<>();

        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ref_phn =dataSnapshot.child("phone").getValue(String.class);
                my_name=dataSnapshot.child("display_name").getValue(String.class);
                myBackPic=dataSnapshot.child("back_pic").getValue(String.class);
                Log.d("REACHED TO PHONE", " <<<<<<<<<<<<<<<<<<<<<<<<<<< <<<<<<<<"+ ref_phn);

                if (ref_phn != null && my_name != null && myBackPic != null) {
                    ref2 = FirebaseDatabase.getInstance().getReference().child("posts").child(ref_phn);


                    ref2.child("all_posts").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                 StatusView  statusView = ds.getValue(StatusView.class);
                                    list.add(statusView);


                            }
                            statusAdapter = new StatusAdapter(list, getContext());
                            //  myStatusView.setItemAnimator(new DefaultItemAnimator());
                            myStatusView.addItemDecoration(new DividerItemDecoration(myStatusView.getContext(), DividerItemDecoration.VERTICAL));
                            myStatusView.setAdapter(statusAdapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ERROR", " couldn't fetch posts <<<<<<<<"+ databaseError);
            }
        });




//        myStatusView.scrollToPosition(statusAdapter.getItemCount());
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


}
