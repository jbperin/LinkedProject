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
package biz.perin.jibe.linkedproject.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;


/**
 * Created by Jean-Baptiste PERIN on 30/10/2017.
 */
public interface IFiler {
    public void writeStringToFile(String content, String filename);
//    default void writeStringToFile(String content, String filename) {
//        try(  PrintWriter out = new PrintWriter( filename )  ){
//            out.println( content );
//        }catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
    public boolean fileExists(String filename);

    public String readStringFromFile(String filename);

//    default String readStringFromFile(String filename)  {
//        try {
//            Charset encoding = Charset.defaultCharset();
//            byte[] encoded = Files.readAllBytes(Paths.get(filename));
//            return new String(encoded, encoding);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}
