package com.example.batmanlol.demandui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class Mydemand_model {

    private String DemandTitle;
    private String DemandDate;
    private String upvotes;
    private String DemandId;
    public static ArrayList<Mydemand_model> dataList;
    private static ArrayList<String> title = new ArrayList<String>();;
    private static ArrayList<String> date;
    private static ArrayList<String> upvo;
    //TextView vCategory, vDescription, vUpvotes;
    //static int n=0;
    //static String title[];//= new String[n];
    //static String Dates[];//= new String[n];
    //static String upvote[];//= new String[n];

    public void fetchData(){
    }

    public String getDemandTitle(){

        return DemandTitle;
    }

    public String getDemandId(){

        return DemandId;
    }

    public void setDemandTitle(String Title){

        this.DemandTitle = Title;

    }

    public void setDemandId(String Id){

        this.DemandId = Id;

    }

    public String getDemandDate(){

        return DemandDate;
    }

    public void setDemandDate(String Date){

        this.DemandDate = Date;

    }

    public String getUpvotes(){

        return upvotes;
    }


    public void setUpvotes(String upvotes){

        this.upvotes = upvotes;

    }

    public static String[] getArrayTitle(FirebaseUser currentUser) {


        ArrayList<String> title=new ArrayList<String>();


        String[] Titles = {"Garmentsfsdfdsfdsfdsfdsfdsfdsf", "Bar", "Restaurant"};

        return Titles;


    }


    public static String[] getArrayDates() {

        //String[] Dates= globalDate;
        String[] Dates = {"12/7/17", "12/10/17", "12/12/17"};


        return Dates;
    }

    public static String[] getArrayupvote() {

        String[] upvote = {"14", "16", "17"};

        return upvote;
    }


    public static List<Mydemand_model> getObjectList(FirebaseUser currentUser, final Context context, final RecyclerView recyclerView) {

        dataList = new ArrayList<>();

        date = new ArrayList<String>();
        upvo = new ArrayList<String>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //String userEmail= getUserEmail();
        String email= currentUser.getEmail();
        Log.d("GOTemail", email);


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
                                demand.setDemandTitle(document.get("Category").toString()); //Put Category
                                demand.setDemandId(document.get("RequestID").toString());
                                //System.out.print(document.get("Timestamp").toString());
                                String s = document.get("Timestamp").toString();
                                String p = s.substring(4,10);
                                String q = s.substring(29);
                                demand.setDemandDate(p+","+q);
                                demand.setUpvotes(document.get("Upvotes").toString());
                                Log.d("LodaUpvote", document.get("Upvotes").toString());
                                dataList.add(demand);
                                RecyclerAdapter adapter = new RecyclerAdapter(context, dataList);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                //Log.d("sizeofdataList2",dataList.size()+"");
                                }
                        } else {
                            Log.d(TAG, "Error getting requests: ", task.getException());
                        }

                    }
                });
        return dataList;

    }

}