package com.example.recipedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth auth;
    private Button signupButton;
    private EditText signupName, signupEmail, signupPassword, signupCPassword, signupPhone, signUpDob;
    private TextView loginRedirect;

    boolean passwordVisible1;
    boolean passwordVisible2;
    private DatePickerDialog picker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        String photos = "https://firebasestorage.googleapis.com/v0/b/recipedia-3c907.appspot.com/o/pp%2FIMG_20231123_133648511.jpg?alt=media&token=edaae85d-835d-46da-8d86-745a355e91a0";
        
        auth = FirebaseAuth.getInstance();

        signupButton= findViewById(R.id.button_signup);
        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupCPassword = findViewById(R.id.signup_cpassword);
        signUpDob = findViewById(R.id.signup_dob);
        loginRedirect = findViewById(R.id.text_toLogin);
        signupPhone = findViewById(R.id.signup_telephone);

        signUpDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(SignUp.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int years, int monthOfYear, int dayOfMonth) {
                        signUpDob.setText(dayOfMonth + "/" + (monthOfYear+1) + "/" + years);
                    }
                }, year, month, day);
                picker.show();
            }
        });


        signupButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String name = signupName.getText().toString().trim();
                String email = signupEmail.getText().toString().trim();
                String pass = signupPassword.getText().toString().trim();
                String cpass = signupCPassword.getText().toString().trim();
                String telephone = signupPhone.getText().toString().trim();
                String dob = signUpDob.getText().toString().trim();
                String member = "non-member";

                signupPassword.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        final int Right=2;
                        if (motionEvent.getAction()==MotionEvent.ACTION_UP){
                            if (motionEvent.getRawX()>=signupPassword.getRight()-signupPassword.getCompoundDrawables()[Right].getBounds().width()){
                                int selection= signupPassword.getSelectionEnd();
                                if(passwordVisible1){
                                    signupPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.eye_hide,0);
                                    signupPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                    passwordVisible1=false;
                                }else{
                                    signupPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.eye_show,0);
                                    signupPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                    passwordVisible1=true;

                                }
                                signupPassword.setSelection(selection);
                                return true;
                            }
                        }
                        return false;
                    }
                });

                signupCPassword.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        final int Right=2;
                        if (motionEvent.getAction()==MotionEvent.ACTION_UP){
                            if (motionEvent.getRawX()>=signupCPassword.getRight()-signupCPassword.getCompoundDrawables()[Right].getBounds().width()){
                                int selection= signupCPassword.getSelectionEnd();
                                if(passwordVisible2){
                                    signupCPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.eye_hide,0);
                                    signupCPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                    passwordVisible2=false;
                                }else{
                                    signupCPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.eye_show,0);
                                    signupCPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                    passwordVisible2=true;

                                }
                                signupCPassword.setSelection(selection);
                                return true;
                            }
                        }
                        return false;
                    }
                });

                String mobileRegex = "[0-9][0-9]{9}";
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";
                Matcher mobileMatcher;
                Pattern mobilePattern = Pattern.compile(mobileRegex);
                mobileMatcher = mobilePattern.matcher(telephone);

                if (!name.isEmpty()) {
                    if(!telephone.isEmpty()) {
                        if(mobileMatcher.find()) {
                            if (telephone.length() < 13) {
                                if (!dob.isEmpty()) {
                                    if (!email.isEmpty()) {
                                        if ((email.matches(emailPattern))) {
                                            if (!pass.isEmpty()) {
                                                if (!(pass.length() < 8)) {
                                                    if (!cpass.isEmpty()) {
                                                        if (!(cpass.equals(pass))) {
                                                            signupCPassword.setError("Password Don't Match");
                                                        } else {
                                                            auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(SignUp.this, "Sign Up Successfully", Toast.LENGTH_SHORT).show();

                                                                        FirebaseUser firebaseUser = auth.getCurrentUser();

                                                                        ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(name, telephone, dob, email, photos, member);

                                                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered Users");

                                                                        reference.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                            }
                                                                        });

                                                                        startActivity(new Intent(SignUp.this, Login.class));
                                                                    } else {
                                                                        Toast.makeText(SignUp.this, "Sign Up Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                        }

                                                    } else {
                                                        signupCPassword.setError("Confirm Password cannot be empty");
                                                        signupCPassword.requestFocus();
                                                    }

                                                } else {
                                                    signupPassword.setError("Password cannot be under 8");
                                                    signupPassword.requestFocus();
                                                }

                                            } else {
                                                signupPassword.setError("Password cannot be empty");
                                                signupPassword.requestFocus();
                                            }

                                        } else {
                                            signupEmail.setError("Email must end with @email.com");
                                            signupEmail.requestFocus();
                                        }

                                    } else {
                                        signupEmail.setError("Email cannot be empty");
                                        signupEmail.requestFocus();
                                    }
                                } else {
                                    signUpDob.setError("Date of Birth cannot be empty");
                                    signUpDob.requestFocus();
                                }
                            } else {
                                signupPhone.setError("Phone number cannot be over 12 digits");
                                signupPhone.requestFocus();
                            }

                        }else {
                            signupPhone.setError("Phone number is no valid");
                            signupPhone.requestFocus();
                        }
                    }else {
                        signupPhone.setError("Phone number cannot be empty");
                        signupPhone.requestFocus();
                    }

                } else {
                    signupName.setError("Name cannot be empty");
                    signupName.requestFocus();
                }
            }
        });

        loginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, Login.class));
            }
        });
    }

}
