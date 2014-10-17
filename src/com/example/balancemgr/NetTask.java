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

import android.app.Activity;
import android.content.Context;
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
      
      private String[] operators = new String[100];
      
      private String myOp;
      private String myCircle;
      
      private String topOp;
      private String topCircle;
      
      private void getServiceProviders(){
      	
      	try {
      		/*
      		 * Get all operators
      		 */
  			JSONObject pro = readJsonFromUrl("https://www.freecharge.in/rest/operators/Mobile");
  			JSONArray arr = pro.getJSONArray("operators");
  			
  			for(int i = 0; i < arr.length(); i++){
  				String op = arr.getJSONObject(i).get("name").toString();
  				int masID = arr.getJSONObject(i).getInt("operatorMasterId");
  				Log.w("rakesh", op);
  				operators[masID] = op;
  			}
      		
      		MainActivity myAct = MainActivity.getThisActivity();
      		Context myCon = MainActivity.getContext();
      		
      		String myNum = myAct.getMyPhoneNum();
      		String topCaller = myAct.getTopCaller();
      		
      		JSONObject myOpJson = readJsonFromUrl("https://www.freecharge.in/rest/operators/mapping/V/" + myNum);
      		JSONArray myOpArr = myOpJson.getJSONArray("prefixData");
      		myOp = operators[myOpArr.getJSONObject(0).getInt("operatorMasterId")];
      		myCircle = myOpArr.getJSONObject(0).getString("circleName");
      		Log.w("rakesh", "your operator is " + myOp + myCircle);

      		JSONObject topOpJson = readJsonFromUrl("https://www.freecharge.in/rest/operators/mapping/V/" + topCaller);
      		JSONArray topOpArr = topOpJson.getJSONArray("prefixData");
      		topOp = operators[topOpArr.getJSONObject(0).getInt("operatorMasterId")];
      		topCircle = topOpArr.getJSONObject(0).getString("circleName");
      		Log.w("rakesh", "your top caller operator is " + topOp + topCircle);
      		
      		String out = "";
      		
      		if(myOp.equals(topOp)){
      			Log.w("rakesh", "You call lot of On-Net numbers. Choose an on-net plan");
      			out += "You call lot of On-Net numbers. Choose an on-net plan.\n";
      		}
      		else{
      			Log.w("rakesh", "You don't do many on-net calls. Choose an off-net plan");
      			out += "You don't do many on-net calls. Choose an off-net plan";
      		}
      		
      		if(!myCircle.equals(topCircle)){
      			Log.w("rakesh", "You do lots of STD calls. Choose and std-plan");
      			out +=  "You do lots of STD calls. Choose and std-plan";
      		}
      		else{
      			Log.w("rakesh", "You don't do many STD calls. Don't go for std-plans");
      			out += "You don't do many STD calls. Don't go for std-plans";
      		}
      		
      		
      		myAct.showText(out);
      		
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

