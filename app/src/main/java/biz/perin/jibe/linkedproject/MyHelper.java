package biz.perin.jibe.linkedproject;/*
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


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

/**
 * Created by Jean-Baptiste PERIN on 30/10/2017.
 */
public class MyHelper {

    public static String compress(String str) {
        String result = str;
        if (str == null || str.length() == 0) {
            return str;
        }
        try {
            System.out.println("String length : " + str.length());
            java.io.ByteArrayOutputStream obj = new java.io.ByteArrayOutputStream();
            java.util.zip.GZIPOutputStream gzip = new java.util.zip.GZIPOutputStream(obj);
            gzip.write(str.getBytes("UTF-8"));
            gzip.close();
            String outStr = obj.toString("UTF-8");

            result = outStr;
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String decompress(String str) {
        String result = str;
        if (str == null || str.length() == 0) {
            return str;
        }
        try {
            java.util.zip.GZIPInputStream gis = new java.util.zip.GZIPInputStream(new java.io.ByteArrayInputStream(str.getBytes("UTF-8")));
            java.io.BufferedReader bf = new java.io.BufferedReader(new java.io.InputStreamReader(gis, "UTF-8"));
            String outStr = "";
            String line;
            while ((line = bf.readLine()) != null) {
                outStr += line;
            }

        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String dict2json(Map<String, String> dictPersonalInfo) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        String userJson = gson.toJson(dictPersonalInfo);
        return (userJson);
    }

    public static HashMap<String, String> json2dict(String jsString) {
        HashMap<String, String> dictValues = new HashMap<>();
        Gson gson = new GsonBuilder().serializeNulls().create();
        JsonObject jsobj = new JsonParser().parse(jsString).getAsJsonObject();
        Set<Map.Entry<String, com.google.gson.JsonElement>> ssEntry = jsobj.entrySet();
        for (Map.Entry ent:ssEntry) {
            dictValues.put((String)ent.getKey(), ent.getValue().toString());
        }
        return (dictValues);
    }


    public interface EntryHandler {

        void processEntry(String[] params);
    }

    public static void patternRunThrough (String htmlText, String RegularExpressionPattern, EntryHandler hndl){
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(RegularExpressionPattern);

        java.util.regex.Matcher matcher = pattern.matcher(htmlText);
        boolean found = false;
        while (matcher.find()) {
            found = true;
            int nbParams = matcher.groupCount();
            String[] params = new String [nbParams];
            for (int ii=1; ii<=nbParams; ii++) {
                params[ii-1] = matcher.group(ii).trim();
            }
            hndl.processEntry(params);
        }
    }


}
