package com.salesrepmanager.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.salesrepmanager.R;
import com.salesrepmanager.helperClass.SQLiteHandler;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TabHost th;
    private TextView tv, tv1, tv2, productPriceTextView, productBrandTextView,
            RepNameTextView, numberAvailableTextView;
    private Button SellButton;
    private Spinner productModelSpinner, dateSpinner;
    private String date, time, name;
    private List<byte[]> productImage;
    private HashMap<String, String> MainProductDetails;
    private SQLiteHandler db;
    public static String[] modelArray, brandArray, number_availableArray, priceArray;
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadProductDetails();
        initialisation();
        uiSetUp();
        listenerMethods();
    }


    void loadProductDetails() {
        db = new SQLiteHandler(MainActivity.this);
        productImage = db.getProductImage();
        MainProductDetails = db.getMainProductDetails();
        modelArray = db.getMainProductModels();
        brandArray = db.getMainProductBrand();
        priceArray = db.getMainProductPrice();
        number_availableArray = db.getMainProductNumberAvailable();
        name = db.getUserDetails().get("name");
    }

    void initialisation() {

        th = (TabHost) findViewById(android.R.id.tabhost);
        productModelSpinner = (Spinner) findViewById(R.id.productSpinner);
        productPriceTextView = (TextView) findViewById(R.id.productPriceTextView);
        productBrandTextView = (TextView) findViewById(R.id.productBrandTextView);
        numberAvailableTextView = (TextView) findViewById(R.id.numberAvailableTextView);
        RepNameTextView = (TextView) findViewById(R.id.RepNameTextView);
        SellButton = (Button) findViewById(R.id.SellButton);
        dateSpinner = (Spinner) findViewById(R.id.dateSpinner);

    }

    void uiSetUp() {
        th.setup();
        tab();
        spinner();
        dateSpinner();
        RepNameTextView.setText(name);
        UpdateUiOnProductModelChange();
    }

    void timeAndDate() {
        Date curDate = new Date();
        date = DateFormat.getDateInstance().format(curDate);
        time = DateFormat.getTimeInstance(DateFormat.SHORT).format(curDate);
    }

    void listenerMethods() {
        SellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productModelString = productModelSpinner.getSelectedItem().toString();
                long transaction_date = System.currentTimeMillis();
                if (productModelString.equalsIgnoreCase("Product model")) {
                    Toast.makeText(MainActivity.this, "Please,select Product model", Toast.LENGTH_SHORT).show();
                } else {
                    if (Integer.parseInt(String.valueOf(numberAvailableTextView.getText())) <= 0) {
                        Toast.makeText(MainActivity.this, productModelString + " is not Available", Toast.LENGTH_LONG).show();
                    } else if (Integer.parseInt(String.valueOf(numberAvailableTextView.getText())) > 0) {
                        int productIndex = 0;
                        for (int i = 0; i < modelArray.length; i++) {
                            if (modelArray[i].equalsIgnoreCase(productModelString)) {
                                productIndex = i;
                            }
                        }
                        timeAndDate();
                        db.addSales(productModelString, priceArray[productIndex], time, date, transaction_date, productImage.get(productIndex));
                        db.MinusOneFromDetailsNumberAvailable(productModelString);
                        productModelSpinner.setSelection(0);
                        loadProductDetails();
                        Toast.makeText(MainActivity.this, "Transaction Successful!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void dateSpinner() {
        List<String> categories = new ArrayList<String>();
        categories.add("DATE RANGE");
        categories.add("All Previous");
        categories.add("Today");
        categories.add("Specify Range");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(dataAdapter);
    }

    private void UpdateUiOnProductModelChange() {
        loadProductDetails();
        productModelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadProductDetails();
                String productModelString = productModelSpinner.getSelectedItem().toString();
                int productIndex = 0;
                for (int i = 0; i < modelArray.length; i++) {
                    if (modelArray[i].equalsIgnoreCase(productModelString)) {
                        productIndex = i;
                    }
                }
                productPriceTextView.setText(NumberFormat.getNumberInstance(Locale.US).format(Double.parseDouble(priceArray[productIndex])) + "NGN");
                productBrandTextView.setText(brandArray[productIndex]);
                numberAvailableTextView.setText(number_availableArray[productIndex]);
                if (productModelString.equalsIgnoreCase("Product model")) {
                    productPriceTextView.setText("_ _ _ _");
                    productBrandTextView.setText("_ _ _ _");
                    numberAvailableTextView.setText("_ _ _ _");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                loadProductDetails();
            }
        });
    }

    private void spinner() {
        List<String> categories = new ArrayList<String>();
        categories.add("Product model");
        for (int i = 0; i < modelArray.length; i++) {
            categories.add(modelArray[i]);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productModelSpinner.setAdapter(dataAdapter);
    }

    private void tab() {
        TabHost.TabSpec spd = th.newTabSpec("tag1");
        spd.setContent(R.id.tab1);
        spd.setIndicator("Sales");
        th.addTab(spd);
        spd = th.newTabSpec("tag2");
        spd.setContent(R.id.tab2);
        spd.setIndicator("Report");
        th.addTab(spd);
        th.getTabWidget().getChildAt(1).setBackgroundDrawable(new ColorDrawable(Color.parseColor("#fdce7e24")));
        tv2 = (TextView) th.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
        tv2.setTextColor(Color.parseColor("#ffffff"));
        tv1 = (TextView) th.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
        tv1.setTextColor(Color.parseColor("#ffffff"));
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

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Tap again to exit", Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProductDetails();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadProductDetails();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addToStock) {
            Intent intent = new Intent(MainActivity.this, AddStock.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.ViewSales) {
            Intent intent = new Intent(MainActivity.this, ViewSales.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.ViewStock) {
            Intent intent = new Intent(MainActivity.this, ViewStock.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.changeProductPrice) {
            Intent intent = new Intent(MainActivity.this, ChangeProductPrice.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
