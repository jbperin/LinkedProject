package biz.perin.jibe.linkedproject;

import android.content.*;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

import static biz.perin.jibe.linkedproject.Constants.COUNTERPART;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
        ,AnnounceFragment.OnListAnnounceInteractionListener
        ,AnnounceFragment.ListAnnounceDataProvider
{


    private final String TAG = MainActivity.class.getName();

    NavigationView navigationView = null;

    LetsClient mSelClient = null;

    FragmentManager fragmentManager = getSupportFragmentManager();
    WebFragment webFragment = new WebFragment();
    AnnounceFragment announceFragment = new AnnounceFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);


        // Allow to retrieve biz.perin.jibe.linkedproject.web content from main thread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (mSelClient == null ) {
            Log.d(TAG, "Retrieving new Instance of sel client");
            mSelClient = LetsClient.getInstance(this.getApplicationContext());
        } else {
            Log.d(TAG, "Sel client already available");
        }

        TextView tvDisplay = (TextView) findViewById(R.id.displayedText);
        tvDisplay.setText("Il y a "+mSelClient.getModel().getAnnonymousAnnounces().size() + " annonces");

        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content_main, webFragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onListAnnounceInteraction(String jsItem) {
        Log.d(TAG, "onListAnnounceInteraction");
        Intent intent = new Intent (this, ViewAnnounceActivity.class);
        intent.putExtra("biz.perin.jibe.ANNOUNCE_DATA", jsItem);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();
    }
    @Override
    protected void onRestart() {
        Log.i(TAG, "onRestart");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();

    }

    @Override
    public void onDestroy (){
        super.onDestroy();
        Log.d(TAG, "onDestroy");
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
                Intent msgIntent = new Intent(this, SurferService.class);
                msgIntent.putExtra("RessourceType","LOGIN");
                startService(msgIntent);
            }
            return true;

        } else if (id == R.id.action_refresh) {

            //refreshView();
            TextView tvDisplay = (TextView) findViewById(R.id.displayedText);
            tvDisplay.setText("Il y a "+mSelClient.getModel().getAnnonymousAnnounces().size() + " annonces");

            Intent intent = new Intent(this, PublishTransactionActivity.class);
            intent.putExtra(COUNTERPART, "unknown");
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // This callback is called only when there is a saved instance previously saved using
    // onSaveInstanceState(). We restore some state in onCreate() while we can optionally restore
    // other state here, possibly usable after onStart() has completed.
    // The savedInstanceState Bundle is same as the one used in onCreate().
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i(TAG, "onRestoreInstanceState");
        String strModel = savedInstanceState.getString("model");
        //mSelClient.setModel(new Gson().fromJson(strModel, ModelInterface.class));
    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "onSaveInstanceState");
        //outState.putString("model", new Gson().toJson(mSelClient.getModel()).toString());
        super.onSaveInstanceState(outState);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_main, webFragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_announce) {
            FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_main, announceFragment);
            fragmentTransaction.addToBackStack("nav2announce");
            fragmentTransaction.commit();

        } else if (id == R.id.nav_people) {


        } else if (id == R.id.nav_forums) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_account) {

            Intent intent = new Intent(this, AccountActivity.class);
            //intent.putExtra(COUNTERPART, "unknown");
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void refreshView() {
        TextView tv = (TextView) findViewById(R.id.displayedText);
        tv.setText("To be defined");


    }


    @Override
    public List<String> getListAnnounce() {
        return mSelClient.getModel().getAnnonymousAnnounces();
    }
}
