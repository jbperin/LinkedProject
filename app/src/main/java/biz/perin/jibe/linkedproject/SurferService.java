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
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import static biz.perin.jibe.linkedproject.Constants.*;

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
        Intent localIntent = null;
        String resType = intent.getStringExtra("RessourceType");

        if (resType.equals(ANONYMOUS_ANNOUNCE)){
            Log.d(TAG, "Start thread downloading anonymous announce ..");
            WebHelper.getInstance().getAnonymousAnnounces(true);

            localIntent =
                    new Intent(WEB_PART_VISITED)
                            .putExtra("resType", resType);

        } else if (resType.equals(LOGIN)) {
            Log.d(TAG, "Connecting ..");
            localIntent =
                    new Intent(WEB_LOGIN);

            if (WebHelper.getInstance().connect()) {
                // Puts the status into the Intent
                localIntent.putExtra("result", "Logged");
            } else {
                localIntent.putExtra("result", "Failed");
            }

        }else if (resType.equals(ANNOUNCES)) {
                Log.d(TAG, "Start thread downloading announces ..");
                WebHelper.getInstance().getAnnuaire(true);
                localIntent =
                        new Intent(WEB_PART_VISITED)
                                .putExtra("resType", resType);



        } else if (resType.equals(ANNUAIRE)) {
            Log.d(TAG, "Start thread downloading annuaire ..");
            WebHelper.getInstance().getAnnuaire(true);
            localIntent =
                    new Intent(WEB_PART_VISITED)
                            .putExtra("resType", resType);

        } else if (resType.equals(FORUMS)) {
            Log.d(TAG, "Start thread downloading forums ..");
            WebHelper.getInstance().getForums(true);
            localIntent =
                    new Intent(WEB_PART_VISITED)
                            .putExtra("resType", resType);
        } else if (resType.equals(PERSONNAL_INFO)) {
            Log.d(TAG, "Start thread downloading personnal infos ..");
            WebHelper.getInstance().getPersonnalInfo(true);
            localIntent =
                    new Intent(WEB_PART_VISITED)
                            .putExtra("resType", resType);
        } else if (resType.equals(ACCOUNT_INFO)) {
            Log.d(TAG, "Start thread downloading account infos ..");
            WebHelper.getInstance().getAccountInfo(true);
            localIntent =
                    new Intent(WEB_PART_VISITED)
                            .putExtra("resType", resType);
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);

    }
}
