package hippo.One;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import android.util.Log;

public class Post extends Thread
{
    public String query;
    public ArrayList<ArrayList> results;
    
    
    //Post Code
    public void run() {
        results = postit(query);
    }
    
    //Post Method to connect to database via webservice
    ArrayList<ArrayList> postit(String queryString){
        try {
                 String url = "http://jndbk.ddns.net/ajax/hippo/";
                 URL obj = new URL(url);
                 HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                 //add request header
                 con.setRequestMethod("POST");
                 con.setRequestProperty("contentType", "application/json;charset=utf-8");
                 con.setRequestProperty("dataType", "json");

                 String urlParameters = queryString;

                 // Send post request
                 con.setDoOutput(true);
                 DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                 wr.writeBytes(urlParameters);
                 wr.flush();
                 wr.close();

                 int responseCode = con.getResponseCode();
                 System.out.println("\nSending 'POST' request to URL : " + url);
                 System.out.println("Post parameters : " + urlParameters);
                 System.out.println("Response Code : " + responseCode);

                 BufferedReader in = new BufferedReader(
                         new InputStreamReader(con.getInputStream()));
                 String inputLine;
                 StringBuffer response = new StringBuffer();
  
                 while ((inputLine = in.readLine()) != null) {
                         response.append(inputLine);
                 }
                 in.close();
                 JSONArray array = (JSONArray) JSONValue.parse(response.toString());
                 return array;
         } catch(Exception e){
             Log.d("exception e is",e.toString());
            return new ArrayList<ArrayList>();
         }
     }
    
    
}
