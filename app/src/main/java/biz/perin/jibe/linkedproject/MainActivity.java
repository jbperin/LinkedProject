package biz.perin.jibe.linkedproject;

import android.app.Activity;
import android.content.*;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
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
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import biz.perin.jibe.linkedproject.database.DatabaseHelper;
import biz.perin.jibe.linkedproject.file.FileHelper;
import biz.perin.jibe.linkedproject.model.ISelReceiver;
import biz.perin.jibe.linkedproject.model.LocalSystemExchange;
import biz.perin.jibe.linkedproject.model.SelBuilder;
import biz.perin.jibe.linkedproject.view.AnnounceAdapter;
import biz.perin.jibe.linkedproject.view.ViewGroupUtils;
import biz.perin.jibe.linkedproject.web.WebClient;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = "MainActivity";
    //private ModelInterface mModel;
    private ArrayList<String> listAnnounce;

    WebView wvPageViewer = null;
    NavigationView navigationView = null;
    ListView lvListAnnounce = null;

    LetsClient mSelClient = null;

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

        lvListAnnounce = (ListView) findViewById(R.id.list_of_annon_announce);
        //ViewGroup parent = (ViewGroup) C.getParent();
        //int index = parent.indexOfChild(C);
       // parent.removeView(C);
        //C=getLayoutInflater().inflate(R.layout.activity_main, parent, false);





        checkNetworkConnection();

        mSelClient = LetsClient.getInstance(this);

        wvPageViewer = (WebView) new WebView(this);
        wvPageViewer.setWebViewClient(new MyWebViewClient(this));
        //wvPageViewer.addJavascriptInterface(new JavaBridge(this), "JavaBridge");
        wvPageViewer.loadUrl("file:///android_asset/home.html");

        ViewGroupUtils.replaceView(lvListAnnounce, wvPageViewer);

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
        Log.i(TAG, "onResume()");
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy (){
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        mSelClient.finish();

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

    // This callback is called only when there is a saved instance previously saved using
// onSaveInstanceState(). We restore some state in onCreate() while we can optionally restore
// other state here, possibly usable after onStart() has completed.
// The savedInstanceState Bundle is same as the one used in onCreate().
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i(TAG, "onRestoreInstanceState");
//        mTextView.setText(savedInstanceState.getString(TEXT_VIEW_KEY));
    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "onSaveInstanceState");
        outState.putString("toto", "Value");
        //outState.putString(TEXT_VIEW_KEY, mTextView.getText());

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            ViewGroupUtils.replaceView(lvListAnnounce, wvPageViewer);

        } else if (id == R.id.nav_announce) {
            refreshView();
            //ViewGroupUtils.replaceView(wvPageViewer, lvListAnnounce);

        } else if (id == R.id.nav_people) {


        } else if (id == R.id.nav_forums) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class MyWebViewClient extends WebViewClient {

        private Context context;

        public MyWebViewClient(Context context) {
            this.context = context;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url.equals("lets://ContactActivity")){
                Intent i = new Intent(context, ContactActivity.class);
                context.startActivity(i);
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    }


    private void refreshView() {

        ViewGroupUtils.replaceView(wvPageViewer, lvListAnnounce);

        listAnnounce = mSelClient.getModel().getAnnounces();


        TextView tv = (TextView) findViewById(R.id.displayedText);
        tv.setText("Il y a " + listAnnounce.size() + " annonces.");


        lvListAnnounce.setAdapter(new AnnounceAdapter(this, listAnnounce));
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
            }
        };
        lvListAnnounce.setOnItemClickListener(mMessageClickedHandler);
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
