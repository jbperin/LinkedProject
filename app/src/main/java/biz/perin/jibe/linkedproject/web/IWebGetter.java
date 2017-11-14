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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Jean-Baptiste PERIN on 30/10/2017.
 */
public interface IWebGetter {
    public String getSimpleWebPage(String pageAdress);
//    default String getSimpleWebPage(String pageAdress) {
//        String result = null;
//
//
//        URL url = null; // can trigger IOException
//        try {
//            url = new URL(pageAdress);
//            try (BufferedReader in = new BufferedReader(
//                    new InputStreamReader(url.openStream()))) {// can trigger IOException
//
//                String inputLine;
//                result = "";
//                while ((inputLine = in.readLine()) != null)// can trigger IOException
//                    result += inputLine;
//            }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//
//        }
//        return result;
//    }

}
