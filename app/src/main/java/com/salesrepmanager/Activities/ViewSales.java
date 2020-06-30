package com.salesrepmanager.Activities;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import com.salesrepmanager.R;
import com.salesrepmanager.helperClass.SQLiteHandler;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
public class ViewSales extends Activity {
    private TextView tv,TotalSalesAmmountTextView;
    private Spinner salesDurationRange;
    private SQLiteHandler db;
    public static String [] modelArray,brandArray,priceArray,modelInSales,time,date;
    private ListView ViewSalesView;
    private ArrayList<ViewSalesItems> arraylist;
    private ViewSalesCustomAdapter adapter;
    private Double TotalSalesAmmount=0.0;
    private List<byte[]> productImage;
    private  ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_sales);
        initialisation();
       loadProductDetails();
       uiSetUp();
       listenerMethods();
    }

    void PopulateStockListView(){
        arraylist = new ArrayList<ViewSalesItems>();
        for(int i=0;i<modelInSales.length;i++){
          arraylist.add(new ViewSalesItems(modelInSales[i],priceArray[i],time[i],date[i],productImage.get(i)));
            TotalSalesAmmount=TotalSalesAmmount+Double.parseDouble(priceArray[i]);
        }
        adapter = new ViewSalesCustomAdapter(ViewSales.this, arraylist);
        ViewSalesView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        TotalSalesAmmountTextView.setText(String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(TotalSalesAmmount))+"NGN");
    }
    void initialisation(){
        backButton= (ImageButton)findViewById(R.id.backButton);
        tv = (TextView)findViewById(R.id.title);
        ViewSalesView = (ListView)findViewById(R.id.ViewSalesListView);
        salesDurationRange = (Spinner) findViewById(R.id.salesDurationRange);
        TotalSalesAmmountTextView = (TextView)findViewById(R.id.TotalSalesAmmountTextView);
    };
    void uiSetUp(){
        tv.setText("Sales");
        spinner();
        PopulateStockListView();
    }
    void loadProductDetails(){
        db = new SQLiteHandler(ViewSales.this);
        modelArray=db.getMainProductModels();
        brandArray=db.getMainProductBrand();
        priceArray=db.getPriceFromSales();
        productImage=db.getProductImageFromSales();
        modelInSales=db.getModelFromSales();
        time=db.getTimeFromSales();
        date=db.getDateFromSales();
    }
    private void spinner() {
        List<String> categories = new ArrayList<String>();
        categories.add("Today");
        categories.add("This week");
        categories.add("This month");
        categories.add("Specify Range");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ViewSales.this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        salesDurationRange.setAdapter(dataAdapter);
    }
    private void listenerMethods(){
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
