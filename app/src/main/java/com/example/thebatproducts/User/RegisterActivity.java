package com.example.thebatproducts.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.thebatproducts.HomeActivity;
import com.example.thebatproducts.Models.User;
import com.example.thebatproducts.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    EditText edtPhoneNumber;
    EditText edtEmailAddress;
    EditText edtName;
    EditText edtPassword;
    EditText edtSecureCode;
    Button btnRegister;

    StoredUser storedUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtPhoneNumber = (EditText) findViewById(R.id.edtPhoneNumber);
        edtEmailAddress = (EditText) findViewById(R.id.edtEmailAddress);
        edtName = (EditText) findViewById(R.id.edtName);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtSecureCode = (EditText)findViewById(R.id.edtSecureCode);
        btnRegister = (Button)findViewById(R.id.btnRegister);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setMessage("Registering you, Please give us a moment before entering the Bat Experience...");
                progressDialog.show();

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        progressDialog.dismiss();
                        User user = new User(edtEmailAddress.getText().toString(), edtName.getText().toString(), edtPassword.getText().toString(), edtSecureCode.getText().toString());
                        databaseReference.child(edtPhoneNumber.getText().toString()).setValue(user);
                        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                        storedUser.currentUser = user;
                        Toast.makeText(RegisterActivity.this,"Sign up Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(RegisterActivity.this, "Oops, Required Details may be left out", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}