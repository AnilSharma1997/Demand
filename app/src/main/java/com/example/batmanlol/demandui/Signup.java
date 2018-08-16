package com.example.batmanlol.demandui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    //private String email, password;
    String[] SPINNERLIST = {"SELECT STATE","Andhra Pradesh","Arunachal Pradesh","Assam","Bihar","Chhattisgarh","Dadra and Nagar Haveli","Daman and Diu","Delhi","Goa","Gujarat","Haryana","Himachal Pradesh","Jammu and Kashmir","Jharkhand","Karnataka",
            "Kerala","Madhya Pradesh","Maharashtra","Manipur","Meghalaya","Mizoram","Nagaland","Orissa","Puducherry","Punjab", "Rajasthan","Sikkim","Tamil Nadu",
            "Telangana","Tripura","Uttar Pradesh","Uttarakhand","West Bengal"};

    EditText ipName, ipEmail, ipPassword, ipConfirmPassword, ipCity;
    String ipState="";
    Button createAccountButton;
    Spinner spinnerTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        // Giving array in spinner for STATE

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Signup.this,
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
                findViewById(R.id.android_material_design_spinner);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        materialDesignSpinner.setAdapter(arrayAdapter);


        //Going back to login
        TextView login = (TextView)findViewById(R.id.link_login);

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });


        ipName   = (EditText)findViewById(R.id.input_name);
        ipEmail   = (EditText)findViewById(R.id.input_email);
        ipPassword   = (EditText)findViewById(R.id.input_password);
        ipConfirmPassword   = (EditText)findViewById(R.id.input_confirmPassword);
        ipCity   = (EditText)findViewById(R.id.input_city);
        createAccountButton   = (Button)findViewById(R.id.create_account_button);
        spinnerTest = (Spinner)findViewById(R.id.android_material_design_spinner);

        spinnerTest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                ipState = parent.getItemAtPosition(pos).toString();
                //Log.d("ipState", ipState);
            }
            public void onNothingSelected(AdapterView<?> parent) {
                //Log.d("ipState", "Nothing Selected");
            }
        });
//        ipState = spinnerTest.getSelectedItem().toString();

        createAccountButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view)
                    {
                        //Log.v("EditText", ipName.getText().toString());
                        if(ipName.getText().toString().equals("") || ipEmail.getText().toString().equals("") || ipPassword.getText().toString().equals("") || ipConfirmPassword.getText().toString().equals("") || ipCity.getText().toString().equals("")){
                            AlertDialog alertDialog = new AlertDialog.Builder(Signup.this).create();
                            alertDialog.setTitle("Alert");
                            alertDialog.setMessage("Empty Fields");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                        if(!ipPassword.getText().toString().equals(ipConfirmPassword.getText().toString())){
                            AlertDialog alertDialog = new AlertDialog.Builder(Signup.this).create();
                            alertDialog.setTitle("Alert");
                            alertDialog.setMessage("Password Mismatch");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                        if(ipState.equals("SELECT STATE")){
                            AlertDialog alertDialog = new AlertDialog.Builder(Signup.this).create();
                            alertDialog.setTitle("Alert");
                            alertDialog.setMessage("Please Select State");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                        else {
                            createAccount();
                        }
                    }
                });
    }

    private void addToDatabase(){
        Map<String, Object> newCustomer = new HashMap<>();
        newCustomer.put("Name", ipName.getText().toString());					//this is NOT username

        Map<String, Object> nestedAddress = new HashMap<>();
        nestedAddress.put("City", ipCity.getText().toString());
        nestedAddress.put("State", ipState);
        newCustomer.put("Address", nestedAddress);

        String stripEmail = ipEmail.getText().toString();
        db.collection("user").document(stripEmail)
                .set(newCustomer)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Added to DB", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Failed to DB", "Error writing document", e);

                        AlertDialog alertDialog = new AlertDialog.Builder(Signup.this).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage("Signup Failed Could Not Add to Database");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                });
    }

    private void createAccount() {
        mAuth.createUserWithEmailAndPassword(ipEmail.getText().toString(), ipPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            addToDatabase();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Create Account Pass", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Create Account Fail", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Signup.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
}