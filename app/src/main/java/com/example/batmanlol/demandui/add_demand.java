package com.example.batmanlol.demandui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class add_demand extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    String[] SPINNERLIST = {"SELECT CATEGORY","Gas Station", "Cafe", "ATM", "Convenience Store", "Hotel", "Restaurant", "Clinic", "Movies", "Stationery", "Mall", "Barber"};

    EditText ipDescription;
    Button addDmd;
    Spinner catSpinner;
    String ipCategory, userEmail;
    String demandLatitude="";
    String demandLongitude="";
    Map<String, Object> newRequest = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        userEmail= currentUser.getEmail();

        if(userEmail== null){
            AlertDialog alertDialog = new AlertDialog.Builder(add_demand.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Please Login Again");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(add_demand.this, Login.class);
                            startActivity(intent);
                            finish();
                        }
                    });
            alertDialog.show();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_demand);
        /*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        */
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        //userEmail= currentUser.getEmail();
        //Log.d("User Email", userEmail);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(add_demand.this,
                android.R.layout.simple_dropdown_item_1line, SPINNERLIST){
            //Important these methods are overide for adding a placeholder, adding textcolor,
            // !!!!!!!!!!! Don't Change these ovverider methods.

            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }


            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        Spinner materialDesignSpinner = (Spinner)
                findViewById(R.id.prodCat);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        materialDesignSpinner.setAdapter(arrayAdapter);

        //IntentHere
        Intent asd = getIntent();
        demandLatitude= asd.getStringExtra("demandLatitude");
        demandLongitude= asd.getStringExtra("demandLongitude");

        if(demandLatitude.equals("") || demandLongitude.equals("")){
                AlertDialog alertDialog = new AlertDialog.Builder(add_demand.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Pin Not Found");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();;
                                Intent intent = new Intent(add_demand.this, MapsActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                alertDialog.show();
        }

        ipDescription = (EditText)findViewById(R.id.description);
        catSpinner = (Spinner)findViewById(R.id.prodCat);
        addDmd = (Button)findViewById(R.id.submitAddDmd);

        catSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                ipCategory = parent.getItemAtPosition(pos).toString();
                Log.d("Category", ipCategory);
            }
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("Category", "Nothing Selected");
            }
        });

        addDmd.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view)
                    {
                        if(ipDescription== null || ipCategory.equals("SELECT CATEGORY")){
                            AlertDialog alertDialog = new AlertDialog.Builder(add_demand.this).create();
                            alertDialog.setTitle("Alert");
                            alertDialog.setMessage("Empty Field(s)");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                        else {
                            addDemanddb();
                        }
                    }
                });
    }

    private void addDemanddb() {
        newRequest.put("Details", ipDescription.getText().toString());
        //newRequest.put("RequestID", ourVariableRequestID);              //manage this
        newRequest.put("Category", ipCategory);
        newRequest.put("Upvotes", 0);
        newRequest.put("User Floated", userEmail);
        newRequest.put("Latitude", demandLatitude);
        newRequest.put("Longitude", demandLongitude);
        newRequest.put("Timestamp", FieldValue.serverTimestamp());


        addToRequestCollection();
        //part2(demandID);

    }

    private void addToRequestCollection() {
        //String demandID= "yo";
        db.collection("requests")//.document(ourVariableReqID)				//HAS TO be UNIQUE
                .add(newRequest)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Demand to DB", "DocumentSnapshot successfully added!");
                        //        demandID= documentReference.getId();
                        getDemand(documentReference.getId());
                        //              Log.d("DocID", demandID);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Demand To DB Failed", "Error adding document", e);
                    }
                });
        //return demandID;
    }

    private void createReferenceToUser(String demandID) {
        Map<String, Object> tryRequest = new HashMap<>();
        tryRequest.put("RequestID", demandID);
        tryRequest.put("Upvotes", 0);
        tryRequest.put("Category", ipCategory);
        tryRequest.put("Timestamp", FieldValue.serverTimestamp());

        //Log.d("See here", demandID);
        DocumentReference requestRef = db.collection("user").document(userEmail)
                .collection("requests").document(demandID);
        requestRef.set(tryRequest)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Link to User", "DocumentSnapshot successfully written!");
                        Intent intent = new Intent(add_demand.this, MapsActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Link to User Failed", "Error writing document", e);
                    }
                });

    }

    private void getDemand(String id) {

        createReferenceToUser(id);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(add_demand.this, MapsActivity.class);
        startActivity(intent);
        finish();
    }

}