package live.sai.eyeball.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import live.sai.eyeball.Model.Product;
import live.sai.eyeball.R;
import live.sai.eyeball.RestApiHelper.RestApiProduct;

import java.io.ByteArrayOutputStream;
public class AddProductFragment extends Fragment {

    private EditText productNameEditText;
    private Spinner productTypeSpinner;
    private EditText productCostEditText;
    private EditText productStocksEditText;
    private EditText productLowLevelStockEditText;
    private Button saveButton;

    private RestApiProduct restApiProduct;

    private ImageView productImageView;

    private Product product; // Declare product as a member variable
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1001;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);

        productNameEditText = view.findViewById(R.id.productNameEditText);
        productTypeSpinner = view.findViewById(R.id.productTypeSpinner);
        productCostEditText = view.findViewById(R.id.productCostEditText);
        productStocksEditText = view.findViewById(R.id.productStocksEditText);
        productLowLevelStockEditText = view.findViewById(R.id.productLowLevelStockEditText);
        saveButton = view.findViewById(R.id.saveButton);
        productImageView = view.findViewById(R.id.productImageView);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.product_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productTypeSpinner.setAdapter(adapter);

        // Check for camera permission
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            // Permission is already granted, perform camera related operations
        }


        productImageView = view.findViewById(R.id.productImageView);
        productImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        restApiProduct = new RestApiProduct(requireContext());

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProduct();
            }
        });

        return view;
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            productImageView.setImageBitmap(imageBitmap);
        }
    }

    private void saveProduct() {
        String productName = productNameEditText.getText().toString().trim();
        String productType = productTypeSpinner.getSelectedItem().toString().trim();
        String productCost = productCostEditText.getText().toString().trim();
        String productStocks = productStocksEditText.getText().toString().trim();
        String productLowLevelStock = productLowLevelStockEditText.getText().toString().trim();

        // Get the image bitmap from the ImageView
        BitmapDrawable drawable = (BitmapDrawable) productImageView.getDrawable();
        Bitmap imageBitmap = drawable.getBitmap();

        // Convert the image bitmap to a byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imageBytes = stream.toByteArray();

        if (productName.isEmpty() || productType.isEmpty() || productCost.isEmpty() || productStocks.isEmpty() || productLowLevelStock.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Product product = new Product();
        product.setName(productName);
        product.setType(productType);
        product.setCost(productCost);
        product.setStocks(productStocks);
        product.setLowLevelStock(productLowLevelStock);
        product.setImage(imageBytes); // Set the image bytes in the Product model

        restApiProduct.saveProduct(product, new RestApiProduct.CommonCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(requireContext(), "Product saved successfully", Toast.LENGTH_SHORT).show();
                productNameEditText.setText("");
                productTypeSpinner.setSelection(0);
                productCostEditText.setText("");
                productStocksEditText.setText("");
                productLowLevelStockEditText.setText("");
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(requireContext(), "Failed to save product: " + errorMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String errorMessage) {
                // Handle the error
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, perform camera related operations
            } else {
                // Permission is denied, handle the scenario
                Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
