package com.example.recipedia;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private TextView name, email, telephone, dob, member, myrecipe;
    private ImageView photo;
    private String names, emails, telephones, dobs, photos, members;
    private Button btnLogout, editProfile;
    private ImageView imageView;
    private FirebaseAuth auth;
    ReadWriteUserDetails writeUserDetails;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        btnLogout = view.findViewById(R.id.button_logout);

        name = view.findViewById(R.id.name_prof);
        email = view.findViewById(R.id.email_prof);
        telephone = view.findViewById(R.id.phone_prof);
        dob = view.findViewById(R.id.dob_prof);
        editProfile = view.findViewById(R.id.button_editprofile);
        photo = view.findViewById(R.id.photo_prof);
        member = view.findViewById(R.id.member_prof);
        myrecipe = view.findViewById(R.id.my_recipe);

        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String uid = auth.getUid();

        if (firebaseUser == null) {
            Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
        } else {
            showUserProfile();
        }


        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfile.class);
                intent.putExtra("name", names.toString());
                intent.putExtra("email", emails.toString());
                intent.putExtra("mobile", telephones.toString());
                intent.putExtra("dob", dobs.toString());
                intent.putExtra("photo", photos.toString());
                intent.putExtra("member", members.toString());
                startActivity(intent);
            }
        });


        myrecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyRecipe.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getActivity().getSharedPreferences("checkbox", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();

                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                //signOutProfile();
                Toast.makeText(getActivity(), "Logout Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void showUserProfile() {
        String id = auth.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered Users");
        reference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readWriteUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readWriteUserDetails != null) {
                    names = readWriteUserDetails.name;
                    emails = readWriteUserDetails.email;
                    telephones = readWriteUserDetails.mobile;
                    dobs = readWriteUserDetails.dob;
                    photos = readWriteUserDetails.photo;
                    members = readWriteUserDetails.member;

                    name.setText(names);
                    email.setText(emails);
                    telephone.setText(telephones);
                    dob.setText(dobs);
                    member.setText(members);
                    Glide.with(getContext()).load(photos).into(photo);
                }

                if(member.getText().toString().contains("VIP-Member")){
                    member.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.verifieds,0,0);
                }
                else{
                    member.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0 ,0);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
