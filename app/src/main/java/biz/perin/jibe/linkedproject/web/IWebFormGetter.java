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
package biz.perin.jibe.linkedproject.web;

import java.io.*;
import java.net.*;
import java.util.List;

/**
 * Created by Jean-Baptiste PERIN on 30/10/2017.
 */
public interface IWebFormGetter {
    public String getFormWebPage(String pageAdress, List<String[]> params);
//    default String getFormWebPage(String pageAdress, List<String[]> params) {
//        String result = null;
//        URL url = null;
//        HttpURLConnection client = null;
//        BufferedReader in = null;
//        OutputStreamWriter wr = null;
//
//        try {
//            url = new URL(pageAdress);
//            if (url != null) {
//                client = (HttpURLConnection) url.openConnection(); // can trigger MalformedURLException and IOException
//                client.setRequestMethod("POST");                    // can trigger MalformedURLException
//                client.setDoOutput(true);
//                String data = "";
//
//                for (String[] ent: params) {
//                    //assert (ent.length == 2);
//                    data += "&" + URLEncoder.encode(ent[0], "UTF-8")
//                            + "=" + URLEncoder.encode(ent[1], "UTF-8"); // can trigger UnsupportedEncodingException
//
//                }
//
//                OutputStream clientOutputStream = client.getOutputStream();  // can trigger IOException
//                wr = new OutputStreamWriter(clientOutputStream);
//                System.out.println(data.substring(1));
//                wr.write(data.substring(1)); // can trigger IOException
//                wr.flush(); // can trigger IOException
//
//
//                StringBuilder sb = new StringBuilder();
//
//
////                // Read Server Response
////                if (reader != null) {
////                    try {
////                        while ((line = reader.readLine()) != null) {
////                            // Append server response in string
////                            sb.append(line + "\n");
////                        }
////                    } catch (IOException e) {
////                        e.printStackTrace();
////                    }
////                }
////
////                text = sb.toString();
//                in = new BufferedReader(new InputStreamReader(url.openStream())); // can trigger IOException
//
//                String inputLine;
//                while ((inputLine = in.readLine()) != null)// can trigger IOException
//                    sb.append(inputLine + "\n");
//                result = sb.toString();
//
//                wr.close();
//                in.close();
//
//            }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (ProtocolException e) {
//            e.printStackTrace();
//        }catch(UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (wr != null){
//                try {
//                    wr.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (in != null) {
//                try {
//                    in.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//
//
//        return result;
//    }
}
