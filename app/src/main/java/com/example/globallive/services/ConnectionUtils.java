package com.example.globallive.services;

import android.util.Log;

import com.example.globallive.entities.AuthenticatedUser;
import com.example.globallive.entities.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class ConnectionUtils {
    public static String GET(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            //JSONObject json = new JSONObject(jsonText);
            return jsonText;
        } finally {
            is.close();
        }
    }

    public static String POST(String path, String jsonBody) throws IOException {
        try {
            URL url = new URL(path);
            byte[] postDataBytes = jsonBody.toString().getBytes("UTF-8");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);
            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder(); //On récupére le message de réponse du stream
            for (int c; (c = in.read()) >= 0; ) {
                sb.append((char) c);
            }
            String responseMsg = sb.toString();
            int responseCode = conn.getResponseCode();
            return responseMsg;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //Si nous avons une erreur nous retournons null
        return null;
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}


