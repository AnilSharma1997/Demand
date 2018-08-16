package com.example.batmanlol.demandui;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    EditText ipEmail, ipPassword;
    Button ipLogin;
    String usrCity, usrState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_gradbk);

        mAuth = FirebaseAuth.getInstance();

        TextView signup2 = (TextView)findViewById(R.id.link_signup);
        signup2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
                finish();

                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        ipEmail   = (EditText)findViewById(R.id.input_email);
        ipPassword   = (EditText)findViewById(R.id.input_password);
        ipLogin   = (Button) findViewById(R.id.loginButton);

        ipLogin.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view)
                    {
                        if(!ipEmail.getText().toString().equals("") && !ipPassword.getText().toString().equals("")) {
                            //Log.v("EditText", ipEmail.getText().toString());
                            actLogin();
                        }
                        else{
                            AlertDialog alertDialog = new AlertDialog.Builder(Login.this).create();
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
                    }
                });

        /*
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInOptions mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        */
    }


    private void actLogin(){
        mAuth.signInWithEmailAndPassword(ipEmail.getText().toString(), ipPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("OK", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);

                            //getCityState(user.getEmail());
                            Intent activityChangeIntent = new Intent(Login.this, MapsActivity.class);          //homeActivity
                            //activityChangeIntent.putExtra("usrCity", usrCity);
                            //activityChangeIntent.putExtra("usrState", usrState);
                            Login.this.startActivity(activityChangeIntent);
                            finish();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Problem", "signInWithEmail:failure", task.getException());
                            AlertDialog alertDialog = new AlertDialog.Builder(Login.this, R.style.AppCompatAlertDialogStyle).create();
                            alertDialog.setTitle("Alert");
                            alertDialog.setMessage("Login Failed");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                            /*
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                             */
                            //updateUI(null);
                        }
                        // ...
                    }
                });
    }


    /*
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    if (user != null) {
        // Name, email address, and profile photo Url
        String name = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();

        // Check if user's email is verified
        boolean emailVerified = user.isEmailVerified();

        // The user's ID, unique to the Firebase project. Do NOT use this value to
        // authenticate with your backend server, if you have one. Use
        // FirebaseUser.getToken() instead.
        String uid = user.getUid();
    }
    */

}