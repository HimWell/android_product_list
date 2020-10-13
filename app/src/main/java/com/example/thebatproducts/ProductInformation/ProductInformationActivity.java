package com.example.thebatproducts.ProductInformation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.thebatproducts.Models.Product;
import com.example.thebatproducts.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProductInformationActivity extends AppCompatActivity {

    ImageView productImage;
    TextView productName;
    TextView productShortDescription;
    TextView productLongDescription;
    TextView productSpecs;
    Button btnEmailDetails;

    // created to parse selected product through activity
    String productId = "";
    Product product;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_information);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Products");

        productImage = (ImageView)findViewById(R.id.productImage);
        productName = (TextView)findViewById(R.id.productName);
        productShortDescription = (TextView)findViewById(R.id.productShortDescription);
        productLongDescription = (TextView)findViewById(R.id.productLongDescription);
        productSpecs = (TextView)findViewById(R.id.productSpecs);
        btnEmailDetails = (Button)findViewById(R.id.btnEmailDetails);

        // cater for null value
        if(getIntent() != null)
            productId = getIntent().getStringExtra("productId");
        if(!productId.isEmpty() && productId != null)
        {
            getProductInformationById(productId);
        }

        btnEmailDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserEmail();
            }
        });

    }

    private void sendUserEmail() {

        //as per spec - should just be able to send an email
        String productDetails = "";
        productDetails += "Product Name: " + product.getProductName() + "\n";
        productDetails += "Product Short Description: " + product.getProductShortDescription() + "\n";
        productDetails += "Product Price: " + product.getProductPrice();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"));
        intent.setType("message/rfc822");
        // intent.putExtra(Intent.EXTRA_EMAIL, new String[] { StoredUser.currentUser.getEmail() });
        intent.putExtra(Intent.EXTRA_SUBJECT, "The Bat Products - Product Information");
        intent.putExtra(Intent.EXTRA_TEXT, productDetails);

        // email validation
        try {
            startActivityForResult(Intent.createChooser(intent, "Send mail..."), 0);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ProductInformationActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    // this is to notify if the email was successful or not
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode  == 0) {
            Toast.makeText(ProductInformationActivity.this, "Your email has been sent successfully.", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(ProductInformationActivity.this, "Your email has failed to be sent.", Toast.LENGTH_SHORT).show();
        }
    }

    private void getProductInformationById(String productId) {

        //load data from firebase db
        databaseReference.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                product = dataSnapshot.getValue(Product.class);
                Picasso.with(getBaseContext()).load(product.getProductImageURL()).into(productImage);
                productName.setText(product.getProductName());
                productShortDescription.setText(product.getProductShortDescription());
                productLongDescription.setText(product.getProductLongDescription());
                productSpecs.setText(product.getProductSpecs());
        }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}