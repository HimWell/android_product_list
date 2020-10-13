package com.example.thebatproducts;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import com.example.thebatproducts.Adaptors.ProductAdaptor;
import com.example.thebatproducts.Models.Product;
import com.example.thebatproducts.ProductInformation.ProductInformationActivity;
import com.example.thebatproducts.User.LoginActivity;
import com.example.thebatproducts.User.MyProfileActivity;
import com.example.thebatproducts.User.StoredUser;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView txtName;
    StoredUser storedUser;

    RecyclerView recycler_products;

    ProductAdaptor productAdaptor;
    DrawerLayout drawer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // created to store user through application
        Paper.init(this);

        recycler_products = (RecyclerView)findViewById(R.id.recycler_products);
        recycler_products.setLayoutManager(new LinearLayoutManager(this));

        //method to populate products
        loadProducts();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("The Bat Products");
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        txtName =(TextView)headerView.findViewById(R.id.txtName);
        txtName.setText(storedUser.currentUser.getEmail());

    }

    private void loadProducts() {

        //load product from firebase db
        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery( FirebaseDatabase.getInstance().getReference().child("Products"), Product.class).build();

        productAdaptor = new ProductAdaptor(options);
        recycler_products.setAdapter(productAdaptor);

        productAdaptor.setOnItemClickListener(new ProductAdaptor.onItemClickListener() {
            @Override
            public void onItemClick(DataSnapshot documentSnapshot, int position) {
                // parse product id to next activity by intent
                // Toast.makeText(HomeActivity.this, "Testing...", Toast.LENGTH_SHORT).show();
                 Intent intent = new Intent(HomeActivity.this, ProductInformationActivity.class);
                 intent.putExtra("productId",productAdaptor.getRef(position).getKey());
                 startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        productAdaptor.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        productAdaptor.startListening();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen((GravityCompat.START))) {

            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        return super.onOptionsItemSelected(menuItem);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        // extra - done in case user wants to navigate
        if(id == R.id.nav_profile) {
            Intent intent = new Intent(HomeActivity.this, MyProfileActivity.class);
            startActivity(intent);
        }

        if (id == R.id.nav_products) {
            Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
            startActivity(intent);
        }

        if(id == R.id.nav_exit) {
            Paper.book().destroy();
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}