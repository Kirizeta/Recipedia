package com.example.recipedia;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class EditProfile extends AppCompatActivity {

    ImageView back, editPhoto;
    TextView photoStr;
    EditText editName, editEmail, editDob, editPhone, editMember;
    Button changePicture, savedProf;
    DatabaseReference reference;
    DatePickerDialog picker;
    String name, email, phone, dob, photo, changePhoto, member;
    FirebaseAuth auth;
    private TextView membership1;
    ActivityResultLauncher<Intent> activityResultLauncher;
    static Uri uri;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        back = findViewById(R.id.edit_back);
        auth = FirebaseAuth.getInstance();

        reference = FirebaseDatabase.getInstance().getReference("Registered Users");

        photoStr = findViewById(R.id.photo_string);
        editPhoto = findViewById(R.id.edit_picture);
        editName = findViewById(R.id.edit_name);
        editEmail = findViewById(R.id.edit_email);
        editDob = findViewById(R.id.edit_dob);
        editPhone = findViewById(R.id.edit_phone);
        savedProf = findViewById(R.id.button_saved);
        changePicture = findViewById(R.id.button_editpicture);
        membership1 = findViewById(R.id.vip_membership);

        showData();

        membership1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMembership();

            }
        });



        editDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(EditProfile.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int years, int monthOfYear, int dayOfMonth) {
                        editDob.setText(dayOfMonth + "/" + (monthOfYear+1) + "/" + years);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result.getResultCode() == Activity.RESULT_OK){
                Intent data = result.getData();
                if(data !=null && data.getData() != null){
                    uri = data.getData();
                    setProfilePic(EditProfile.this, uri, editPhoto);
                    photo = uri.toString();
                }
            }
        });

        changePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImagePicker.with(EditProfile.this).cropSquare().compress(512).maxResultSize(512, 512).createIntent(new Function1<Intent, Unit>() {
                    @Override
                    public Unit invoke(Intent intent) {
                        activityResultLauncher.launch(intent);
                        return null;
                    }
                });

            }
        });

        savedProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNameChanged() || isEmailChanged() || isDobChanged() || isPhoneChanged() || isPhotoChanged()){
                    Toast.makeText(EditProfile.this, "Saved", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(EditProfile.this, "No Changes Found", Toast.LENGTH_SHORT).show();
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    public boolean isPhotoChanged(){
        if(!photo.equals(changePhoto.toString())){
            saveData();
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            return true;
        }
        else{
            Toast.makeText(this, "No data changed", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    public boolean isNameChanged(){
        if(!name.equals(editName.getText().toString())){
            reference = FirebaseDatabase.getInstance().getReference("Registered Users");
            reference.child(auth.getUid()).child("name").setValue(editName.getText().toString());
            name = editName.getText().toString();
            return true;
        }
        else{
            return false;
        }
    }


    public boolean isEmailChanged(){
        if(!email.equals(editEmail.getText().toString())){
            reference = FirebaseDatabase.getInstance().getReference("Registered Users");
            reference.child(auth.getUid()).child("email").setValue(editEmail.getText().toString());
            email = editEmail.getText().toString();
            return true;
        }
        else{
            return false;
        }
    }

    public boolean isPhoneChanged(){
        if(!phone.equals(editPhone.getText().toString())){
            reference = FirebaseDatabase.getInstance().getReference("Registered Users");
            reference.child(auth.getUid()).child("mobile").setValue(editPhone.getText().toString());
            phone = editPhone.getText().toString();
            return true;
        }
        else{
            return false;
        }
    }


    public boolean isDobChanged(){
        if(!dob.equals(editDob.getText().toString())){
            reference = FirebaseDatabase.getInstance().getReference("Registered Users");
            reference.child(auth.getUid()).child("dob").setValue(editDob.getText().toString());
            dob = editDob.getText().toString();
            return true;
        }
        else{
            return false;
        }
    }

    public void showData(){
        Intent intent = getIntent();

        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        dob = intent.getStringExtra("dob");
        phone = intent.getStringExtra("mobile");
        photo = intent.getStringExtra("photo");
        changePhoto = intent.getStringExtra("photo");
        member = intent.getStringExtra("member");

        editName.setText(name);
        editDob.setText(dob);
        editEmail.setText(email);
        editPhone.setText(phone);
        photoStr.setText(changePhoto);

        Glide.with(this).load(photo).into(editPhoto);

    }

    public void saveData(){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("pp").child(uri.getLastPathSegment());

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri urlImage = uriTask.getResult();
                photo = urlImage.toString();
                uploadData();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    public void uploadData() {
        auth = FirebaseAuth.getInstance();
        ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(name, phone, dob, email, photo, member);
        FirebaseDatabase.getInstance().getReference("Registered Users").child(auth.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(EditProfile.this, "Saved",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditProfile.this, Login.class);
                    startActivity(intent);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfile.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void setProfilePic(Context context, Uri imageUri, ImageView imageView){
        Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform()).into(imageView);
    }


    private void showMembership(){
        Dialog dialog = new Dialog(EditProfile.this);
        dialog.setContentView(R.layout.membership);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.yellow_round);

        Button access = dialog.findViewById((R.id.access));
        Button cancel = dialog.findViewById((R.id.cancel));

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        access.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(EditProfile.this, "You have successfully joined membership ", Toast.LENGTH_SHORT);
                member = "VIP-Member";
                ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(name, phone, dob, email, photo, member);
                FirebaseDatabase.getInstance().getReference("Registered Users").child(auth.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast toast = Toast.makeText(EditProfile.this, "Congratulation, Now you are VIP-Member of RECIPEDIA", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfile.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.dismiss();
            }
        });
        dialog.show();

    }

}
