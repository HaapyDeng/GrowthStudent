package com.mpl.GrowthStud.Student.Tools;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mpl.GrowthStud.Student.Activity.TuWenActivity;
import com.mpl.GrowthStud.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

public class UploadFile {


    public static String UploadPic(final Context context, File file) {
        final String[] backUrl = {""};
        final String[] imgUrl = {context.getResources().getString(R.string.uploadFile)};
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        try {
            params.put("image", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        client.post(imgUrl[0], params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response==>>>", response.toString());
                try {
                    int code = response.getInt("code");
                    if (code == 0) {
                        JSONArray array = response.getJSONArray("data");
                        JSONObject object = array.getJSONObject(0);
                        String singUrl;
                        singUrl = object.getString("resource");
                        backUrl[0] = singUrl;
                    } else {
                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(context, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("responseString==>>", responseString);
                Toast.makeText(context, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(context, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });

        return backUrl[0];
    }
}
