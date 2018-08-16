package com.example.batmanlol.demandui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CompanyInfoActivity extends AppCompatActivity {
    private FirebaseFirestore db;

    TextView dCompName, dCompCategory, dCompContact, dCompAddress, dCompEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Company Details");

        Intent asd = getIntent();
        final String companyID= asd.getStringExtra("companyID");
        //Log.d("GOTcompanyID", companyID);
        //final String companyID= "ICICI";

        if(companyID.equals("")){
                AlertDialog alertDialog = new AlertDialog.Builder(CompanyInfoActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Could not get details");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                alertDialog.show();
        }
        dCompName = (TextView)findViewById(R.id.dispCompName);
        //dCompCategory = (TextView)findViewById(R.id.dispComp);
        dCompContact = (TextView)findViewById(R.id.dispCompContact);
        dCompAddress = (TextView)findViewById(R.id.dispCompAddress);
        dCompEmail = (TextView)findViewById(R.id.dispCompEmail);

        DocumentReference docRef = db.collection("company").document(companyID);			//USERNAME :- need to fetch from auth
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    dCompName.setText(document.get("Name").toString());
                    dCompEmail.setText(companyID);
                    dCompContact.setText(document.get("Contact").toString());
                    String addressNow="";
                    addressNow= addressNow+" "+ document.get("Address.Street").toString();
                    addressNow= addressNow+" "+ document.get("Address.City").toString();
                    addressNow= addressNow+" "+ document.get("Address.State").toString();
                    addressNow= addressNow+" "+ document.get("Address.PIN").toString();
                    dCompAddress.setText(addressNow);

                    if (document != null) {
                        Log.d("Data Fetched", "DocumentSnapshot data: " + task.getResult().getData());
                    } else {
                        Log.d("No Data", "User not found");
                    }
                } else {
                    Log.d("Data Fetch Failed", "get failed with ", task.getException());
                }
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
