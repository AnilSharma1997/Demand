package com.example.batmanlol.demandui;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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

public class MapsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnInfoWindowClickListener {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private GoogleMap mMap;
    private Marker addMarker;
    private EditText searchText;
    ImageView imgUser;
    TextView usrEmail;
    String usrCity, usrState;
    String uEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        final FirebaseUser currentUser= mAuth.getCurrentUser();
        //Log.d("GotEmail", currentUser.getEmail());

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d("seeCordLat", String.valueOf(addMarker.getPosition().latitude));
                //Log.d("seeCordLong", String.valueOf(addMarker.getPosition().longitude));
                Intent intent = new Intent(MapsActivity.this, add_demand.class);

                if (addMarker == null) {
                    AlertDialog alertDialog = new AlertDialog.Builder(MapsActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("No Point Selected");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else {
                    intent.putExtra("demandLatitude", String.valueOf(addMarker.getPosition().latitude));
                    intent.putExtra("demandLongitude", String.valueOf(addMarker.getPosition().longitude));
                    startActivity(intent);
                    finish();
                }
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                  //      .setAction("Action", null).show();

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //toolbar.setNavigationIcon(R.drawable.ic_view_list_black_24dp);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setupSearch();
        uEmail=currentUser.getEmail();

        //Start of MAP Part
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //End of MAP Part

        View headerView = navigationView.getHeaderView(0);

        imgUser = (ImageView) headerView.findViewById(R.id.igView);
        usrEmail = (TextView) headerView.findViewById(R.id.username_shown);

        usrEmail.setText(currentUser.getEmail());
        Log.d("OurUser", usrEmail.getText().toString());

        usrEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(currentUser.getEmail()== null){
                    AlertDialog alertDialog = new AlertDialog.Builder(MapsActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Please Login Again");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    FirebaseAuth.getInstance().signOut();
                                    Intent intent = new Intent(MapsActivity.this, Login.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                    alertDialog.show();
                }
                else{
                    Intent intent = new Intent(MapsActivity.this, UserinfoActivity.class);
                    startActivity(intent);
                }
            }
        });


        imgUser.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentUser.getEmail()== null){
                    AlertDialog alertDialog = new AlertDialog.Builder(MapsActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Please Login Again");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    FirebaseAuth.getInstance().signOut();
                                    Intent intent = new Intent(MapsActivity.this, Login.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                    alertDialog.show();
                }
                else{
                    Intent intent = new Intent(MapsActivity.this, UserinfoActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    //Navigation Drawer Options Handler

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_request) {
            // Handle the My request action
            Intent intent = new Intent(MapsActivity.this, RecycleviewActivity.class);
            startActivity(intent);
        } else if(id == R.id.nav_change_password) {
            Intent intent = new Intent(MapsActivity.this, ChangePassActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            // Handle Logout action
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MapsActivity.this, Login.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    // Function for opening Alert Dialogbox

    public void OpenAlert(View v){

        String[] Category = {"Gas Station", "Cafe", "ATM", "Convenience Store", "Hotel", "Restaurant", "Clinic", "Movies", "Stationery", "Mall", "Barber"};

        final ArrayList mSelectedItems = new ArrayList();  // Where we track the selected items
        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this, R.style.AppCompatAlertDialogStyle);

        // Set the dialog title
        builder.setTitle("Choose Category")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(Category, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {

                                    // If the user checked the item, add it to the selected items
                                    mSelectedItems.add(which);
                                } else if (mSelectedItems.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    mSelectedItems.remove(Integer.valueOf(which));
                                }
                            }
                        })
                // Set the action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        builder.show();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        UiSettings mUiSettings = mMap.getUiSettings();
        mUiSettings.setMapToolbarEnabled(false);

        getCityState(uEmail);
        /*
        String place = usrCity+", "+usrState;               //"Gandhinagar, Gujarat";
        Log.d("OurPlace", place);
        LatLng focusLoc;
        Geocoder mGeoCoder = new Geocoder(this);
        try {
            List<Address> mAddress = mGeoCoder.getFromLocationName(place, 1);

            for(Address a : mAddress){
                if(a.hasLatitude() && a.hasLongitude()) {
                    focusLoc = new LatLng(a.getLatitude(), a.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(focusLoc, 12.5f));
                }
            }

        } catch (Exception e) {
            AlertDialog alertDialog = new AlertDialog.Builder(MapsActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Poor Internet Connection");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(12.5, 12.5), 12.5f));
        }
*/
        //Sample Marker for testing

        fetchDemands();
        fetchProposals();
        //Proposals
        //add marker by category

        mMap.setOnMapClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
    }

    private void fetchDemands() {
        db.collection("requests")					//USERNAME :- need to fetch from auth
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                //Log.d("All Requests Fetched", document.getId() + " => " + document.getData());
                                String categoryName= document.get("Category").toString();
                                String latitude= document.get("Latitude").toString();
                                double lat= Double.parseDouble(latitude);
                                String longitude= document.get("Longitude").toString();
                                double lon= Double.parseDouble(longitude);
                                String upvotes= document.get("Upvotes").toString();
                                if(categoryName.equals("") || latitude.equals("") || longitude.equals("") || upvotes.equals("")){
                                    AlertDialog alertDialog = new AlertDialog.Builder(MapsActivity.this).create();
                                    alertDialog.setTitle("Alert");
                                    alertDialog.setMessage("Invalid Marker");
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    return;
                                                }
                                            });
                                    alertDialog.show();
                                }
                                Marker newMarker;

                                if(categoryName.equals("Gas Station")) {
                                    markerDetails ourMarker = new markerDetails(document.getId(), true);
                                    newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("Type: "+ categoryName).snippet("Upvotes: " + upvotes).icon(BitmapDescriptorFactory.fromResource(R.drawable.gas_station_demand)));
                                    newMarker.setTag(ourMarker);
                                } else if(categoryName.equals("Cafe")) {
                                    markerDetails ourMarker = new markerDetails(document.getId(), true);
                                    newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("Type: "+ categoryName).snippet("Upvotes: " + upvotes).icon(BitmapDescriptorFactory.fromResource(R.drawable.coffee_demand)));
                                    newMarker.setTag(ourMarker);
                                } else if(categoryName.equals("ATM")) {
                                    markerDetails ourMarker = new markerDetails(document.getId(), true);
                                    newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("Type: "+ categoryName).snippet("Upvotes: " + upvotes).icon(BitmapDescriptorFactory.fromResource(R.drawable.atm_demand)));
                                    newMarker.setTag(ourMarker);
                                } else if(categoryName.equals("Convenience Store")) {
                                    markerDetails ourMarker = new markerDetails(document.getId(), true);
                                    newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("Type: "+ categoryName).snippet("Upvotes: " + upvotes).icon(BitmapDescriptorFactory.fromResource(R.drawable.convenience_store_demand)));
                                    newMarker.setTag(ourMarker);
                                } else if(categoryName.equals("Hotel")) {
                                    markerDetails ourMarker = new markerDetails(document.getId(), true);
                                    newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("Type: "+ categoryName).snippet("Upvotes: " + upvotes).icon(BitmapDescriptorFactory.fromResource(R.drawable.hotel_demand)));
                                    newMarker.setTag(ourMarker);
                                } else if(categoryName.equals("Restaurant")) {
                                    markerDetails ourMarker = new markerDetails(document.getId(), true);
                                    newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("Type: "+ categoryName).snippet("Upvotes: " + upvotes).icon(BitmapDescriptorFactory.fromResource(R.drawable.restaurant_demand)));
                                    newMarker.setTag(ourMarker);
                                } else if(categoryName.equals("Clinic")) {
                                    markerDetails ourMarker = new markerDetails(document.getId(), true);
                                    newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("Type: "+ categoryName).snippet("Upvotes: " + upvotes).icon(BitmapDescriptorFactory.fromResource(R.drawable.hospital_demand)));
                                    newMarker.setTag(ourMarker);
                                } else if(categoryName.equals("Movies")) {
                                    markerDetails ourMarker = new markerDetails(document.getId(), true);
                                    newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("Type: "+ categoryName).snippet("Upvotes: " + upvotes).icon(BitmapDescriptorFactory.fromResource(R.drawable.cinema_demand)));
                                    newMarker.setTag(ourMarker);
                                } else if(categoryName.equals("Stationery")) {
                                    markerDetails ourMarker = new markerDetails(document.getId(), true);
                                    newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("Type: "+ categoryName).snippet("Upvotes: " + upvotes).icon(BitmapDescriptorFactory.fromResource(R.drawable.stationery_demand)));
                                    newMarker.setTag(ourMarker);
                                } else if(categoryName.equals("Mall")) {
                                    markerDetails ourMarker = new markerDetails(document.getId(), true);
                                    newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("Type: "+ categoryName).snippet("Upvotes: " + upvotes).icon(BitmapDescriptorFactory.fromResource(R.drawable.mall_demand)));
                                    newMarker.setTag(ourMarker);
                                } else if(categoryName.equals("Barber")) {
                                    markerDetails ourMarker = new markerDetails(document.getId(), true);
                                    newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("Type: "+ categoryName).snippet("Upvotes: " + upvotes).icon(BitmapDescriptorFactory.fromResource(R.drawable.barber_demand)));
                                    newMarker.setTag(ourMarker);
                                } else {
                                    //should never appear in actual map
                                    mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("Warning Marker").snippet("0"));
                                }

                            }
                        } else {
                            Log.d("Requests Fetch Failed", "Error getting requests: ", task.getException());
                        }
                    }
                });

    }

    private void fetchProposals() {

        db.collection("proposals")					//USERNAME :- need to fetch from auth
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d("All Requests Fetched", document.getId() + " => " + document.getData());
                                String categoryName= document.get("Category").toString();
                                String latitude= document.get("Latitude").toString();
                                double lat= Double.parseDouble(latitude);
                                String longitude= document.get("Longitude").toString();
                                double lon= Double.parseDouble(longitude);
                                String upvotes= document.get("Upvotes").toString();
                                if(categoryName.equals("") || latitude.equals("") || longitude.equals("") || upvotes.equals("")){
                                    AlertDialog alertDialog = new AlertDialog.Builder(MapsActivity.this).create();
                                    alertDialog.setTitle("Alert");
                                    alertDialog.setMessage("Invalid Marker");
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    return;
                                                }
                                            });
                                    alertDialog.show();
                                }

                                Marker newMarker;

                                if(categoryName.equals("Gas Station")) {
                                    markerDetails ourMarker = new markerDetails(document.getId(), false);
                                    newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("Type: "+ categoryName).snippet("Upvotes: " + upvotes).icon(BitmapDescriptorFactory.fromResource(R.drawable.gas_station_proposal)));
                                    newMarker.setTag(ourMarker);
                                } else if(categoryName.equals("Cafe")) {
                                    markerDetails ourMarker = new markerDetails(document.getId(), false);
                                    newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("Type: "+ categoryName).snippet("Upvotes: " + upvotes).icon(BitmapDescriptorFactory.fromResource(R.drawable.coffee_proposal)));
                                    newMarker.setTag(ourMarker);
                                } else if(categoryName.equals("ATM")) {
                                    markerDetails ourMarker = new markerDetails(document.getId(), false);
                                    newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("Type: "+ categoryName).snippet("Upvotes: " + upvotes).icon(BitmapDescriptorFactory.fromResource(R.drawable.atm_proposal)));
                                    newMarker.setTag(ourMarker);
                                } else if(categoryName.equals("Convenience Store")) {
                                    markerDetails ourMarker = new markerDetails(document.getId(), false);
                                    newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("Type: "+ categoryName).snippet("Upvotes: " + upvotes).icon(BitmapDescriptorFactory.fromResource(R.drawable.convenience_store_proposal)));
                                    newMarker.setTag(ourMarker);
                                } else if(categoryName.equals("Hotel")) {
                                    markerDetails ourMarker = new markerDetails(document.getId(), false);
                                    newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("Type: "+ categoryName).snippet("Upvotes: " + upvotes).icon(BitmapDescriptorFactory.fromResource(R.drawable.hotel_proposal)));
                                    newMarker.setTag(ourMarker);
                                } else if(categoryName.equals("Restaurant")) {
                                    markerDetails ourMarker = new markerDetails(document.getId(), false);
                                    newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("Type: "+ categoryName).snippet("Upvotes: " + upvotes).icon(BitmapDescriptorFactory.fromResource(R.drawable.restaurant_proposal)));
                                    newMarker.setTag(ourMarker);
                                } else if(categoryName.equals("Clinic")) {
                                    markerDetails ourMarker = new markerDetails(document.getId(), false);
                                    newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("Type: "+ categoryName).snippet("Upvotes: " + upvotes).icon(BitmapDescriptorFactory.fromResource(R.drawable.hospital_proposal)));
                                    newMarker.setTag(ourMarker);
                                } else if(categoryName.equals("Movies")) {
                                    markerDetails ourMarker = new markerDetails(document.getId(), false);
                                    newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("Type: "+ categoryName).snippet("Upvotes: " + upvotes).icon(BitmapDescriptorFactory.fromResource(R.drawable.cinema_proposal)));
                                    newMarker.setTag(ourMarker);
                                } else if(categoryName.equals("Stationery")) {
                                    markerDetails ourMarker = new markerDetails(document.getId(), false);
                                    newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("Type: "+ categoryName).snippet("Upvotes: " + upvotes).icon(BitmapDescriptorFactory.fromResource(R.drawable.stationery_proposal)));
                                    newMarker.setTag(ourMarker);
                                } else if(categoryName.equals("Mall")) {
                                    markerDetails ourMarker = new markerDetails(document.getId(), false);
                                    newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("Type: "+ categoryName).snippet("Upvotes: " + upvotes).icon(BitmapDescriptorFactory.fromResource(R.drawable.mall_proposal)));
                                    newMarker.setTag(ourMarker);
                                } else if(categoryName.equals("Barber")) {
                                    markerDetails ourMarker = new markerDetails(document.getId(), false);
                                    newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("Type: "+ categoryName).snippet("Upvotes: " + upvotes).icon(BitmapDescriptorFactory.fromResource(R.drawable.barber_proposal)));
                                    newMarker.setTag(ourMarker);
                                } else {
                                    //should never appear in actual map
                                    mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("Warning Marker").snippet("0"));
                                }

                            }
                        } else {
                            Log.d("Requests Fetch Failed", "Error getting requests: ", task.getException());
                        }
                    }
                });

    }

    private void setupSearch() {

        searchText = (EditText) findViewById(R.id.search_text);
        ImageButton searchButton = (ImageButton) findViewById(R.id.send);

        searchButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("button_click", "Here");
                LatLng sLoc;
                Geocoder sGeoCoder = new Geocoder(MapsActivity.this);
                try {
                    List<Address> sAddress = sGeoCoder.getFromLocationName(searchText.getText().toString(), 1);
                    Log.d("Results: ", ""+sAddress.size());
                    for(Address a : sAddress){
                        if(a.hasLatitude() && a.hasLongitude()){
                            sLoc = new LatLng(a.getLatitude(), a.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sLoc, 15f));
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MapsActivity.this, "Place not found",
                            Toast.LENGTH_SHORT).show();
                    //TODO: show message that internet is not connected
                    //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(12.5, 12.5), 12.5f));
                }
            }
        });
    }

    @Override
    public void onMapClick(LatLng point) {

        Marker newMarker = mMap.addMarker(new MarkerOptions().position(point).title("Add Demand Here!").icon(BitmapDescriptorFactory.fromResource(R.drawable.add_demand)));
        if(addMarker != null) {
            addMarker.remove();
        }
        addMarker = newMarker;
        //mMap.addMarker(new MarkerOptions().position(point).title("Add Request"));
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        String markerTitle = marker.getTitle();
        String addRequestTitle = "";
        if(addMarker != null) {
            addRequestTitle = addMarker.getTitle();
        }
        if(!markerTitle.equals(addRequestTitle) || addMarker == null) {
            //mTapTextView.setText("Marker Title: " + marker.getTitle()+ ", Add Marker Title: " + addMarker.getTitle());
            //Toast.makeText(this, "Info window clicked",
            //        Toast.LENGTH_SHORT).show();

            markerDetails markDet= (markerDetails)marker.getTag();

            Log.d("Sentid", markDet.docID);

            if(markDet.docID.equals("")){
                AlertDialog alertDialog = new AlertDialog.Builder(MapsActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Invalid Marker");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            else{
                if(markDet.markerType) {
                    Intent intent = new Intent(this, DemandDetails.class);
                    intent.putExtra("demandID", markDet.docID);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(this, ProposalActivity.class);
                    intent.putExtra("proposalID", markDet.docID);
                    startActivity(intent);
                    finish();
                }
            }
        } else if(markerTitle.equals(addRequestTitle)) {
            Intent intent = new Intent(MapsActivity.this, add_demand.class);

            if (addMarker == null) {
                AlertDialog alertDialog = new AlertDialog.Builder(MapsActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("No Point Selected");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            else {
                intent.putExtra("demandLatitude", String.valueOf(addMarker.getPosition().latitude));
                intent.putExtra("demandLongitude", String.valueOf(addMarker.getPosition().longitude));
                startActivity(intent);
                finish();
            }
        }
    }

    protected void getCityState(String usrid){
        DocumentReference docRef = db.collection("user").document(usrid);			//REQNAME :- need to fetch from onclick()
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    usrCity= document.get("Address.City").toString();
                    Log.d("OurCity",usrCity);
                    usrState= document.get("Address.State").toString();
                    Log.d("OurState",usrState);

                    String place = usrCity+", "+usrState;               //"Gandhinagar, Gujarat";
                    Log.d("OurPlace", place);
                    LatLng focusLoc;
                    Geocoder mGeoCoder = new Geocoder(MapsActivity.this);
                    try {
                        List<Address> mAddress = mGeoCoder.getFromLocationName(place, 1);

                        for(Address a : mAddress){
                            if(a.hasLatitude() && a.hasLongitude()) {
                                focusLoc = new LatLng(a.getLatitude(), a.getLongitude());
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(focusLoc, 12.5f));
                            }
                        }

                    } catch (Exception e) {
                        AlertDialog alertDialog = new AlertDialog.Builder(MapsActivity.this).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage("Poor Internet Connection");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(12.5, 12.5), 12.5f));
                    }

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
    }
}

class markerDetails {

    String docID = "";
    boolean markerType;

    public markerDetails(String id, boolean b) {

        docID = id;
        markerType = b;
    }
}