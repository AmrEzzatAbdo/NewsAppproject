package com.example.amrez.newsappproject;

import android.text.TextUtils;
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
 * Created by amrez on 10/20/2017.
 */

public class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<NewsObjects> fetchNewsData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant data fields from the JSON
        List<NewsObjects> News = extractFeatureFromJson(jsonResponse);

        // Return the list
        return News;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    //Http Request
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // URL checking
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // request checker
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    //read from stream
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
        //return stream data
        return output.toString();
    }

    //return parsing json data
    private static List<NewsObjects> extractFeatureFromJson(String BookJSON) {
        // json string checker
        if (TextUtils.isEmpty(BookJSON)) {
            return null;
        }

        // Create empty list to add News in it
        List<NewsObjects> News = new ArrayList<>();

        //Json Parsing
        try {
            JSONObject baseJsonResponse = new JSONObject(BookJSON);
            JSONObject response = baseJsonResponse.getJSONObject("response");
            JSONArray NewsArray = response.getJSONArray("results");

            for (int i = 0; i < NewsArray.length(); i++) {

                JSONObject currentNews = NewsArray.getJSONObject(i);
                //define NewsObjects variable
                String section;
                String title;
                String date;
                String previewLink;

                // Extract the value for the section
                section = currentNews.getString("sectionName");

                // Extract the value for the title
                title = currentNews.getString("webTitle");


                // Extract the value from authors
                JSONArray authorsArray;
                JSONObject bio;
                StringBuilder authorsInfo = new StringBuilder();
                if (currentNews.has("tags")) {
                    authorsArray = currentNews.getJSONArray("tags");
                    for (int j = 0; j < authorsArray.length(); j++) {
                        bio = authorsArray.getJSONObject(j);
                        if (bio.has("bio")) {
                            authorsInfo.append(bio.getString("bio").replaceAll("[@<p>/\"/]", ""));
                        }
                    }

                } else {
                    authorsInfo.append("No Authors");
                }

                // Extract the date
                if (currentNews.has("webPublicationDate")) {
                    date = currentNews.getString("webPublicationDate");
                } else {
                    date = "No date";
                }

                //getUrl
                previewLink = currentNews.getString("webUrl");

                NewsObjects book = new NewsObjects(title, section, authorsInfo.toString(), date, previewLink);
                News.add(book);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing Book JSON result", e);
        }
        return News;
    }


}
