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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jean-Baptiste PERIN on 30/10/2017.
 */
public class WebClient implements IWebClient {

    private String login=null;
    private String password=null;
    private String loginURL=null;
    private boolean isConnected = false;
    private HttpClient httpclient;
    private BasicCookieStore cookieStore;

    public WebClient() {
    }

    private String UnannonForm (String spec, List<String []> params) {
        String htmlContent=null;
        try {
            RequestBuilder postRequestBuilder = RequestBuilder.post()
                .setUri(new URI(spec));
            for (String[] ent: params) {
                postRequestBuilder.addParameter(URLEncoder.encode(ent[0],"UTF-8"), URLEncoder.encode(ent[1],"UTF-8"));
            }

            HttpUriRequest postRequest = postRequestBuilder.build();
            HttpResponse httpResp = httpclient.execute(postRequest);
            HttpEntity ent = httpResp.getEntity();
            htmlContent = EntityUtils.toString(ent);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } finally {
            // TODO fermer httpResp s'il est ouvert
        }

        return (htmlContent);
    }

    private static  String AnnonForm(String spec, List<String []> params) {
        String htmlContent = null;
        try {
            URL url = new URL(spec);
            HttpURLConnection client = null;
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("POST");
            client.setDoOutput(true);
            String data = "";

            for (String[] ent: params) {
                //assert (ent.length == 2);
                data += "&" + URLEncoder.encode(ent[0], "UTF-8")
                        + "=" + URLEncoder.encode(ent[1], "UTF-8"); // can trigger UnsupportedEncodingException

            }
            OutputStream clientOutputStream = client.getOutputStream();
            OutputStreamWriter wr = new OutputStreamWriter(clientOutputStream);
            wr.write(data.substring(1));
            wr.flush();
            wr.close();
            BufferedReader reader = null;

            reader = new BufferedReader(new InputStreamReader(client.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            if (reader != null) {
                try {
                    while ((line = reader.readLine()) != null) {
                        // Append server response in string
                        sb.append(line + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                reader.close();
            }

            htmlContent = sb.toString();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (htmlContent);
    }

    @Override
    public String getFormWebPage(String pageAdress , List<String []> params) {
        String result = null;

        if (! isConnected) {
            result = AnnonForm(pageAdress , params);
        } else {
            result = UnannonForm(pageAdress , params);
        }


        return result;
    }
    @Override
    public String getSimpleWebPage(String pageAdress ) {
        String result = null;
        try {
            RequestBuilder postRequestBuilder = RequestBuilder.post()
                    .setUri(new URI(pageAdress));

            HttpUriRequest postRequest = postRequestBuilder.build();
            HttpResponse httpResp = httpclient.execute(postRequest);
            HttpEntity ent = httpResp.getEntity();
            result = EntityUtils.toString(ent);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } finally {
            // TODO fermer httpResp s'il est ouvert
        }

        return result;
    }
    @Override
    public boolean connect(String loginPageURL, String user, String password) throws URISyntaxException {

        this.loginURL = loginPageURL;
        this.login = user;
        this.password = password;


        this.httpclient = new DefaultHttpClient();
        if (httpclient == null) {
            isConnected = false;
        }
        HttpPost post = new HttpPost(loginPageURL);
        HttpResponse response = null;
        List<NameValuePair> postFields = new ArrayList<NameValuePair>(4);


        postFields.add(new BasicNameValuePair("lien", "connexion"));
        postFields.add(new BasicNameValuePair("login", login));
        postFields.add(new BasicNameValuePair("mdp", password));
        postFields.add(new BasicNameValuePair("faire", "entrer"));
        try {
            post.setEntity(new UrlEncodedFormEntity(postFields, HTTP.UTF_8));
            response = httpclient.execute(post);
            HttpEntity entity = response.getEntity();
            System.out.println("Login form get: " + response.getStatusLine());
            isConnected = true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //*******************
//        this.cookieStore = new BasicCookieStore();
//        this.httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
//        if (httpclient == null) {
//            isConnected = false;
//        }
//        HttpUriRequest _login = RequestBuilder.post()
//                .setUri(new URI(this.loginURL))
//                .addParameter("lien", "connexion")
//                .addParameter("login", this.login)
//                .addParameter("mdp", this.password)
//                .addParameter("faire", "entrer")
//                .build();
//        try {
//            CloseableHttpResponse loginResponse = httpclient.execute(_login);
//            HttpEntity entity = loginResponse.getEntity();
//
//            System.out.println("Login form get: " + loginResponse.getStatusLine());
//
//            EntityUtils.consume(entity);
//
//
//
//            isConnected = true;
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            // TODO fermer loginResponse s'il est ouvert
//        }
        return isConnected;
    }

    @Override
    public void disconnect() {
         {
            //httpclient.close();
            isConnected = false;
        }
    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }

    public static void main(String[] args) {

        System.out.println("Hello world");
        String htmlText;

        WebClient wc =  new WebClient();

        htmlText = wc.getFormWebPage("http://sel-des-deux-rives.org/webcatalogue-anonyme/"
                , new ArrayList<String[]>() {{
                    add(new String[] {"annonce", "offres"});
                    add(new String[] {"lien", "offres"});
                    add(new String[] {"affich", "cat"});
                    add(new String[] {"lister", "ok"});
                }});

        System.out.println(htmlText);

//        try {
//            wc.connect("http://sel-des-deux-rives.org/catalogue/index.php", userLogin, userPassword);
//
//            htmlText = wc.getFormWebPage("http://sel-des-deux-rives.org/catalogue/index.php?lien=annuaire"
//                    , new ArrayList<String[]>() {{
//                        add(new String[] {"qui", userPseudo});
//                     }});
//            System.out.println(htmlText);
//
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//
//
//        wc.disconnect();
//

        System.out.println("The end");

    }
}
