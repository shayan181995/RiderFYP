package com.example.hp.fyp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.R.id.toggle;
import static java.security.AccessController.getContext;

/**
 * Created by HP on 5/11/2017.
 */

public class Main extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private FirebaseAuth mAuth;
    private static String UserID;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private GoogleMap mMap;
    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private EditText edittext;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mTitle;
    private Button gobtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        mAuth = FirebaseAuth.getInstance();
        ///Fetching UserID of the user from login or signup page/////////
        Bundle myData = getIntent().getExtras();
        UserID = myData.getString("UserID");

        ///////////////////////////

        ////////////////Navigation drawer work//////////////////
        mPlanetTitles = getResources().getStringArray(R.array.menu_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles){
            ///code to make text of menu items black
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);

                TextView textView=(TextView) view.findViewById(android.R.id.text1);

            /*YOUR CHOICE OF COLOR*/
                textView.setTextColor(Color.BLACK);

                return view;
            }
        });



        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());


        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getActionBar().setTitle(mDrawerTitle);
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
       mDrawerToggle.setDrawerIndicatorEnabled(true);
       mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_drawer);


        // Create an instance of GoogleAPIClient.

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        //Map CallBack
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




    }



    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            view.setBackgroundColor(Color.WHITE);
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        FragmentManager fragmentManager = getFragmentManager();

        //Hiding Lets Go btn on vaigation item picked
        gobtn =(Button) findViewById(R.id.GoBtn);
        gobtn.setVisibility(View.GONE);

        ///////Disable navigation drawer on item click/////
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mDrawerToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerToggle.syncState();



        if (position == 0) {
            fragmentManager.beginTransaction()
                    .replace(R.id.relativeLayout1, new CarFragment() )
                    .addToBackStack(null)
                    .commit();
        } else if (position == 1) {
            fragmentManager.beginTransaction()
                    .replace(R.id.relativeLayout1, new TripFragment())
                    .addToBackStack(null)
                    .commit();
        } else if (position == 2) {
            fragmentManager.beginTransaction()
                    .replace(R.id.relativeLayout1, new ProfileFragment())
                    .addToBackStack(null)
                    .commit();
        }
        else if (position == 3) {
            fragmentManager.beginTransaction()
                    .replace(R.id.relativeLayout1, new PaymentFragment())
                    .addToBackStack(null)
                    .commit();
        }
        else if (position == 4) {
            mAuth.signOut();
            Intent i = new Intent(this,LoginActivity.class);
            startActivity(i);

        }


        // Highlight the selected item, update the title, and close the drawer
        //mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void onBackPressed(){
        if(getFragmentManager().getBackStackEntryCount() > 0){
            getFragmentManager().popBackStackImmediate();
            //Display Button again Lets Go on coming back to home page
            gobtn =(Button) findViewById(R.id.GoBtn);
            gobtn.setVisibility(View.VISIBLE);

            //Enable Navigation drawer on home return
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            mDrawerToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_UNLOCKED);
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            mDrawerToggle.syncState();


            //Change title to home again
            getSupportActionBar().setTitle("FYP");




        }
        else{
            super.onBackPressed();

        }
    }



    @Override
    public void setTitle(CharSequence title) {
       mTitle = title;
       getSupportActionBar().setTitle(mTitle);
    }



    //////////////////Car Page /////////////////////
    public static class CarFragment extends Fragment {
        public static final String ARG_PLANET_NUMBER = "planet_number";
        private Spinner CarCategories;
        private EditText CarMake;
        private EditText CarModel;
        private EditText CarCapacity;
        private Spinner CarCategory;
        private String CarKey;




        public CarFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.carpage, container, false);

            // Reference to layout Widgets//
            CarMake = (EditText) rootView.findViewById(R.id.CarMake);
            CarModel = (EditText) rootView.findViewById(R.id.CarModel);
            CarCapacity = (EditText) rootView.findViewById(R.id.CarCapacity);
            CarCategory = (Spinner) rootView.findViewById(R.id.category);
            final Button button = (Button) rootView.findViewById(R.id.Register);

            /*int i = getArguments().getInt(ARG_PLANET_NUMBER);
            String planet = getResources().getStringArray(R.array.menu_array)[i];
            getActivity().setTitle(planet);*/

            // Set the Car Category Options//////

            CarCategories = (Spinner) rootView.findViewById(R.id.category);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.CarCats,
                    R.layout.support_simple_spinner_dropdown_item
            );
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            CarCategories.setAdapter(adapter);
            ///////////////////////////////////

            //Populate Car Page for edit//
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("");
            DatabaseReference CarRef = ref.child("cars");
            CarRef.orderByChild("OwnerID").equalTo(UserID).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                    Car newCar = dataSnapshot.getValue(Car.class);
                    CarKey = dataSnapshot.getKey();
                    if(CarKey!=null) {
                        button.setText("Update Car");
                    }
                    CarCategory.setSelection(newCar.Category);
                    CarMake.setText(newCar.CarMake);
                    CarModel.setText(String.valueOf(newCar.CarModel));
                    CarCapacity.setText(String.valueOf(newCar.CarCapacity));
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
            //Populate Car Page for edit ends//

            ///Button Click Work//

            button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference("");
                    DatabaseReference CarRef = ref.child("cars");
                    DatabaseReference newCarRef = CarRef.push();
                    //Pushing in Car Table
                    Car mCar = new Car(UserID,CarCategory.getSelectedItemPosition(),CarMake.getText().toString(),
                            Integer.parseInt(CarModel.getText().toString()),Integer.parseInt(CarCapacity.getText().toString()));

                    ////////////////////////////

                    // Get the unique ID generated by a push()
                    //String ProfileId = newUserRef.getKey();
                    ///////////////////////////////

                    //Add in Database///
                    if(CarKey!=null) {
                        CarRef.child(CarKey).setValue(mCar);
                    }
                    else{
                        newCarRef.setValue(mCar);
                    }
                }
            });
            ///Button Click Work Ends//

            return rootView;
        }

    }

    //////////////Trip Fragment///////////////
    public static class TripFragment extends Fragment {
        public static final String ARG_PLANET_NUMBER = "planet_number";

        public TripFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.trips, container, false);
            /*int i = getArguments().getInt(ARG_PLANET_NUMBER);
            String planet = getResources().getStringArray(R.array.menu_array)[i];
            getActivity().setTitle(planet);*/
            return rootView;
        }
    }
    //////////////Profile Fragement//////////
    public static class ProfileFragment extends Fragment {
        public static final String ARG_PLANET_NUMBER = "planet_number";
        private EditText UserName;
        private EditText Country;
        private EditText City;
        private EditText Email;
        private EditText Phone;
        private EditText UserType;
        private EditText Gender;
        private Spinner PaymentMode;
        private String[] PaymentArray;



        public ProfileFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.profiles, container, false);
            /*int i = getArguments().getInt(ARG_PLANET_NUMBER);
            String planet = getResources().getStringArray(R.array.menu_array)[i];
            getActivity().setTitle(planet);*/

            ///Disabling Non editable Profile Data///
            Country = (EditText) rootView.findViewById(R.id.Country);
            City = (EditText) rootView.findViewById(R.id.City);
            Email = (EditText) rootView.findViewById(R.id.Email);
            UserType = (EditText) rootView.findViewById(R.id.UserType);

            Country.setEnabled(false);
            City.setEnabled(false);
            Email.setEnabled(false);
            UserType.setEnabled(false);

            // Set the Payment Options//////

            PaymentMode = (Spinner) rootView.findViewById(R.id.PaymentMode);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.SpinnerItems,
                    R.layout.support_simple_spinner_dropdown_item
                    );
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            PaymentMode.setAdapter(adapter);
            ///////////////////////////////////

            return rootView;
        }
    }
    /////////////Payment Fragement/////////
    public static class PaymentFragment extends Fragment {
        public static final String ARG_PLANET_NUMBER = "planet_number";

        public PaymentFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.payments, container, false);
            /*int i = getArguments().getInt(ARG_PLANET_NUMBER);
            String planet = getResources().getStringArray(R.array.menu_array)[i];
            getActivity().setTitle(planet);*/
            return rootView;
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser==null){
            Intent i = new Intent(this,LoginActivity.class);
            startActivity(i);
        }
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    //if want other menus at top then use this
    /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
        */


    @Override
    public void onMapReady(final GoogleMap googleMap) {

        this.googleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        else {

        }

        googleMap.setMyLocationEnabled(true);

        final PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        EditText etPlace = (EditText)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input);
        etPlace.setHint("Enter The Dropoff location");
        etPlace.setBackground(ContextCompat.getDrawable(this, R.drawable.edittext));
        etPlace.getLayoutParams().width=250;
        etPlace.getLayoutParams().height=80;;
        //Code for restricting autocomplete results within karachi
       /* autocompleteFragment.setBoundsBias(new LatLngBounds(
                new LatLng(24, 66),
                new LatLng(25, 67)));*/


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.

                //My start position
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);

                //Draw route(line) from start to end position
                PolylineOptions rectOptions = new PolylineOptions()
                        .add(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                        .add(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude))
                        .geodesic(true);
                Polyline polyline = googleMap.addPolyline(rectOptions);


                //Work for zooming camera position in the route
                LatLngBounds.Builder boundsbuilder = new LatLngBounds.Builder();
                LatLng StartLoc = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
                LatLng EndLoc = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                boundsbuilder.include(StartLoc);
                boundsbuilder.include(EndLoc);
                int routePadding = 100;
                LatLngBounds latLngBounds = boundsbuilder.build();
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds,routePadding));

                //adding marker at the destination
                googleMap.addMarker(new MarkerOptions().position(EndLoc));

        }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
               // Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    //This method will be used when we ask for permission on runtime inside else of checking permission code
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (permissions.length == 1 &&
                    permissions[0] == android.Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            } else {
                // Permission was denied. Display an error message.
            }
        }
    }


    //code to get current location and put it on the start location text field
    @Override
    public void onConnected(Bundle connectionHint) {
        edittext = (EditText) findViewById(R.id.editText);
       mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                LatLng StartLoc = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(StartLoc,17));


            } catch (IOException e) {
                e.printStackTrace();
            }

            String address = addresses.get(0).getAddressLine(0);
            edittext.setText(address);
            edittext.setEnabled(false);



        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}