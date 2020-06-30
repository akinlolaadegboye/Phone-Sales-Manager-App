package com.salesrepmanager.Activities;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.salesrepmanager.R;
import com.salesrepmanager.helperClass.SQLiteHandler;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChangeProductPrice extends Activity {

    private TabHost th;
    private TextView tv,tv1,tv2,tv3,productPriceTextView,productBrandTextView,unitAvailableTextView;
    private Spinner productModelSpinner;
    private Button changePriceButton;
    private EditText chagePriceEdittext;
    private SQLiteHandler db;
    private  ImageButton backButton;
    public static String [] modelArray,brandArray,number_availableArray,priceArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_product_price);
        initialisation();
        uiSetUp();
        listenerMethod();
    }

    void initialisation(){
        db= new SQLiteHandler(ChangeProductPrice.this);
        modelArray=db.getMainProductModels();
        brandArray=db.getMainProductBrand();
        priceArray=db.getMainProductPrice();
        number_availableArray=db.getMainProductNumberAvailable();
        productModelSpinner = (Spinner) findViewById(R.id.productSpinner);
        backButton= (ImageButton)findViewById(R.id.backButton);
        tv = (TextView)findViewById(R.id.title);
        productModelSpinner = (Spinner) findViewById(R.id.productSpinner);
        productPriceTextView = (TextView) findViewById(R.id.productPriceTextView);
        productBrandTextView = (TextView) findViewById(R.id.productBrandTextView);
        unitAvailableTextView = (TextView) findViewById(R.id.unitAvailableTextView);
        changePriceButton = (Button) findViewById(R.id.changePriceButton);
        chagePriceEdittext = (EditText) findViewById(R.id.changePriceEdittext);
    }
    void uiSetUp(){
        tv.setText("Change Price");
        spinner();
        UpdateUiOnProductModelChange();
    }
    void listenerMethod(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.hold_animmation,
                        R.anim.abc_slide_out_bottom);
            }
        });

        changePriceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productModelString = productModelSpinner.getSelectedItem().toString();
                String newPriceString = String.valueOf(chagePriceEdittext.getText());
                if (productModelString.equalsIgnoreCase("Product model") && !newPriceString.equals("")) {
                    Toast.makeText(ChangeProductPrice.this, "Please,select Product model ", Toast.LENGTH_SHORT).show();
                }
                else if (productModelString.equalsIgnoreCase("Product model") && newPriceString.equals("")) {
                    Toast.makeText(ChangeProductPrice.this, "Please,select Product model and \n         New Price", Toast.LENGTH_SHORT).show();
                }
                else if (!productModelString.equalsIgnoreCase("Product model") && newPriceString.equals("")) {
                    Toast.makeText(ChangeProductPrice.this, "Please,fill the new Price.", Toast.LENGTH_SHORT).show();
                }
                else {
                    db.ChangePrice(productModelString,newPriceString);
                    productModelSpinner.setSelection(0);
                    chagePriceEdittext.setText(null);
                    Toast.makeText(ChangeProductPrice.this, "Price Changed Succesfully!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void UpdateUiOnProductModelChange(){
        productModelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String productModelString = productModelSpinner.getSelectedItem().toString();
                int productIndex =0;
                for(int i = 0;i<MainActivity.modelArray.length;i++){
                    if(modelArray[i].equalsIgnoreCase(productModelString)){
                        productIndex=i;
                    }
                }
                if (productModelString.equalsIgnoreCase("Product model")) {
                    productPriceTextView.setText("_  _ _ _");
                    productBrandTextView.setText("_ _ _ _");
                    unitAvailableTextView.setText("_ _ _ _");
                }else {
                    productPriceTextView.setText(NumberFormat.getNumberInstance(Locale.US).format(Double.parseDouble(priceArray[productIndex])) + "NGN");
                    productBrandTextView.setText(brandArray[productIndex]);
                    unitAvailableTextView.setText(number_availableArray[productIndex]);
                }
                modelArray=db.getMainProductModels();
                brandArray=db.getMainProductBrand();
                priceArray=db.getMainProductPrice();
                number_availableArray=db.getMainProductNumberAvailable();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    private void spinner() {
        List<String> categories = new ArrayList<String>();
        categories.add("Product model");
        for (int i=0;i<MainActivity.modelArray.length;i++){
            categories.add(MainActivity.modelArray[i]);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ChangeProductPrice.this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productModelSpinner.setAdapter(dataAdapter);
    }

}
