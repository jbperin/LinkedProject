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

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * Created by Jean-Baptiste PERIN on 25/11/2017.
 */
public class SurferService extends IntentService{

    private final String TAG = SurferService.class.getName();

    public SurferService() {
        super("SurferService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int result = super.onStartCommand(intent, flags, startId);

        return result;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String resType = intent.getStringExtra("RessourceType");

        if (resType.equals("ANNONYMUS_ANNOUNCE")){
            //sLog.d(TAG, "Anonymous announce were downloaded.");
            Log.d(TAG, "Start thread downloading anonymous announce ..");
            WebHelper.getInstance().getAnonymousAnnounces(true);
            Log.d(TAG, "Anonymous announce were downloaded.");

            Intent localIntent =
                    new Intent("biz.perin.jibe.linkedproject.BROADCAST")
                            // Puts the status into the Intent
                            .putExtra("biz.perin.jibe.linkedproject.STATUS", "Done");
            // Broadcasts the Intent to receivers in this app.
            LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);


        } else if (resType.equals("LOGIN")) {
            Log.d(TAG, "Connecting ..");
            WebHelper.getInstance().connect();
            Log.d(TAG, "End of connection");

        }
    }
}
