package com.bhave.intellijent;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 24-09-2017.
 */

public class RecExtractor {
    private RecExtractor() {
    }

    public static List<Song> extractData(String mood){




        String HTTP_REQUEST = "http://839e1a35.ngrok.io/v1.0/get_recommendation";
        URL url = createUrl(HTTP_REQUEST);

        if(url == null){return null;}

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            // TODO Handle the IOException
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        List<Song> ranges = new ArrayList<Song>();


        try {

            JSONObject root  = new JSONObject(jsonResponse);
            JSONArray tags = root.getJSONArray("Songs");

            //JSONArray tags = new JSONArray(jsonResponse)
            Log.e("Length","length recieved is " + tags.length());

            for (int i = 0; i < tags.length(); i++) {
                JSONObject row = tags.getJSONObject(i);


                String title = row.getString("Title");
                String artist = row.getString("Artist");
                String album = row.getString("Album");

                Log.e("Test",title+" "+album+" "+artist);


                ranges.add(new Song(1,title,artist,album));

            }





        } catch (JSONException e) {

            Log.e("QueryUtils", "Problem parsing the activity JSON results", e);
        }
        //String text =ranges.get(0).toString();
        //Log.i("text",text);
        // Return the list of Activities
        return ranges;
    }


    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            return null;
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000000 /* milliseconds */);
            urlConnection.setConnectTimeout(145000 /* milliseconds */);
            urlConnection.connect();
            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
            else{
                Log.e("Url","Problem in URL Connection");
                return null;
            }
        } catch (IOException e) {
            // TODO: Handle the exception

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


}
