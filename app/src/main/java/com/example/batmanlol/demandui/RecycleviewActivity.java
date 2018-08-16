package com.example.batmanlol.demandui;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class RecycleviewActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    //List<Mydemand_model> dataList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycleview);

        //FirebaseFirestore db = FirebaseFirestore.getInstance();
        //dataList =  new ArrayList<>();

        /*
        db.collection("user").document(currentUser.getEmail()).collection("requests")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                //n++;

                                Log.d(TAG, document.getId() + " => " + document.getData());
                                //Log.d("OurName", document.get("Name").toString());
                                //Dates[i]= document.get("Timestamp").toString();
                                //upvote[i]= document.get("Upvotes").toString();
                                //assignDocument(document);
                                Log.d("YO","success");
                                Mydemand_model demand = new Mydemand_model();
                                demand.setDemandTitle(document.get("RequestID").toString());
                                //System.out.print(document.get("Timestamp").toString());
                                demand.setDemandDate(document.get("Timestamp").toString());
                                demand.setUpvotes(document.get("Upvotes").toString());
                                dataList.add(demand);
                                //Log.d("sizeofdataList2",dataList.size()+"");
                                //lol(document.get("RequestID").toString(), document.get("Timestamp").toString(), document.get("Upvotes").toString());
                            }
                        } else {
                            Log.d(TAG, "Error getting requests: ", task.getException());
                        }
                    }
                });

        */

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        Mydemand_model.getObjectList(currentUser, this, recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Demands");


    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
