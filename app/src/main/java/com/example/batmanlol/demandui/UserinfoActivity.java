package com.example.batmanlol.demandui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class UserinfoActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    TextView vUserName, vEmail, vCity, vState;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinfo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("User Details");

        vUserName = (TextView)findViewById(R.id.dispUserName);
        vEmail = (TextView)findViewById(R.id.dispUserEmailID);
        vCity = (TextView)findViewById(R.id.dispUserCity);
        vState = (TextView)findViewById(R.id.dispUserState);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        userEmail= currentUser.getEmail();

        if(userEmail== null){
            AlertDialog alertDialog = new AlertDialog.Builder(UserinfoActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Please Login Again");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(UserinfoActivity.this, Login.class);
                            startActivity(intent);
                            finish();
                        }
                    });
            alertDialog.show();
        }

        DocumentReference docRef = db.collection("user").document(userEmail);			//USERNAME :- need to fetch from auth
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    vUserName.setText(document.get("Name").toString());
                    vEmail.setText(userEmail);
                    vCity.setText(document.get("Address.City").toString());
                    vState.setText(document.get("Address.State").toString());

                    if (document != null) {
                        Log.d(null, "DocumentSnapshot data: " + task.getResult().getData());
                    } else {
                        Log.d(null, "User not found");
                    }
                } else {
                    Log.d(null, "get failed with ", task.getException());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        //Intent intent = new Intent(UserinfoActivity.this, MapsActivity.class);
        //startActivity(intent);
        finish();
    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


}
