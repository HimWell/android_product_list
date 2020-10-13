package com.example.thebatproducts.Adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.bumptech.glide.Glide;
import com.example.thebatproducts.Models.Product;
import com.example.thebatproducts.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProductAdaptor extends FirebaseRecyclerAdapter<Product, ProductAdaptor.ProductViewHolder> {

    private onItemClickListener listener;

    public ProductAdaptor(@NonNull FirebaseRecyclerOptions<Product> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Product product) {

        // Bind data from firebase db
        holder.productName.setText(product.getProductName());
        Glide.with(holder.productImage.getContext()).load(product.getProductImageURL()).into(holder.productImage);
        holder.productPrice.setText(product.getProductPrice());
        holder.productShortDescription.setText(product.getProductShortDescription());
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
       return new ProductViewHolder(view);
    }

    class ProductViewHolder extends RecyclerView.ViewHolder
    {
        public TextView productName;
        public CircleImageView productImage;
        public TextView productPrice;
        public TextView productShortDescription;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);


            // load items to view holder
            productName = (TextView) itemView.findViewById(R.id.productName);
            productImage = (CircleImageView) itemView.findViewById(R.id.productImage);
            productPrice = (TextView) itemView.findViewById(R.id.productPrice);
            productShortDescription = (TextView) itemView.findViewById(R.id.productShortDescription);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(DataSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }
}
