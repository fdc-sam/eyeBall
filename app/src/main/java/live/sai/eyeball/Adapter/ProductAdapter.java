package live.sai.eyeball.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import live.sai.eyeball.Model.Product;
import live.sai.eyeball.R;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> productList;

    public ProductAdapter() {
        this.productList = new ArrayList<>();
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView productIdTextView;
        private TextView productNameTextView;
        private TextView productTypeTextView;
        private TextView productStocksTextView;
        private TextView productLowLevelStockTextView;
        private TextView productDateCreateTextView;
        private TextView productDateModifiedTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productIdTextView = itemView.findViewById(R.id.product_id_textview);
            productNameTextView = itemView.findViewById(R.id.product_name_textview);
            productTypeTextView = itemView.findViewById(R.id.product_type_textview);
            productStocksTextView = itemView.findViewById(R.id.product_stocks_textview);
//            productLowLevelStockTextView = itemView.findViewById(R.id.product_low_level_stock_textview);
            productDateCreateTextView = itemView.findViewById(R.id.product_date_create_textview);
            productDateModifiedTextView = itemView.findViewById(R.id.product_date_modified_textview);
        }

        public void bind(Product product) {
            productIdTextView.setText(String.valueOf(product.getId()));
            productNameTextView.setText(product.getName());
            productTypeTextView.setText(product.getType());
            productStocksTextView.setText(String.valueOf(product.getStocks()));
//            productLowLevelStockTextView.setText(String.valueOf(product.getLowLevelStock()));
            productDateCreateTextView.setText(product.getDateCreate());
            productDateModifiedTextView.setText(product.getDateModified());
        }
    }
}
