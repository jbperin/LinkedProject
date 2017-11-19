package biz.perin.jibe.linkedproject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import biz.perin.jibe.linkedproject.database.DatabaseHelper;
import biz.perin.jibe.linkedproject.file.FileHelper;
import biz.perin.jibe.linkedproject.model.ISelReceiver;
import biz.perin.jibe.linkedproject.model.LocalSystemExchange;
import biz.perin.jibe.linkedproject.model.SelBuilder;
import biz.perin.jibe.linkedproject.web.WebClient;

/**
 * Created by Jean-Baptiste PERIN on 18/11/2017.
 */
public class LetsClient {
    private final String TAG = "LetsClient";
    private static LetsClient ourInstance = null;


    private DatabaseHelper dbHelper = null;
    private WebHelper webHelper = null;
    LocalSystemExchange theSel = null;
    boolean mBound = false;
    private Context mContext;

    public ModelInterface getModel() {
        return mModel;
    }

    private ModelInterface mModel;
    public void setDownloaderService(DownloadService downloaderService) {
        this.downloaderService = downloaderService;
    }

    DownloadService downloaderService = null;

    public static LetsClient getInstance(Context context) {

        if (ourInstance == null) {
            ourInstance = new LetsClient(context.getApplicationContext());
        }
        return ourInstance;
    }
    public static LetsClient getInstance() {
        return ourInstance;
    }

    private LetsClient(Context context) {
        mContext = context;
        dbHelper = DatabaseHelper.getInstance(context);
        webHelper = WebHelper.getInstance();
        FileHelper.getInstance().setContext(context.getApplicationContext());
        theSel = new LocalSystemExchange();

        ISelReceiver aSelBuilder = new SelBuilder(theSel);
        WebClient aWebClient = new WebClient();

        webHelper.setSelReceiver(aSelBuilder);
        webHelper.setWebClient(aWebClient);

        mModel = new ModelInterface();

        theSel.attach(mModel);
        theSel.attach(dbHelper);

        bindToDownloaderService();

    }
    public void bindToDownloaderService() {

        Log.i(TAG, "bindToDownloaderService()");

        Intent intent = new Intent(mContext, DownloadService.class);
        //startService(intent);
        mContext.bindService(intent, downloaderServiceConnection, Context.BIND_AUTO_CREATE); // no Context.BIND_AUTO_CREATE, because service will be started by startService and thus live longer than this activity
    }

        private ServiceConnection downloaderServiceConnection = new ServiceConnection() {
        private final String TAG = "downloaderServiceConnection";

        public void onServiceConnected(ComponentName className, IBinder binder) {
            Log.d("ServiceConnection", "connected");
            mBound = true;
            setDownloaderService(((DownloadService.DownloaderServiceBinder) binder).getService());
            downloaderService.VisitAnnounceAnonymously();

        }
        //binder comes from server to communicate with method's of
        public void onServiceDisconnected(ComponentName className) {
            Log.d("ServiceConnection", "disconnected");
            mBound = false;
            setDownloaderService(null);
        }
    };

    public void finish() {
        mContext.unbindService(downloaderServiceConnection);
        mContext.stopService(new Intent(mContext, DownloadService.class));

    }
}