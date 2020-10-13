package com.example.thebatproducts.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thebatproducts.HomeActivity;
import com.example.thebatproducts.Models.User;
import com.example.thebatproducts.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    EditText edtPhoneNumber;
    EditText edtPassword;
    CheckBox chkRememberMe;
    TextView txtForgotPassword;
    Button btnLogin;
    Button btnRegister;
    EditText edtSecurePhoneNumber;
    EditText edtSecureCode;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference users;
    StoredUser storedUser = new StoredUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtPhoneNumber = (EditText) findViewById(R.id.edtPhoneNumber);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        chkRememberMe = (CheckBox)findViewById(R.id.chkRememberMe);
        txtForgotPassword = (TextView)findViewById(R.id.txtForgotPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnRegister = (Button)findViewById(R.id.btnRegister);

        // to store logged in user through application
        Paper.init(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        users = firebaseDatabase.getReference("Users");

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(chkRememberMe.isChecked()) {
                    Paper.book().write(StoredUser.user_key, edtPhoneNumber.getText().toString());
                    Paper.book().write(StoredUser.user_password, edtPassword.getText().toString());
                }

                if(edtPhoneNumber.getText().toString().equals("") && edtPassword.getText().toString().equals("") || edtPhoneNumber.getText().toString().equals("") || edtPassword.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "Please enter your login details", Toast.LENGTH_SHORT).show();
                }
                else
                LoginUser(edtPhoneNumber.getText().toString(), edtPassword.getText().toString());
            }
        });

        String userKey = Paper.book().read(StoredUser.user_key);
        String passwordKey = Paper.book().read(StoredUser.user_password);

        // cater for null
        if(userKey != null && passwordKey != null) {
            if(!userKey.isEmpty() && !passwordKey.isEmpty())
                Login(userKey,passwordKey);
        }

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    private void Login(String phone, String passwordKey) {
        LoginUser(phone,passwordKey);
    }

    // extra - done in case user forgets password, will be prompt for secure code upon registration
    private void showDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
        alertDialog.setTitle("Oops...Forgot your Password?");
        alertDialog.setMessage("Please Enter your Secure Code");

        LayoutInflater inflater = this.getLayoutInflater();
        View reset_password = inflater.inflate(R.layout.forgot_password_layout, null);

        edtSecurePhoneNumber = reset_password.findViewById(R.id.edtSecurePhoneNumber);
        edtSecureCode = (EditText)reset_password.findViewById(R.id.edtSecureCode);
        alertDialog.setView(reset_password);
        alertDialog.setIcon(R.drawable.ic_baseline_info_24);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                users.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.child(edtSecurePhoneNumber.getText().toString()).getValue(User.class);
                        if(user.getSecureCode().equals(edtSecureCode.getText().toString()))
                            Toast.makeText(LoginActivity.this,"Your Password is: " + user.getPassword(), Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(LoginActivity.this, "Wrong Secure Code, Try Again or please contact support", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(LoginActivity.this, "Oops, Wrong Login Details.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void LoginUser(final String phoneNumber, String password) {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Login you into The Batman Experience, Please Wait...");
        progressDialog.show();

        final String localPhone = phoneNumber;
        final String localPassword = password;

        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child(localPhone).exists()) {
                    progressDialog.dismiss();
                    User user = snapshot.child(localPhone).getValue(User.class);
                    String email = (String) snapshot.child(localPhone).child("email").getValue();
                    String password = (String) snapshot.child(localPhone).child("password").getValue();
                    String name = (String) snapshot.child(localPhone).child("name").getValue();
                    user.setPassword(password);
                    user.setEmail(email);
                    user.setName(name);
                    user.setPhoneNumber(localPhone);

                    if(password.equals(localPassword)) {
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        storedUser.currentUser = user;
                        Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    } else
                        Toast.makeText(LoginActivity.this, "Wrong Password, Try Again", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "User does not Exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Wrong Password, Try Again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}