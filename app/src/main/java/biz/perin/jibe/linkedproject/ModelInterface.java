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

import biz.perin.jibe.linkedproject.file.FileHelper;
import biz.perin.jibe.linkedproject.model.*;
import com.google.gson.*;

import java.util.ArrayList;


/**
 * Created by Jean-Baptiste PERIN on 12/11/2017.
 */
public class ModelInterface implements IModelGUI, LetsObserver {

    ArrayList<String> lAnn = new ArrayList<>();
    ArrayList<String> lPers = new ArrayList<>();
    ArrayList<String> lForum = new ArrayList<>();
    ArrayList<String> lTrans = new ArrayList<>();
    String personnalInfo = null;
    String account  = null;

    public void loadFromFile(String filename) {
        String SelJson = FileHelper.getInstance().readStringFromFile(filename);

        JsonObject jsobj = new JsonParser().parse(SelJson).getAsJsonObject();

        if (jsobj.has("listOfAnnounce")) {
            JsonArray jsList = jsobj.get("listOfAnnounce").getAsJsonArray();
            for (JsonElement jsElem : jsList) {
                lAnn.add(jsElem.toString());

            }
        }

        if (jsobj.has("listOfPerson")) {
            JsonArray jsList = jsobj.get("listOfPerson").getAsJsonArray();
            for (JsonElement jsElem : jsList) {
                lPers.add(jsElem.toString());

            }
        }

        if (jsobj.has("listOfTransaction")) {
            JsonArray jsList = jsobj.get("listOfTransaction").getAsJsonArray();
            for (JsonElement jsElem : jsList) {
                lTrans.add(jsElem.toString());

            }
        }

        if (jsobj.has("listOfForum")) {
            JsonArray jsList = jsobj.get("listOfForum").getAsJsonArray();
            for (JsonElement jsElem : jsList) {
                lForum.add(jsElem.toString());

            }
        }

        if (jsobj.has("personnalInfo")) {
            JsonElement jsElem = jsobj.get("personnalInfo");
            personnalInfo = jsElem.toString();
        }

        if (jsobj.has("account")) {
            JsonElement jsElem = jsobj.get("account");
            account = jsElem.toString();
            // TODO deals with transactions of the account
        }

    }

    @Override
    public ArrayList<String> getAnnounces() {


        return (lAnn);

    }

    @Override
    public ArrayList<String> getAnnuaire() {
        return lAnn;
    }

    @Override
    public ArrayList<String> getForums() {
        return lForum;
    }

    @Override
    public void onNewAnnounce(Announce ann) {
        lAnn.add(new Gson().toJson(ann).toString());
    }

    @Override
    public void onNewPerson(Person pers) {
        lPers.add(new Gson().toJson(pers).toString());
    }

    @Override
    public void onNewPost(ForumMessage mess) {
        // TODO
        //lMess.add(new Gson().toJson(mess).toString());
    }

    @Override
    public void onNewTransaction(Transaction trans) {
        lTrans.add(new Gson().toJson(trans).toString());
    }
}
