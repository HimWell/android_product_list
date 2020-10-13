package com.example.thebatproducts.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import java.util.ArrayList;

public class MyProfileActivity extends AppCompatActivity {

    EditText edtPhoneNumber;
    EditText edtEmailAddress;
    EditText edtName;
    Button btnUpdateDetails;

    ArrayList<User> userList;
    User user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    // parse to get logged in user details
    String phoneNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        // extra functionality to update user details
        edtPhoneNumber = (EditText) findViewById(R.id.edtPhoneNumber);
        edtPhoneNumber.setEnabled(false);
        edtEmailAddress = (EditText)findViewById(R.id.edtEmailAddress);
        edtName = (EditText)findViewById(R.id.edtName);
        btnUpdateDetails = (Button)findViewById(R.id.btnUpdateDetails);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        userList = new ArrayList<>();

        phoneNumber = StoredUser.currentUser.getPhoneNumber();

        // get user details from firebase db
        loadProfileData(phoneNumber);
        user = new User();
        
        btnUpdateDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = edtPhoneNumber.getText().toString();
                String email = edtEmailAddress.getText().toString();
                String name = edtName.getText().toString();
                UpdateProfileDetails(phoneNumber, email, name);

            }
        });
    }

    private void UpdateProfileDetails(String phoneNumber, String email, String name) {
        databaseReference.getRef().child(phoneNumber).child("email").setValue(email);
        databaseReference.getRef().child(phoneNumber).child("name").setValue(name);

        Intent intent = new Intent(MyProfileActivity.this, HomeActivity.class);
        Toast.makeText(MyProfileActivity.this, "Profile Details Updated", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    private void loadProfileData(final String phoneNumber) {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child(phoneNumber).getValue(User.class);
                String email = (String) dataSnapshot.child(phoneNumber).child("email").getValue();
                String name = (String) dataSnapshot.child(phoneNumber).child("name").getValue();

                user.setEmail(email);
                user.setName(name);
                user.setPhoneNumber(phoneNumber);
                edtPhoneNumber.setText(phoneNumber);
                edtEmailAddress.setText(email);
                edtName.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MyProfileActivity.this, "Oops... Error Loading Profile Details", Toast.LENGTH_SHORT).show();
            }
        });
    }
}