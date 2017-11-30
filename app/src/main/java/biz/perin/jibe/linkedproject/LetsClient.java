package biz.perin.jibe.linkedproject;

import android.content.*;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import biz.perin.jibe.linkedproject.database.DatabaseHelper;
import biz.perin.jibe.linkedproject.file.FileHelper;
import biz.perin.jibe.linkedproject.model.ISelReceiver;
import biz.perin.jibe.linkedproject.model.LocalSystemExchange;
import biz.perin.jibe.linkedproject.model.SelBuilder;
import biz.perin.jibe.linkedproject.web.WebClient;
import com.google.gson.Gson;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static biz.perin.jibe.linkedproject.Constants.*;

/**
 * Created by Jean-Baptiste PERIN on 18/11/2017.
 */
public class LetsClient implements AnnounceFragment.ListAnnounceDataProvider {

    public static final String LETS_MODEL_JSON_FILENAME = "LetsModel.json";
    private static final String TAG = LetsClient.class.getName();

    private static LetsClient ourInstance = null;

    private DatabaseHelper dbHelper = null;
    private WebHelper webHelper = null;
    private LocalSystemExchange theSel = null;

    //boolean mBound = false;
    private Context mContext;

    // Connectivity
    boolean wifiConnected = false;
    boolean mobileConnected = false;

    private ModelInterface mModel;

//    DownloadService downloaderService = null;

    public static LetsClient getInstance(Context context) {

        if (ourInstance == null) {
            Log.d(TAG,"Instanciating LetsClient" );
            ourInstance = new LetsClient(context.getApplicationContext());
        }
        return ourInstance;
    }
    public static LetsClient getInstance() {
        return ourInstance;
    }

    @Override
    public List<String> getListAnnounce() {
        return mModel.getAnnonymousAnnounces();
    }

    private boolean downloadAllowed() {
        // TODO take into account user settings to know if it is allowed to download on mobile data network
        return (wifiConnected || mobileConnected);
    }
    private LetsClient(Context context) {

        mContext = context;
        dbHelper = DatabaseHelper.getInstance(context);
        webHelper = WebHelper.getInstance();
        FileHelper.getInstance().setContext(mContext);

        if (FileHelper.getInstance().fileExists(LETS_MODEL_JSON_FILENAME)){
            Log.d(TAG,"Load model from file " );
            theSel = new Gson().fromJson(FileHelper.getInstance().readStringFromFile(LETS_MODEL_JSON_FILENAME), LocalSystemExchange.class);
        } else {
            Log.d(TAG,"Create new model" );
            theSel = new LocalSystemExchange();
        }

        ISelReceiver aSelBuilder = new SelBuilder(theSel);
        WebClient aWebClient = new WebClient();

        webHelper.setSelReceiver(aSelBuilder);
        webHelper.setWebClient(aWebClient);


        mModel = new ModelInterface(theSel);
        theSel.attach(mModel);
        theSel.attach(dbHelper);


        IntentFilter webPartVisited = new IntentFilter(WEB_PART_VISITED);
        IntentFilter webLogin = new IntentFilter(WEB_LOGIN);
        // Instantiates a new DownloadStateReceiver
        DownloadStateReceiver mDownloadStateReceiver = new DownloadStateReceiver();
        // Registers the DownloadStateReceiver and its intent filters
        LocalBroadcastManager.getInstance(mContext).registerReceiver(
                mDownloadStateReceiver,
                webPartVisited);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(
                mDownloadStateReceiver,
                webLogin);


        checkNetworkConnection();

        if (downloadAllowed() && mModel.getAnnonymousAnnounces().size() == 0) {
            Log.d(TAG,"No announce available .. download some");
            triggerDownload(ANONYMOUS_ANNOUNCE);
        }

        //bindToDownloaderService();
        SharedPreferences UserPreferences = mContext.getSharedPreferences  ("UserPreferences", MODE_PRIVATE );
        if (UserPreferences.contains("login") && UserPreferences.contains("password") ){
           webHelper.setAuthenticationInformation(
                    UserPreferences.getString("login", "defaultlogin")
                    , UserPreferences.getString("password", "defaultpassword"));

            Log.d(TAG,"Start intent service for login " );
            Intent msgIntent = new Intent(mContext, SurferService.class);
            msgIntent.putExtra("RessourceType",LOGIN);
            mContext.startService(msgIntent);
        }



    }

    private void triggerDownload(String resourceType) {
        Intent downloadAnnonIntent = new Intent(mContext, SurferService.class);
        downloadAnnonIntent.putExtra("RessourceType", resourceType);
        mContext.startService(downloadAnnonIntent);
    }

    private void downloadData() {
        if (downloadAllowed()) {
            if (mModel.getPersonnalInfo() == null) {
                triggerDownload(PERSONNAL_INFO);
            }

            if (mModel.getAccount() == null) {
                triggerDownload(ACCOUNT_INFO);
            }

            if (mModel.getAnnounces().size() == 0) {
                triggerDownload(ANNOUNCES);
            }


            if (mModel.getAnnuaire().size() == 0) {
                triggerDownload(ANNUAIRE);
            }

            if (mModel.getForums().size() == 0) {
                triggerDownload(FORUMS);
            }
        }
    }
    private class DownloadStateReceiver extends BroadcastReceiver {
        private DownloadStateReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {

            //Log.d(TAG, "DownloadStateReceiver.onReceive");
            if (intent.getAction().equals(WEB_LOGIN)){
                if (intent.getStringExtra("result").equals("Logged")) {
                    Log.d(TAG, "Successfully logged in !");
                    downloadData();

                } else {
                    Log.d(TAG, "Failed to log in !");
                }
            }else if (intent.getAction().equals(WEB_PART_VISITED)){

                FileHelper.getInstance().writeStringToFile(new Gson().toJson(theSel), LETS_MODEL_JSON_FILENAME);
                mModel.refreshFromModel(intent.getStringExtra("resType"));
            }
        }
    }
    /**
     * Check whether the device is connected, and if so, whether the connection
     * is wifi or mobile (it could be something else).
     */
    private void checkNetworkConnection() {
        // BEGIN_INCLUDE(connect)
        ConnectivityManager connMgr =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {

            wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        } else {
            //Log.i(TAG, getString(R.string.no_wifi_or_mobile));
        }
        // END_INCLUDE(connect)
    }



    public void setModel(ModelInterface modelInterface) {
        mModel = modelInterface;
    }
    public ModelInterface getModel() {
        return mModel;
    }

//    public void setDownloaderService(DownloadService downloaderService) {
//        this.downloaderService = downloaderService;
//    }

//    public void bindToDownloaderService() {
//
//        Log.i(TAG, "bindToDownloaderService()");
//
//        Intent intent = new Intent(mContext, DownloadService.class);
//        //startService(intent);
//        mContext.bindService(intent, downloaderServiceConnection, Context.BIND_AUTO_CREATE); // no Context.BIND_AUTO_CREATE, because service will be started by startService and thus live longer than this activity
//    }


    //        WebHelper.getInstance().getAnonymousAnnounces(false);
//        WebHelper.getInstance().setAuthenticationInformation(userLogin, userPassword);
//        WebHelper.getInstance().getAnnonymousAnnounces(false);
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



//    private ServiceConnection downloaderServiceConnection = new ServiceConnection() {
//        private final String TAG = "downloaderServiceConnection";
//
//        public void onServiceConnected(ComponentName className, IBinder binder) {
//            Log.d("ServiceConnection", "connected");
//            mBound = true;
//            setDownloaderService(((DownloadService.DownloaderServiceBinder) binder).getService());
//            downloaderService.VisitAnnounceAnonymously();
//
//        }
//        //binder comes from server to communicate with method's of
//        public void onServiceDisconnected(ComponentName className) {
//            Log.d("ServiceConnection", "disconnected");
//            mBound = false;
//            setDownloaderService(null);
//        }
//    };

//    public void finish() {
//        mContext.stopService(new Intent(mContext, DownloadService.class));
//        mContext.unbindService(downloaderServiceConnection);
//
//    }


}
