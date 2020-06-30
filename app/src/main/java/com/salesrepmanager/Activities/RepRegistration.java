package com.salesrepmanager.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.salesrepmanager.R;
import com.salesrepmanager.helperClass.AppUtilityClass;
import com.salesrepmanager.helperClass.SQLiteHandler;
import com.salesrepmanager.helperClass.SessionManager;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RepRegistration extends Activity {
    private Button RegisterButton;
    private EditText Repname, email, phoneNumber, password;
    private Spinner regionSpinner, outletSpinner;
    private SQLiteHandler db;
    private SessionManager session;
    private String[] model, brand, number_available, price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rep_registration);
        initialisation();
        listenerMethods();
        RegionSpinnerMethod();
        UpdateUiOnProductModelChange();
    }

    private void initialisation() {
        model = getResources().getStringArray(R.array.model);
        brand = getResources().getStringArray(R.array.brand);
        number_available = getResources().getStringArray(R.array.number_available);
        price = getResources().getStringArray(R.array.price);
        regionSpinner = (Spinner) findViewById(R.id.regionSpinner);
        outletSpinner = (Spinner) findViewById(R.id.outletSpinner);
        RegisterButton = (Button) findViewById(R.id.registerButton);
        db = new SQLiteHandler(getApplicationContext());
        Repname = (EditText) findViewById(R.id.repName);
        email = (EditText) findViewById(R.id.emailAddress);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        password = (EditText) findViewById(R.id.password);
    }

    private void UpdateUiOnProductModelChange() {

        regionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                outletSpinner.setVisibility(View.VISIBLE);

                List<String> categories = new ArrayList<String>();


                String regionString = regionSpinner.getSelectedItem().toString();
                if (regionString.equalsIgnoreCase("Select Region")) {


                } else if (regionString.equalsIgnoreCase("LAGOS")) {

                    categories.add("SELECT OUTLET");
                    categories.add("Slot Ola-ayeni,Ikeja");
                    categories.add("Biozed,Ikeja");
                    categories.add("Micro station,Ikeja");
                    categories.add("Sloth medical,Ikeja");
                    categories.add("Dr. Phones");
                    categories.add("Pointeck medical");
                    categories.add("Okayfones,Ikeja");
                    categories.add("Ptv,Ikeja");
                    categories.add("Slot Omole");
                    categories.add("Mtn Berger");
                    categories.add("Micro Station, Mushin");
                    categories.add("Mizbeach, Agege");
                    categories.add("X-Right, Agege");
                    categories.add("X-Right Ojodu, Berger");
                    categories.add("Slot, Abule-Egba");
                    categories.add("Slot, Ikorodu");
                    categories.add("Micro Station, Ikorodu");
                    categories.add("Ptv, Ikorodu");
                    categories.add("Slot, Surulere");
                    categories.add("Slot, Apapa");
                    categories.add("Gods Gift Apapa");
                    categories.add("Slot, Marina");

                } else if (regionString.equalsIgnoreCase("SOUTH EAST 1")) {

                    categories.add("SELECT OUTLET");
                    categories.add("Guaranty");
                    categories.add("Fine Brothers");
                    categories.add("Glaceo");
                    categories.add("Obejor");
                    categories.add("Wireless");
                    categories.add("Tidings");


                } else if (regionString.equalsIgnoreCase("SOUTH EAST 2")) {

                    categories.add("SELECT OUTLET");
                    categories.add("Mikings");
                    categories.add("Kashmir");
                    categories.add("Ok ultimate");
                    categories.add("Slot");
                    categories.add("Henoutex");
                    categories.add("7-icon");
                    categories.add("Hybrain");

                } else if (regionString.equalsIgnoreCase("SOUTH WEST")) {

                    categories.add("SELECT OUTLET");
                    categories.add("Micro station");
                    categories.add("Maxwell");
                    categories.add("Solat");
                    categories.add("La place");
                    categories.add("Slot");
                    categories.add("Promotower");
                    categories.add("Plat");
                } else if (regionString.equalsIgnoreCase("SOUTH SOUTH")) {

                    categories.add("SELECT OUTLET");
                    categories.add("Callus miller1");
                    categories.add("Callus miller2");
                    categories.add("Callus miller3");
                    categories.add("Givers");
                    categories.add("Mizbeach");
                    categories.add("Icon mobile");
                    categories.add("JJ Phone");
                } else if (regionString.equalsIgnoreCase("NORTH CENTRAL 1")) {

                    categories.add("SELECT OUTLET");
                    categories.add("Alada Phone");
                    categories.add("Kabs comm");
                    categories.add("Maitangaran");
                    categories.add("Slot");
                    categories.add("Bradam");
                    categories.add("Dancell");
                    categories.add("Deigit phones");
                    categories.add("Btt comm");
                }


                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(RepRegistration.this, R.layout.spinner_layout, R.id.textView, categories);
                dataAdapter.setDropDownViewResource(R.layout.spinner_layout);
                outletSpinner.setAdapter(dataAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void RegionSpinnerMethod() {


        List<String> categories = new ArrayList<String>();
        categories.add("SELECT REGION");
        categories.add("LAGOS");
        categories.add("SOUTH EAST 1");
        categories.add("SOUTH EAST 2");
        categories.add("SOUTH SOUTH");
        categories.add("SOUTH WEST");
        categories.add("NORTH CENTRAL 1");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(RepRegistration.this, R.layout.spinner_layout, R.id.textView, categories);
        dataAdapter.setDropDownViewResource(R.layout.spinner_layout);
        regionSpinner.setAdapter(dataAdapter);

//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(RepRegistration.this, android.R.layout.simple_spinner_item, categories);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        regionSpinner.setAdapter(dataAdapter);
    }

    private void listenerMethods(){
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String repNamee = Repname.getText().toString();
                String emaill = email.getText().toString();
                String phoneNumberr = phoneNumber.getText().toString();
                String passwordd = password.getText().toString();
                String regionn = regionSpinner.getSelectedItem().toString();

                if (!repNamee.isEmpty() && !emaill.isEmpty() && !phoneNumberr.isEmpty() && !passwordd.isEmpty() && outletSpinner.isShown() && !regionn.equalsIgnoreCase("SELECT REGION")) {
                    String outlett = outletSpinner.getSelectedItem().toString();
                    if (!outlett.equalsIgnoreCase("SELECT OUTLET")) {
                        if (AppUtilityClass.validateEmail(emaill)) {
                            db.addUser(repNamee, emaill, phoneNumberr, passwordd, regionn, outlett);
//                            LoadDefaultProductDetailsToDatabase();
                            SessionManager.setLogin(true);
                            Intent intent = new Intent(RepRegistration.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else email.setError("This email is Invalid !");

                    } else
                        Toast.makeText(RepRegistration.this, "Select your Outlet", Toast.LENGTH_SHORT).show();
                } else {
                    if (regionn.equalsIgnoreCase("SELECT REGION"))
                        Toast.makeText(RepRegistration.this, "Select your Region!", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(RepRegistration.this, "Fill all empty field", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
