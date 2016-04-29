package com.inetcar.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class NetWorkUtils {

    public final static String serverpath="http://10.13.33.63:8080/inetCar";
    //检测是否接通网络
    public static boolean isNetWorkEnabled(Context context)
    {
        ConnectivityManager netmanager =  (ConnectivityManager) context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = netmanager.getActiveNetworkInfo();
        if(netinfo==null)
        {
            return false;
        }
        return true;
    }

    public static String getUTF(InputStream in) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(in,
                "UTF-8"));
        StringBuilder builder = new StringBuilder();
        int len = 0;
        char buffer[] = new char[100];
        while ((len = reader.read(buffer)) != -1) {
            builder.append(buffer, 0, len);
        }
        reader.close();
        in.close();

        return builder.toString();

    }

    public static String doTask(String path, ArrayList<NameValuePair> params) throws IOException {

        HttpClient client = new DefaultHttpClient();
        HttpPost request = new HttpPost(serverpath+path);
        HttpEntity entity = new UrlEncodedFormEntity(params,"utf-8");
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){

            InputStream in = response.getEntity().getContent();
            return getUTF(in);
        }
        return null;
    }
}
