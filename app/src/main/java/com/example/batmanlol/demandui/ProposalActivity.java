package com.example.batmanlol.demandui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProposalActivity extends AppCompatActivity {
    private FirebaseFirestore db;

    TextView vPropCat, vPropComp, vPropDesc, vPropUpvotes;
    int intUpvotes;
    String companyID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.proposal);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Proposal Details");

        Intent asd = getIntent();
        final String proposalID= asd.getStringExtra("proposalID");
        if(proposalID.equals("")){
            AlertDialog alertDialog = new AlertDialog.Builder(ProposalActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Please Login Again");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
            alertDialog.show();
        }
        //final String proposalID= "y83XAt9pEHIQ1M8ViRCy";

        vPropCat = (TextView)findViewById(R.id.dispPropCat);
        vPropComp = (TextView)findViewById(R.id.dispPropComp);
        vPropDesc = (TextView)findViewById(R.id.dispPropDesc);
        vPropUpvotes = (TextView)findViewById(R.id.dispPropUpvotes);

        DocumentReference docRef = db.collection("proposals").document(proposalID);			//PROPNAME :- need to fetch from onclick()
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    vPropCat.setText(document.get("Category").toString());
                    vPropDesc.setText(document.get("Details").toString());
                    vPropUpvotes.setText(document.get("Upvotes").toString());
                    getCompFloated(document.get("Company Floated").toString());
                    companyID= document.get("Company Floated").toString();
                    intUpvotes= document.getLong("Upvotes").intValue();
                    intUpvotes++;

                    if (document != null) {
                        Log.d(null, "DocumentSnapshot data: " + task.getResult().getData());
                    } else {
                        Log.d(null, "No such Proposal");
                    }
                } else {
                    Log.d(null, "get failed with ", task.getException());
                }
            }
        });


        vPropUpvotes.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view)
                    {

                        DocumentReference upProposal = db.collection("proposals").document(proposalID);
                        upProposal
                                .update("Upvotes", intUpvotes)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        DocumentReference up1Proposal = db.collection("company").document(companyID).collection("proposals").document(proposalID);
                                        up1Proposal
                                                .update("Upvotes", intUpvotes)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("Like Updated", "DocumentSnapshot successfully updated!");
                                                        vPropUpvotes.setText(Integer.toString(intUpvotes));
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

    private void getCompFloated(String companyfloated) {
        DocumentReference docRef = db.collection("company").document(companyfloated);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    vPropComp.setText(document.get("Name").toString());

                    vPropComp.setOnClickListener(
                            new View.OnClickListener() {
                                public void onClick(View view)
                                {

                                    //String name1 = etName1.getText().toString();
                                    Intent activityChangeIntent = new Intent(ProposalActivity.this, CompanyInfoActivity.class);
                                    //Log.d("companyID", companyID);
                                    activityChangeIntent.putExtra("companyID", companyID);
                                    //String name2 = etName2.getText().toString();
                                    //activityChangeIntent.putExtra("Name2", name2);
                                    ProposalActivity.this.startActivity(activityChangeIntent);
                                }
                            }
                    );
                    if (document != null) {
                        Log.d(null, "DocumentSnapshot data: " + task.getResult().getData());
                    } else {
                        Log.d(null, "No such Proposal");
                    }
                } else {
                    Log.d(null, "get failed with ", task.getException());
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        Intent intent = new Intent(ProposalActivity.this, MapsActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProposalActivity.this, MapsActivity.class);
        startActivity(intent);
        finish();
    }
}