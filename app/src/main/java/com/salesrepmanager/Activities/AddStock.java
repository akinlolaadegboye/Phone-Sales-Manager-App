package com.salesrepmanager.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.salesrepmanager.R;
import com.salesrepmanager.helperClass.AppUtilityClass;
import com.salesrepmanager.helperClass.SQLiteHandler;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddStock extends Activity {

    private TabHost th;
    private TextView tv, tv1, tv2, productPriceTextView, productBrandTextView, unitAvailableTextView;
    private Spinner productModelSpinner;
    private Button addUnitsButton, addNewProductButton;
    private ImageView productImageView;
    private EditText additionalUnitsEdittext, productModel, productBrand, productPrice, productnumberAvailable;
    private ImageButton pictureButton;
    int use = 0;
    private String picturePath;
    private Bitmap bp;
    private Uri selectedImage;
    private String DidUserAddToStock = "no";
    private SQLiteHandler db;
    private ImageButton backButton;
    public static String[] modelArray, brandArray, number_availableArray, priceArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_stock);
        initialisation();
        uiSetUp();
        listenerMethod();
        UpdateUiOnProductModelChange();
    }

    void uiSetUp() {
        tv = (TextView) findViewById(R.id.title);
        tv.setText("Add to Stock");
        th.setup();
        tab();
        spinner();
    }

    void listenerMethod() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.hold_animmation,
                        R.anim.abc_slide_out_bottom);
            }
        });
        pictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productModel.setFocusable(false);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
                use = 1;
            }
        });
        addNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isInserted;
                String model = productModel.getText().toString();
                String brand = productBrand.getText().toString();
                String price = productPrice.getText().toString();
                String numberAvailable = productnumberAvailable.getText().toString();
                if (model.isEmpty() || brand.isEmpty() || price.isEmpty() || numberAvailable.isEmpty()) {
                    Toast.makeText(AddStock.this, "Field(s) not filled.", Toast.LENGTH_SHORT).show();
                } else if (!model.isEmpty() && !brand.isEmpty() && !price.isEmpty() && !numberAvailable.isEmpty() && productImageView.isShown()) {

                    if (ModelSeenInDatabase(model) == 0) {
                        BitmapDrawable drawable = (BitmapDrawable) productImageView.getDrawable();
                        Bitmap productBitmap = drawable.getBitmap();
                        db.addMainProductDetails(model, brand, price, numberAvailable, AppUtilityClass.getBytes(productBitmap));
                        productModel.setText(null);
                        productBrand.setText(null);
                        productPrice.setText(null);
                        productImageView.setVisibility(View.GONE);
                        productnumberAvailable.setText(null);
                        Toast.makeText(AddStock.this, model + " added", Toast.LENGTH_SHORT).show();
                        DidUserAddToStock = "yes";
                        loadProductDetails();
                    } else {
                        Toast.makeText(AddStock.this, model + "is already Added to Stock.", Toast.LENGTH_SHORT).show();
                    }
                } else if (!productImageView.isShown()) {
                    Toast.makeText(AddStock.this, "Please Capture the Product Image.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addUnitsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productModelString = productModelSpinner.getSelectedItem().toString();
                String additionalUnitsString = String.valueOf(additionalUnitsEdittext.getText());
                if (productModelString.equalsIgnoreCase("Product model") && !additionalUnitsString.equals("")) {
                    Toast.makeText(AddStock.this, "Please,select Product model ", Toast.LENGTH_SHORT).show();
                } else if (productModelString.equalsIgnoreCase("Product model") && additionalUnitsString.equals("")) {
                    Toast.makeText(AddStock.this, "Please,select Product model and \n         Additional Unit(s)", Toast.LENGTH_SHORT).show();
                } else if (!productModelString.equalsIgnoreCase("Product model") && additionalUnitsString.equals("")) {
                    Toast.makeText(AddStock.this, "Fill the Additional Unit(s).", Toast.LENGTH_SHORT).show();
                } else if (!productModelString.equalsIgnoreCase("Product model") && additionalUnitsString.equals("0")) {
                    Toast.makeText(AddStock.this, "Additional Units cannot be zero.", Toast.LENGTH_SHORT).show();
                } else {
                    db.AddToNumberAvailable(productModelString, additionalUnitsString);
                    productModelSpinner.setSelection(0);
                    productPriceTextView.setText("N_ _ _ _");
                    productBrandTextView.setText("_ _ _ _");
                    unitAvailableTextView.setText("_ _ _ _");
                    additionalUnitsEdittext.setText(null);
                    Toast.makeText(AddStock.this, additionalUnitsString + " Units of " + productModelString + " added Succesfully!", Toast.LENGTH_LONG).show();
                    loadProductDetails();
                }
            }
        });

        productModel.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v == productModel) {
                    if (hasFocus) {
                        ((InputMethodManager) getApplicationContext().getSystemService(getApplicationContext().INPUT_METHOD_SERVICE)).showSoftInput(productModel, InputMethodManager.SHOW_FORCED);

                    } else {
                        ((InputMethodManager) getApplicationContext().getSystemService(getApplicationContext().INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(productModel.getWindowToken(), 0);
                    }
                }
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);
        if (view instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];
            if (event.getAction() == MotionEvent.ACTION_UP
                    && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w
                    .getBottom())) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus()
                        .getWindowToken(), 0);
            }
        }
        return ret;
    }

    void initialisation() {
        th = (TabHost) findViewById(android.R.id.tabhost);
        productModelSpinner = (Spinner) findViewById(R.id.productSpinner);
        productPriceTextView = (TextView) findViewById(R.id.productPriceTextView);
        productBrandTextView = (TextView) findViewById(R.id.productBrandTextView);
        unitAvailableTextView = (TextView) findViewById(R.id.unitAvailableTextView);
        addUnitsButton = (Button) findViewById(R.id.addUnitsButton);
        addNewProductButton = (Button) findViewById(R.id.add_new_product_button);
        additionalUnitsEdittext = (EditText) findViewById(R.id.additionalUnitsEdittext);
        productModel = (EditText) findViewById(R.id.model);
        productBrand = (EditText) findViewById(R.id.brand);
        productPrice = (EditText) findViewById(R.id.price);
        productnumberAvailable = (EditText) findViewById(R.id.number_available);
        productImageView = (ImageView) findViewById(R.id.product_imageView);
        pictureButton = (ImageButton) findViewById(R.id.picture_button);
        db = new SQLiteHandler(AddStock.this);
        modelArray = db.getMainProductModels();
        brandArray = db.getMainProductBrand();
        priceArray = db.getMainProductPrice();
        number_availableArray = db.getMainProductNumberAvailable();
        th = (TabHost) findViewById(android.R.id.tabhost);
        productModelSpinner = (Spinner) findViewById(R.id.productSpinner);
        backButton = (ImageButton) findViewById(R.id.backButton);
    }

    int ModelSeenInDatabase(String model) {
        int productSeen = 0;
        for (int i = 0; i < modelArray.length; i++) {
            if (modelArray[i].equalsIgnoreCase(model)) {
                productSeen = 1;
            }
        }
        return productSeen;
    }

    void loadProductDetails() {
        modelArray = db.getMainProductModels();
        brandArray = db.getMainProductBrand();
        priceArray = db.getMainProductPrice();
        number_availableArray = db.getMainProductNumberAvailable();
    }

    private void UpdateUiOnProductModelChange() {
        loadProductDetails();
        productModelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String productModelString = productModelSpinner.getSelectedItem().toString();
                int productIndex = 0;
                for (int i = 0; i < MainActivity.modelArray.length; i++) {
                    if (MainActivity.modelArray[i].equalsIgnoreCase(productModelString)) {
                        productIndex = i;
                    }
                }
                if (productModelString.equalsIgnoreCase("Product model")) {
                    productPriceTextView.setText(" _ _ _ _");
                    productBrandTextView.setText("_ _ _ _");
                    unitAvailableTextView.setText("_ _ _ _");
                } else {
                    productPriceTextView.setText(NumberFormat.getNumberInstance(Locale.US).format(Double.parseDouble(priceArray[productIndex])) + "NGN");
                    productBrandTextView.setText(brandArray[productIndex]);
                    unitAvailableTextView.setText(number_availableArray[productIndex]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    protected void onActivityResult(int requestcode, int resultCode, Intent data) {
        if (use == 1) {
            super.onActivityResult(requestcode, resultCode, data);
            if (requestcode == 0 && resultCode == RESULT_OK) {
                selectedImage = data.getData();
                bp = (Bitmap) data.getExtras().get("data");
                productImageView.setVisibility(View.VISIBLE);
                productImageView.setImageBitmap(bp);
            } else {
                Toast.makeText(this, "You did not Capture Image", Toast.LENGTH_LONG).show();
            }
        } else {
            try {
                if (requestcode == RESULT_OK || null != data) {
                    selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    productImageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                  } else {
                    Toast.makeText(this, "You haven't  pick Image", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Something went wrong in image proccess", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void spinner() {
        List<String> categories = new ArrayList<String>();
        categories.add("Product model");

        for (int i = 0; i < MainActivity.modelArray.length; i++) {
            categories.add(MainActivity.modelArray[i]);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddStock.this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productModelSpinner.setAdapter(dataAdapter);
    }

    private void tab() {


        TabHost.TabSpec spd = th.newTabSpec("tag1");
        spd.setContent(R.id.tab1);
        spd.setIndicator("New Product");
        th.addTab(spd);

        spd = th.newTabSpec("tag2");
        spd.setContent(R.id.tab2);
        spd.setIndicator("Existing Product");
        th.addTab(spd);

        th.getTabWidget().getChildAt(1).setBackgroundDrawable(new ColorDrawable(Color.parseColor("#fdce7e24")));
        tv1 = (TextView) th.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
        tv1.setTextColor(Color.parseColor("#ffffff"));
        tv2 = (TextView) th.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
        tv2.setTextColor(Color.parseColor("#ffffff"));
        tv = (TextView) th.getCurrentTabView().findViewById(android.R.id.title);
        tv.setTextColor(Color.parseColor("#fdce7e24"));

        th.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                for (int i = 0; i < th.getTabWidget().getChildCount(); i++) {
                    th.getTabWidget().getChildAt(i).setBackgroundDrawable(new ColorDrawable(Color.parseColor("#fdce7e24")));
                    tv = (TextView) th.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
                    tv.setTextColor(Color.parseColor("#ffffff"));
                }
                switch (th.getCurrentTab()) {
                    case 0:
                        th.getTabWidget().getChildAt(0).setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
                        th.getTabWidget().getChildAt(1).setBackgroundDrawable(new ColorDrawable(Color.parseColor("#fdce7e24")));
                        tv = (TextView) th.getCurrentTabView().findViewById(android.R.id.title);
                        tv.setTextColor(Color.parseColor("#fdce7e24"));
                        break;
                    case 1:
                        th.getTabWidget().getChildAt(1).setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
                        th.getTabWidget().getChildAt(0).setBackgroundDrawable(new ColorDrawable(Color.parseColor("#fdce7e24")));
                        tv = (TextView) th.getCurrentTabView().findViewById(android.R.id.title);
                        tv.setTextColor(Color.parseColor("#fdce7e24"));
                        break;
                }
            }

        });
    }
}
