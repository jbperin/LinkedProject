/*
 * Copyright (C) 2017  Jean-Baptiste Perin <jean-baptiste.perin@orange.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package biz.perin.jibe.linkedproject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.*;
import android.util.Log;

/**
 * Created by Jean-Baptiste PERIN on 12/11/2017.
 */
public class DownloadService extends Service {
    private final String TAG = "DownloadService";
    SharedPreferences preferences;

    private final DownloaderServiceBinder mBinder = new DownloaderServiceBinder();

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private NotificationManager mNM;
    String downloadUrl;
    public static boolean serviceState = false;
    Messenger mMessenger;

    HandlerThread thread;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
//                case MSG_SAY_HELLO:
//                    Toast.makeText(getApplicationContext(), "hello!", Toast.LENGTH_SHORT).show();
//                    break;
                default:
                    super.handleMessage(msg);
            }
            Log.d(TAG, " received :" +msg.toString());
            downloadFile();
            //stopSelf(msg.arg1);
        }
    }
    public void VisitAnnounceAnonymously(){
        Log.d(TAG, "Start downloading anonymous announce ..");
        WebHelper.getInstance().getAnonymousAnnounces(false);
        Log.d(TAG, "Anonymous announce were downloaded.");
    }

    public class DownloaderServiceBinder extends Binder {
        public DownloadService getService() {
            return DownloadService.this;
        }

    }



    @Override
    public void onCreate() {
        Log.d(TAG, "Service created");
        serviceState = true;
        thread = new HandlerThread("ServiceStartArguments", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();

        mServiceHandler = new ServiceHandler(mServiceLooper);
        mMessenger = new Messenger(mServiceHandler);

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        Bundle extra = intent.getExtras();
        if (extra != null) {
            String downloadUrl = extra.getString("downloadUrl");
            Log.d("URL", downloadUrl);

            this.downloadUrl = downloadUrl;
        }

        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }


    @Override
    public void onDestroy() {

        Log.d(TAG, "DESTROY");
        serviceState = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind " +intent.toString());
        return mBinder;
    }



    public void downloadFile() {

//        downloadFile(this.downloadUrl, fileName);
        StatFs stat_fs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        double avail_sd_space = (double) stat_fs.getAvailableBlocks() * (double) stat_fs.getBlockSize();
        //double GB_Available = (avail_sd_space / 1073741824);
        double MB_Available = (avail_sd_space / 10485783);
        //System.out.println("Available MB : " + MB_Available);
        Log.d(TAG, "MB : " + MB_Available);




        SharedPreferences UserPreferences = getSharedPreferences  ("UserPreferences", MODE_PRIVATE );
        if (UserPreferences.contains("login") && UserPreferences.contains("password") ){

            Log.d(TAG, "Authentication Info availablee, connecting ..");

            WebHelper.getInstance().setAuthenticationInformation(
                    UserPreferences.getString("login", "defaultlogin")
                    , UserPreferences.getString("password", "defaultpassword"));

            //WebHelper.getInstance().setAuthenticationInformation(userLogin, userPassword);

            if (WebHelper.getInstance().isAuthenticated()) {
                Log.d(TAG, "Web Client is authenticated, Downloading content ..");
                //
                WebHelper.getInstance().getAnnounces(true);
                Log.d(TAG, "Announce downloaded, downloading annuaire ..");
                WebHelper.getInstance().getAnnuaire(true);
                Log.d(TAG, "Annuaire downloaded, downloading account info ..");
                WebHelper.getInstance().getAccountInfo(true);
                Log.d(TAG, "Account info  downloaded, downloading personnal info ..");
                WebHelper.getInstance().getPersonnalInfo(true);
                Log.d(TAG, "Personnal Info downloaded, downloading forums ..");
                WebHelper.getInstance().getForums(true);
                Log.d(TAG, "Forums downloaded.");
            }


        }


        Log.d(TAG, " Content Downloaded.");

    }


}