package biz.perin.jibe.linkedproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import biz.perin.jibe.linkedproject.database.DatabaseHelper;
import biz.perin.jibe.linkedproject.file.FileHelper;
import biz.perin.jibe.linkedproject.model.ISelReceiver;
import biz.perin.jibe.linkedproject.model.LocalSystemExchange;
import biz.perin.jibe.linkedproject.model.SelBuilder;
import biz.perin.jibe.linkedproject.view.AnnounceAdapter;
import biz.perin.jibe.linkedproject.web.WebClient;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = "MainActivity";
    private ModelInterface mModel;
    private ArrayList<String> listAnnounce;
    private DatabaseHelper dbHelper = null;
    private WebHelper webHelper = null;

    LocalSystemExchange theSel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // Allow to retrieve biz.perin.jibe.linkedproject.web content from main thread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        FileHelper.getInstance().setContext(this.getApplicationContext());

        checkNetworkConnection();

        dbHelper = DatabaseHelper.getInstance(this);
        webHelper = WebHelper.getInstance();

        theSel = new LocalSystemExchange();


        ISelReceiver aSelBuilder = new SelBuilder(theSel);
        WebClient aWebClient = new WebClient();

        webHelper.setSelReceiver(aSelBuilder);
        webHelper.setWebClient(aWebClient);


        mModel = new ModelInterface();

        theSel.attach(mModel);
        theSel.attach(dbHelper);

        Log.d(TAG, "Starts service");
        startService(new Intent(getBaseContext(), DownloadService.class));

        refreshView();

//        SharedPreferences UserPreferences = getSharedPreferences  ("UserPreferences", MODE_PRIVATE );
//        if (UserPreferences.contains("login") && UserPreferences.contains("password") ){
//            WebHelper.getInstance().setAuthenticationInformation(
//                    UserPreferences.getString("login", "defaultlogin")
//                    , UserPreferences.getString("password", "defaultpassword"));
//        }
//
//        WebHelper.getInstance().getAnonymousAnnounces(false);
//        WebHelper.getInstance().setAuthenticationInformation(userLogin, userPassword);
//        WebHelper.getInstance().getAnnounces(false);
//        WebHelper.getInstance().getAnnuaire(false);
//        WebHelper.getInstance().getAccountInfo (false);
//        WebHelper.getInstance().getPersonnalInfo(false);
//        WebHelper.getInstance().getForums(false);


        //WebHelper.getInstance().getMyAnnounces(false);

        //WebHelper.getInstance().publishTansaction();
        //WebHelper.getInstance().publishSold();
        //WebHelper.getInstance().publishInfo();
        //WebHelper.getInstance().publishAnnounce();
        //WebHelper.getInstance().publishUrgentAnnounce();
        //WebHelper.getInstance().unpublishAnnounce();




        //  System.out.println( theSel.getListOfAnnounce());
//        for (Announce ann : lAnnonces) {
//            System.out.println(ann);
//        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            SharedPreferences UserPreferences = getSharedPreferences  ("UserPreferences", MODE_PRIVATE );
            if (UserPreferences.contains("login") && UserPreferences.contains("password") ){
                WebHelper.getInstance().setAuthenticationInformation(
                        UserPreferences.getString("login", "defaultlogin")
                        , UserPreferences.getString("password", "defaultpassword"));
            }
            return true;

        } else if (id == R.id.action_refresh) {
            refreshView();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshView() {

//        Gson theGson = new Gson();
//        String systemcomplet = theGson.toJson(theSel);
//        //System.out.println(systemcomplet);
//        FileHelper.getInstance().writeStringToFile(systemcomplet, "sel.js");

        listAnnounce = mModel.getAnnounces();


        TextView tv = (TextView) findViewById(R.id.displayedText);
        tv.setText("Il y a " + listAnnounce.size() + " annonces.");


        ListView lv = (ListView) findViewById(R.id.list_of_annon_announce);
        lv.setAdapter(new AnnounceAdapter(this, listAnnounce));
        final Activity act = this;
        // Create a message handling object as an anonymous class.
        AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                System.out.println (String.format ("Element clicked positon = %d, id = %x ", position, id));
                Intent intent = new Intent (act, ViewAnnounceActivity.class);
                Bundle bun = new Bundle();
                bun.putInt("position", position);
                bun.putLong("id", id);
                bun.putString("values",listAnnounce.get(position));
                intent.putExtra("biz.perin.jibe.ANNOUNCE_INDEX", bun);
                startActivity(intent);
                //finish();
            }
        };
        lv.setOnItemClickListener(mMessageClickedHandler);
    }

    @Override
    public void onDestroy (){
        super.onDestroy();
        Log.d(TAG, "Stopping service");
        stopService(new Intent(getBaseContext(), DownloadService.class));
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * Check whether the device is connected, and if so, whether the connection
     * is wifi or mobile (it could be something else).
     */
    private void checkNetworkConnection() {
        boolean wifiConnected;
        boolean mobileConnected;
        // BEGIN_INCLUDE(connect)
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {

            wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            if(wifiConnected) {
               // Log.i(TAG, getString(R.string.wifi_connection));
            } else if (mobileConnected){
                //Log.i(TAG, getString(R.string.mobile_connection));
            }
        } else {
            //Log.i(TAG, getString(R.string.no_wifi_or_mobile));
        }
        // END_INCLUDE(connect)
    }
}
