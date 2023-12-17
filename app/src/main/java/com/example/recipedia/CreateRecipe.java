package com.example.recipedia;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CreateRecipe extends Fragment {

    ImageView recipe_photo;
    TextView recipe_user, recipe_category;
    Button create_button;
    EditText recipe_name, recipe_ingredient, recipe_time, recipe_serving, recipe_calories, recipe_video, recipe_direction;
    String imageUrl, user;
    Uri uri;
    FirebaseAuth auth;
    String[] category = {"meat", "vegetarian", "noodle", "soup", "seafood", "bakery", "lowCarb", "diabetic", "lowFat", "juices", "boba", "coffee"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create, container, false);

        recipe_photo = view.findViewById(R.id.recipe_photo);
        recipe_name = view.findViewById(R.id.recipe_name);
        recipe_ingredient = view.findViewById(R.id.recipe_ingredient);
        recipe_time = view.findViewById(R.id.recipe_time);
        recipe_serving = view.findViewById(R.id.recipe_serving);
        recipe_calories = view.findViewById(R.id.recipe_calories);
        recipe_video = view.findViewById(R.id.recipe_video);
        create_button = view.findViewById(R.id.create_button);
        recipe_category = view.findViewById(R.id.recipe_category);
        recipe_direction = view.findViewById(R.id.recipe_direction);
        recipe_user = view.findViewById(R.id.recipe_user);

        autoCompleteTextView = view.findViewById(R.id.category_cuy);
        adapterItems = new ArrayAdapter<String>(getActivity(), R.layout.list_item, category);

        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String Item = parent.getItemAtPosition(position).toString();
                recipe_category.setText(Item.toString());
            }
        });

        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();

        user = firebaseUser.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered Users");
        reference.child(user).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readWriteUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readWriteUserDetails != null) {
                    String names = readWriteUserDetails.name;
                    recipe_user.setText(names);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri = data.getData();
                            recipe_photo.setImageURI(uri);

                        } else {
                            Toast.makeText(getActivity(), "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        recipe_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });

        return view;
    }

    public void saveData() {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Recipe").child(uri.getLastPathSegment());

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setCancelable(false);
            builder.setView(R.layout.progress_layout);
            AlertDialog dialog = builder.create();
            dialog.show();

            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete()) ;
                    Uri urlImage = uriTask.getResult();
                    imageUrl = urlImage.toString();
                    uploadData();
                    dialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Photo cannot be empty", Toast.LENGTH_SHORT).show();
                    recipe_photo.requestFocus();
                }
            });
        }

    public void uploadData() {
        String name = recipe_name.getText().toString();
        String ingredient = recipe_ingredient.getText().toString();
        String time = recipe_time.getText().toString();
        String serving = recipe_serving.getText().toString();
        String calories = recipe_calories.getText().toString();
        String users = recipe_user.getText().toString();
        String video1;
        String video = recipe_video.getText().toString();
        String categories = recipe_category.getText().toString();
        String direction = recipe_direction.getText().toString();
        String uid = auth.getUid();
        int rating = 0;

        if(!name.isEmpty()) {
            if (!ingredient.isEmpty()) {
                if (!direction.isEmpty()) {
                    if (!time.isEmpty()) {
                        if (!serving.isEmpty()) {
                            if (!calories.isEmpty()) {
                                if(!video.isEmpty()) {
                                    if (video.contains("=")) {
                                        String array[] = video.split("=", 2);
                                        String array2 = array[1];
                                        video1 = array2;

                                        if (!categories.isEmpty()) {
                                            if (!time.isEmpty()) {
                                                DataClass dataClass = new DataClass(name, ingredient, time, imageUrl, serving, calories, video1, categories, direction, users, rating, user);

                                                FirebaseDatabase.getInstance().getReference("Recipe").child(categories).child(name).child(user).setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(getActivity(), Login.class);
                                                            startActivity(intent);
                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                        } else {
                                            categories = "meat";

                                            DataClass dataClass = new DataClass(name, ingredient, time, imageUrl, serving, calories, video1, categories, direction, users, rating, uid);

                                            FirebaseDatabase.getInstance().getReference("Recipe").child(categories).child(name).child(user).setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(getActivity(), Login.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    } else {
                                        recipe_video.setError("Please input link Youtube video");
                                        recipe_video.requestFocus();
                                    }
                                } else {
                                    recipe_video.setError("Video Link cannot be empty");
                                    recipe_video.requestFocus();
                                }
                            } else {
                                recipe_calories.setError("Calories cannot be empty");
                                recipe_calories.requestFocus();
                            }
                        } else {
                            recipe_serving.setError("Serving cannot be empty");
                            recipe_serving.requestFocus();
                        }
                    } else {
                        recipe_time.setError("Time cannot be empty");
                        recipe_time.requestFocus();
                    }
                } else {
                    recipe_direction.setError("Direction cannot be empty");
                    recipe_direction.requestFocus();
                }
            } else {
                recipe_ingredient.setError("Ingredient cannot be empty");
                recipe_ingredient.requestFocus();
            }
        } else{
            recipe_name.setError("Name cannot be empty");
            recipe_name.requestFocus();
        }
    }


}