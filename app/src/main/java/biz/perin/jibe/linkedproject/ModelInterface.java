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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Jean-Baptiste PERIN on 12/11/2017.
 */
public class ModelInterface implements IModelGUI, Observer {

    ArrayList<String> lAnn = new ArrayList<>();

    @Override
    public ArrayList<String> getAnnounces() {


        String SelJson = FileHelper.getInstance().readStringFromFile("sel.js");
        //System.out.println(SelJson);
//        JSONParser parser = new JSONParser();

//        try {
//            JsonObject obj = new JSONObject(SelJson);
        JsonObject jsobj = new JsonParser().parse(SelJson).getAsJsonObject();
        if(jsobj.has("listOfAnnounce")) {
            JsonArray jsListOfAnnounce = jsobj.get("listOfAnnounce").getAsJsonArray();
            for (JsonElement jsAnnounce : jsListOfAnnounce) {
                lAnn.add(jsAnnounce.toString());

//            JsonObject jsTrans = jsAnnounce.getAsJsonObject();
//            System.out.println("jsAnnounce : " +jsTrans.toString());
//            String pseudoOfferer= (jsTrans.has("pseudoOfferer"))?jsTrans.get("pseudoOfferer").getAsString():null;
//            String pseudoDemander= jsTrans.get("pseudoDemander").getAsString();
//            String offerDescritpion= jsTrans.get("offerDescritpion").getAsString();
//            String demandDescription= jsTrans.get("demandDescription").getAsString();
//            //String turlututu= jsTrans.get("turlututu").getAsString();
//            int amount = nullifyJsInteger(jsTrans,"amount");
//
//            System.out.println("Announce : "
//                    +     pseudoOfferer + " "
//                    +     pseudoDemander + " "
//                    +     offerDescritpion + " "
//                    +     demandDescription + " "
//                    +     amount + " "
//            );

            }
            System.out.println();

//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        }
        return (lAnn);

    }

    @Override
    public void update(Observable observable, Object o) {

    }
}
