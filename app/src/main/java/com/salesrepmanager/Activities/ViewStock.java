package com.salesrepmanager.Activities;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import com.salesrepmanager.R;
import com.salesrepmanager.helperClass.SQLiteHandler;

import java.util.ArrayList;
import java.util.List;

public class ViewStock extends Activity {
    private TextView tv;
    private Spinner productModelSpinner;
    private ListView ViewStockListView;
    private ArrayList<ViewStockItems> arraylist;
    private ViewStockCustomAdapter adapter;
    private SQLiteHandler db;
    private List<byte[]> productImage;
    private ImageButton backButton;
    public static String [] modelArray,brandArray,number_availableArray,priceArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_stock);
        loadProductDetails();
        initialisation();
        uiSetUp();
        listenerMethods();
        PopulateStockListView();
    }

    void PopulateStockListView(){
        arraylist = new ArrayList<ViewStockItems>();
        for(int i=0;i<MainActivity.modelArray.length;i++){
            arraylist.add(new ViewStockItems(modelArray[i],priceArray[i],number_availableArray[i], productImage.get(i)));//RepRegistration.productImages[i]
        }
        adapter = new ViewStockCustomAdapter(ViewStock.this, arraylist);
        ViewStockListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    void loadProductDetails(){
        db = new SQLiteHandler(ViewStock.this);
        modelArray=db.getMainProductModels();
        brandArray=db.getMainProductBrand();
        priceArray=db.getMainProductPrice();
        number_availableArray=db.getMainProductNumberAvailable();
        productImage=db.getProductImage();
    }
    void initialisation(){
        ViewStockListView = (ListView)findViewById(R.id.ViewStockListView);
        backButton= (ImageButton)findViewById(R.id.backButton);
        productModelSpinner = (Spinner) findViewById(R.id.productSpinner);
        tv = (TextView)findViewById(R.id.title);
    }
    void uiSetUp() {
        tv.setText("Stock");
    }
    void listenerMethods(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.hold_animmation,
                        R.anim.abc_slide_out_bottom);
            }
        });
    }
}
