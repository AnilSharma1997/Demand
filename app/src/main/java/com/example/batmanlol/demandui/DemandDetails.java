package com.example.batmanlol.demandui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;


public class DemandDetails extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    TextView vCategory, vDescription, vUpvotes;
    int intUpvotes;
    String userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.deman_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Demand Details");

        Intent asd = getIntent();
        final String demandID= asd.getStringExtra("demandID");
        //final String demandID= "aNmhsOsr4WCXcc4y4cMO";
        //Log.d("Gotid", demandID);
        if(demandID.equals("")){
            AlertDialog alertDialog = new AlertDialog.Builder(DemandDetails.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Please Login Again");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(DemandDetails.this, Login.class);
                            startActivity(intent);
                            finish();
                        }
                    });
            alertDialog.show();
        }

        vCategory = (TextView)findViewById(R.id.dispCategory);
        vDescription = (TextView)findViewById(R.id.dispDescription);
        vUpvotes = (TextView)findViewById(R.id.dispUpvoteCount);

        DocumentReference docRef = db.collection("requests").document(demandID);			//REQNAME :- need to fetch from onclick()
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    vCategory.setText(document.get("Category").toString());
                    vDescription.setText(document.get("Details").toString());
                    vUpvotes.setText(document.get("Upvotes").toString());
                    userid= document.get("User Floated").toString();
                    intUpvotes= document.getLong("Upvotes").intValue();
                    intUpvotes++;

                    if (document != null) {
                        Log.d("Fetched Request", "DocumentSnapshot data: " + task.getResult().getData());
                    } else {
                        Log.d("Null Doc", "No such Proposal");
                    }
                } else {
                    Log.d("Fetch Failed", "get failed with ", task.getException());
                }
            }
        });

        vUpvotes.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view)
                    {
                        DocumentReference upDemand = db.collection("requests").document(demandID);
                        upDemand
                                .update("Upvotes", intUpvotes)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //Log.d("Like Updated", "DocumentSnapshot successfully updated!");
                                        DocumentReference upDemand = db.collection("user").document(userid).collection("requests").document(demandID);
                                        upDemand
                                                .update("Upvotes", intUpvotes)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("Like Updated", "DocumentSnapshot successfully updated!");
                                                        vUpvotes.setText(Integer.toString(intUpvotes));
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w("Like Not Updated", "Error updating document", e);
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("Like Not Updated", "Error updating document", e);
                                    }
                                });
                    }
                }
        );
    }

    @Override
    public boolean onSupportNavigateUp(){
        Intent intent = new Intent(DemandDetails.this, MapsActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DemandDetails.this, MapsActivity.class);
        startActivity(intent);
        finish();
    }

}