package com.example.batmanlol.demandui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText newPass, oldPass, confPass;
    Button changePass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pass);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Change Password");

        final FirebaseUser currentUser = mAuth.getCurrentUser();

        Log.d("ChangeP", currentUser.getEmail());
        newPass= (EditText)findViewById(R.id.new_pass);
        oldPass= (EditText)findViewById(R.id.old_pass);
        confPass= (EditText)findViewById(R.id.conf_pass);
        changePass= (Button)findViewById(R.id.trigChange);


        changePass.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              if(!newPass.getText().toString().equals(confPass.getText().toString())){
                                                  AlertDialog alertDialog = new AlertDialog.Builder(ChangePassActivity.this).create();
                                                  alertDialog.setTitle("Alert");
                                                  alertDialog.setMessage("Password Mismatch");
                                                  alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                          new DialogInterface.OnClickListener() {
                                                              public void onClick(DialogInterface dialog, int which) {
                                                                  dialog.dismiss();
                                                              }
                                                          });
                                                  alertDialog.show();
                                              }else {
                                                  AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), oldPass.getText().toString());

                                                  currentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                      @Override
                                                      public void onComplete(@NonNull Task<Void> task) {
                                                          if(task.isSuccessful()){
                                                              currentUser.updatePassword(newPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                  @Override
                                                                  public void onComplete(@NonNull Task<Void> task) {
                                                                      if(!task.isSuccessful()){
                                                                          AlertDialog alertDialog = new AlertDialog.Builder(ChangePassActivity.this).create();
                                                                          alertDialog.setTitle("Alert");
                                                                          alertDialog.setMessage("Couldn't Change Password");
                                                                          alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                                                  new DialogInterface.OnClickListener() {
                                                                                      public void onClick(DialogInterface dialog, int which) {
                                                                                          dialog.dismiss();
                                                                                      }
                                                                                  });
                                                                          alertDialog.show();
                                                                      }else {
                                                                          AlertDialog alertDialog = new AlertDialog.Builder(ChangePassActivity.this).create();
                                                                          alertDialog.setTitle("Alert");
                                                                          alertDialog.setMessage("Change Password Successful");
                                                                          alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                                                  new DialogInterface.OnClickListener() {
                                                                                      public void onClick(DialogInterface dialog, int which) {
                                                                                          dialog.dismiss();
                                                                                          FirebaseAuth.getInstance().signOut();
                                                                                          Intent intent = new Intent(ChangePassActivity.this, Login.class);
                                                                                          startActivity(intent);
                                                                                          finish();
                                                                                      }
                                                                                  });
                                                                          alertDialog.show();
                                                                      }
                                                                  }
                                                              });
                                                          }else {
                                                              AlertDialog alertDialog = new AlertDialog.Builder(ChangePassActivity.this).create();
                                                              alertDialog.setTitle("Alert");
                                                              alertDialog.setMessage("Couldn't Find User");
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
                                              }
                                          }
                                      }
        );

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
