package live.sai.eyeball.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import live.sai.eyeball.Adapter.ProductAdapter;
import live.sai.eyeball.Model.Product;
import live.sai.eyeball.R;
import live.sai.eyeball.RestApiHelper.RestApiProduct;

import java.util.List;

public class ProductFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private RestApiProduct restApiProduct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        recyclerView = view.findViewById(R.id.productRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        productAdapter = new ProductAdapter();
        recyclerView.setAdapter(productAdapter);

        // Initialize RestApiProduct
        restApiProduct = new RestApiProduct(requireContext());

        // Setup SwipeRefreshLayout
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        // Fetch product data
        getAllProducts();

        return view;
    }

    private void getAllProducts() {
        // Use RestApiProduct to fetch all products
        restApiProduct.getAllProducts(new RestApiProduct.ProductListCallback() {
            @Override
            public void onSuccess(List<Product> productList) {
                // Products fetched successfully, set up the RecyclerView
                productAdapter.setProductList(productList);
            }

            @Override
            public void onFailure(String errorMessage) {
                // Handle failure
            }

            @Override
            public void onError(String errorMessage) {
                // Error occurred while fetching products, handle the error
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void refreshFirstTab() {
        // Fetch product data
        getAllProducts();
    }

    @Override
    public void onRefresh() {
        // Handle the refresh event here
        getAllProducts();
        swipeRefreshLayout.setRefreshing(false);
    }
}
