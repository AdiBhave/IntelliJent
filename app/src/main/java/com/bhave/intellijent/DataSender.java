package com.bhave.intellijent;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams.*;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

import static android.R.id.message;


public class DataSender extends Activity implements OnClickListener {

    TextView tvIsConnected, name,artist,album;
    Button btnPost;
    String iname,iartist,ialbum;
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_sender);

        tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);
        artist = (TextView) findViewById(R.id.tvCountry);
        name = (TextView) findViewById(R.id.tvName);
        album = (TextView) findViewById(R.id.tvTwitter);

        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        iname = getIntent().getStringExtra("name");
        iartist = getIntent().getStringExtra("artist");
        ialbum = getIntent().getStringExtra("album");

        artist.setText(iartist);
        name.setText(iname);
        album.setText(ialbum);

        btnPost = (Button) findViewById(R.id.btnPost);

        // check if you are connected or not
        if(isConnected()){
            tvIsConnected.setBackgroundColor(0xFF00CC00);
            tvIsConnected.setText("You are conncted");
        }
        else{
            tvIsConnected.setText("You are NOT conncted");
        }

        // add click listener to Button "POST"
        btnPost.setOnClickListener(this);

    }

    public static String POST(String urlx, final String name, final String album, final String artist, final String sender) {
        InputStream inputStream = null;
        String result = "";

            // 1. create HttpClient
            /*HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("Title",name);
            jsonObject.accumulate("Artist", album);
            jsonObject.accumulate("Album", artist);
            jsonObject.accumulate("Sender", sender);

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        */

            // 11. return result



                    try {
                        URL url = new URL("http://c2ac624c.ngork.io/post_test");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                        conn.setRequestProperty("Accept", "application/json");
                        conn.setDoOutput(true);
                        conn.setDoInput(true);

                        JSONObject jsonParam = new JSONObject();
                        jsonParam.put("Tite", name);
                        jsonParam.put("Artist", artist);
                        jsonParam.put("Album", album);
                        jsonParam.put("Sender", sender);


                        Log.i("JSON", jsonParam.toString());
                        DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                        //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                        os.writeBytes(jsonParam.toString());

                        os.flush();
                        os.close();

                        Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                        Log.i("MSG", conn.getResponseMessage());

                        conn.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

            return result;
    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.btnPost:
                if(!validate())
                    Toast.makeText(getBaseContext(), "Enter some data!", Toast.LENGTH_LONG).show();
                // call AsynTask to perform network operation on separate thread
                new HttpAsyncTask().execute("http://839e1a35.ngrok.io/v1.0/song");
                finish();
                break;
        }

    }


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {


        JsonHttpHandler handler = new JsonHttpHandler();

        @Override
        protected String doInBackground(String... urls) {

            String status = "Success";
            //return POST(urls[0],iname,ialbum,iartist,sp.getString("email","asda"));
            JSONObject json = new JSONObject();
            try {

                json.accumulate("Title", iname);
                json.accumulate("Album", ialbum);
                json.accumulate("Artist",iartist);
                json.accumulate("Sender",sp.getString("email","adity.bhave41@yahoo.com"));


                handler.postJSONfromUrl(urls[0], json);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                status = "Error Protocol";
            } catch (IOException e) {
                e.printStackTrace();
                status = "Error reading";
            } catch (JSONException e) {
                e.printStackTrace();
                status = "JSON Error";
            }
            return status;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validate(){
            return true;
    }
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    // get reference to the views
}

