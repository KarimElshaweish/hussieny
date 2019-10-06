package com.example.karim.MedicalRep.Activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.karim.MedicalRep.MainActivity;
import com.example.karim.MedicalRep.R;
import com.example.karim.MedicalRep.model.User;
import com.example.karim.MedicalRep.shared;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    View registerLayout,loginLayout;
    TextView nameText,emailTextRegister,postionText,passwordText,loginPassword,loginPhoneNumber;
    ProgressBar pb;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference mReference=database.getReference("OrderUser");
    SharedPreferences sharedPreferences;


    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallsBack;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcalls;
    private FirebaseAuth mAuth;

    public void Close(View view){
        finish();
    }
    private void __init__(){
        mAuth=FirebaseAuth.getInstance();
        sharedPreferences=getSharedPreferences("pref",MODE_PRIVATE);
        registerLayout=findViewById(R.id.register_layout);
        loginLayout=findViewById(R.id.login_layout);
        nameText=findViewById(R.id.nameText);
        emailTextRegister=findViewById(R.id.email);
        passwordText=findViewById(R.id.passordText);
        pb=findViewById(R.id.pb);
        loginPassword=findViewById(R.id.loginPassword);
        loginPhoneNumber=findViewById(R.id.loginPhone);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Window w=getWindow();
//        w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_register);
        __init__();
    }
    public void viewRegisterClicked(View v){
        loginLayout.setVisibility(View.GONE);
        registerLayout.setVisibility(View.VISIBLE);
    }
    public void viewLoginClicked(View v){
        loginLayout.setVisibility(View.VISIBLE);
        registerLayout.setVisibility(View.GONE);
    }
    public void Registerfunc(View v){

        validation();
    }
    private void validation(){
        String name=nameText.getText().toString();
        if(name.equals("")){
            nameText.setError("please enter your name");
        }else if(emailTextRegister.getText().toString().equals("")){
            emailTextRegister.setText("please enter your email");
        }else if(passwordText.getText().toString().equals("")){
            passwordText.setError("please enter your password");
        }else{
            pb.setVisibility(View.VISIBLE);
            nameText.setEnabled(false);
            passwordText.setEnabled(false);
            emailTextRegister.setEnabled(false);
            signUp(emailTextRegister.getText().toString(),passwordText.getText().toString(),nameText.getText().toString());
        }
    }
    private void signUp(String email, String password, final String name){
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    pushData();
                                }
                            });
                        } else {
                            Toast.makeText(Register.this, "Authentication failed:"+task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
    private void pushData(){
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user=new User();
                    user.setName(nameText.getText().toString());
                    user.setEmail(emailTextRegister.getText().toString());
                    user.setId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    mReference.child(user.getId()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                          finish();
                          startActivity(new Intent(Register.this,MainActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Register.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    ProgressDialog progressDialog;
    public void login(View v) {

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Loading....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        if(loginPhoneNumber.getText().toString().toLowerCase().equals("admin")&&loginPassword.getText().toString().equals("123")){
            shared.admin=true;
            shared.userName="Admin";
            progressDialog.dismiss();
            startActivity(new Intent(this,MainActivity.class));
        }else{
            mAuth.signInWithEmailAndPassword(loginPhoneNumber.getText().toString(), loginPassword.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                FirebaseUser user = mAuth.getCurrentUser();
                                finish();
                                startActivity(new Intent(Register.this,MainActivity.class));
                            } else {
                                progressDialog.dismiss();
                                Log.w("loginEmail", "signInWithEmail:failure", task.getException());
                                Toast.makeText(Register.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                            }
                    });
        }
    }

}
