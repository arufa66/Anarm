package com.example.hitroki.anarm;

import android.content.AsyncTaskLoader;
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by hitroki on 2014/12/12.
 */
public class WeatherLoder extends AsyncTaskLoader<JSONObject> {
    private int mCityId;
    private final static String API_URL ="http://weather.livedoor.com/forecast/" +
            "webservice/json/v1?city=%1$d";
    private boolean mEnableCache;
    public WeatherLoder(Context context, boolean enableCache, int cityId){

        super(context);
        this.mEnableCache = enableCache;
        this.mCityId = cityId;
    }



    protected void onStartLoading(){
        forceLoad();
        super.onStartLoading();
    }

    @Override
    public JSONObject loadInBackground() {
        URL url;
        JSONObject result = null;
        try {
            File cachefile = new File(getContext().getCacheDir(), "weather.json");
            Calendar calendar = Calendar.getInstance();
            if (cachefile.exists()) {
                calendar.setTime(new Date(cachefile.lastModified()));
                calendar.add(Calendar.HOUR_OF_DAY, 3);

            }
            String json;

            if (!mEnableCache ||!cachefile.exists() || calendar.getTime().before(new Date())) {
                url = new URL(String.format(API_URL, mCityId));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                json = loadInputStream(connection.getInputStream());
                FileOutputStream cache = null;



                try {
                    cache = new FileOutputStream(cachefile);
                    cache.write(json.getBytes());
                } finally {
                    if (cache != null) {
                        cache.close();
                    }
                }
            }else {
                InputStream stream = new FileInputStream(cachefile);
                json = loadInputStream(stream);

            }


            result = new JSONObject(json);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }
        return result;
    }

    private String loadInputStream(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        String line;
        StringBuffer lines = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            lines.append(line);
        }
        return lines.toString();
    }

}
