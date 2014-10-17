package com.example.balancemgr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

class NetTask extends AsyncTask<Void, Void, Void> {

    private Exception exception;
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
          sb.append((char) cp);
        }
        return sb.toString();
      }

      public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
          BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
          String jsonText = readAll(rd);
          JSONObject json = new JSONObject(jsonText);
          return json;
        } finally {
          is.close();
        }
      }
    
      public static JSONArray readJsonArrayFromUrl(String url) throws IOException, JSONException {
          InputStream is = new URL(url).openStream();
          try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONArray json = new JSONArray(jsonText);
            return json;
          } finally {
            is.close();
          }
        }
      
      private void getServiceProviders(){
      	
      	try {
  			JSONObject pro = readJsonFromUrl("https://www.freecharge.in/rest/operators/Mobile");
  			JSONArray arr = pro.getJSONArray("operators");
  			
  			for(int i = 0; i < arr.length(); i++)
  				Log.w("rakesh", arr.getJSONObject(i).get("name").toString());
  			
  		} catch (IOException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		} catch (JSONException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
      }
      
    protected Void doInBackground(Void... arg0) {
    	getServiceProviders();
		return null;
    	
    }
}

