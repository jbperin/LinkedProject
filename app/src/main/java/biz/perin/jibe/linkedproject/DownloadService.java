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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Jean-Baptiste PERIN on 12/11/2017.
 */
public class DownloadService extends Service {
    private final String TAG = "DownloadService";
    SharedPreferences preferences;


    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private NotificationManager mNM;
    String downloadUrl;
    public static boolean serviceState = false;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, " received :" +msg.toString());
            downloadFile();
            //showNotification(getResources().getString(R.string.notification_catalog_downloaded), "VVS");
            showNotification("notification_catalog_downloaded", "VVS");
            stopSelf(msg.arg1);
        }
    }


    @Override
    public void onCreate() {
        Log.d(TAG, "Service created");
        serviceState = true;
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        HandlerThread thread = new HandlerThread("ServiceStartArguments", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);

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
        //Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }


    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }


    public void downloadFile() {

//        downloadFile(this.downloadUrl, fileName);
        StatFs stat_fs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        double avail_sd_space = (double) stat_fs.getAvailableBlocks() * (double) stat_fs.getBlockSize();
        //double GB_Available = (avail_sd_space / 1073741824);
        double MB_Available = (avail_sd_space / 10485783);
        //System.out.println("Available MB : " + MB_Available);
        Log.d("MB", "" + MB_Available);



        WebHelper.getInstance().getAnonymousAnnounces(false);
        Log.d(TAG, "Anonymous announce were downloaded ..");

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


        Log.d(TAG, " Content Downlaoded.");

    }


    void showNotification(String message, String title) {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = message;

        // Set the icon, scrolling text and timestamp
        Notification notification = new Notification(R.drawable.ic_menu_manage, "vvs",
                System.currentTimeMillis());
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this.getBaseContext(), 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // Set the info for the views that show in the notification panel.
//        notification.setLatestEventInfo(this, title,
//                text, contentIntent);
        // Send the notification.
        // We use a layout id because it is a unique number.  We use it later to cancel.
        mNM.notify(R.string.app_name, notification);
    }

//    public void downloadFile(String fileURL, String fileName) {
//
//        StatFs stat_fs = new StatFs(Environment.getExternalStorageDirectory().getPath());
//        double avail_sd_space = (double) stat_fs.getAvailableBlocks() * (double) stat_fs.getBlockSize();
//        //double GB_Available = (avail_sd_space / 1073741824);
//        double MB_Available = (avail_sd_space / 10485783);
//        //System.out.println("Available MB : " + MB_Available);
//        Log.d("MB", "" + MB_Available);
//        try {
//            File root = new File(Environment.getExternalStorageDirectory() + "/vvveksperten");
//            if (root.exists() && root.isDirectory()) {
//
//            } else {
//                root.mkdir();
//            }
//            Log.d("CURRENT PATH", root.getPath());
//            URL u = new URL(fileURL);
//            HttpURLConnection c = (HttpURLConnection) u.openConnection();
//            c.setRequestMethod("GET");
//            c.setDoOutput(true);
//            c.connect();
//            int fileSize = c.getContentLength() / 1048576;
//            Log.d("FILESIZE", "" + fileSize);
//            if (MB_Available <= fileSize) {
//                this.showNotification("notification_no_memory", "notification_error");
//                c.disconnect();
//                return;
//            }
//
//            FileOutputStream f = new FileOutputStream(new File(root.getPath(), fileName));
//
//            InputStream in = c.getInputStream();
//
//
//            byte[] buffer = new byte[1024];
//            int len1 = 0;
//            while ((len1 = in.read(buffer)) > 0) {
//                f.write(buffer, 0, len1);
//            }
//            f.close();
//            File file = new File(root.getAbsolutePath() + "/" + "some.pdf");
//            if (file.exists()) {
//                file.delete();
//                Log.d("FILE-DELETE", "YES");
//            } else {
//                Log.d("FILE-DELETE", "NO");
//            }
//            File from = new File(root.getAbsolutePath() + "/" + fileName);
//            File to = new File(root.getAbsolutePath() + "/" + "some.pdf");
//
//
//        } catch (Exception e) {
//            Log.d("Downloader", e.getMessage());
//
//        }
//    }
}