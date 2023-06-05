package com.mradking.translatorx.activity.utility;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mradking.translatorx.activity.interf.tras_result_call;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class TransX {


    public void TranslateX(String input_ln,String out_lng,String text, tras_result_call result_call_interface) {
        try {
            String encode = URLEncoder.encode(text, "utf-8");
            StringBuilder sb = new StringBuilder();
            sb.append("https://translate.googleapis.com/translate_a/single?client=gtx&sl=");
            sb.append(input_ln);
            sb.append("&tl=");
            sb.append(out_lng);
            sb.append("&dt=t&q=");
            sb.append(encode);



            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(sb.toString())

                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        try {
                            JSONArray jsonArray = new JSONArray(responseBody).getJSONArray(0);
                            StringBuilder translationBuilder = new StringBuilder();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONArray jsonArray2 = jsonArray.getJSONArray(i);
                                translationBuilder.append(jsonArray2.get(0).toString());
                            }

                            String translation = translationBuilder.toString();

                            // Display Toast on the main thread
                            Handler mainHandler = new Handler(Looper.getMainLooper());
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    result_call_interface.susess(translation);

                                }
                            });

                        } catch (JSONException e) {
                            Log.e("translate_api", "JSON parsing error: " + e.getMessage());
                            result_call_interface.failed(e.toString());
                        }
                    } else {
                        result_call_interface.failed("Request was not successful");
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("translate_api", "Request failed: " + e.getMessage());
                    result_call_interface.failed(e.toString());
                }
            });

        } catch (Exception e) {
            Log.e("translate_api", e.getMessage());
            result_call_interface.failed(e.toString());
        }
    }


    public void post_request(String input_ln,String out_lng,String text,
                             tras_result_call result_call_interface) {
        try {
            String encode = URLEncoder.encode(text, "utf-8");
            StringBuilder sb = new StringBuilder();
            sb.append("https://translate.googleapis.com/translate_a/single?client=gtx&sl=");
            sb.append(input_ln);
            sb.append("&tl=");
            sb.append(out_lng);
            sb.append("&dt=t&q=");
            sb.append(encode);


            String URL = "https://sangamenterprises.net/api/fatch_singal_file.php";
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");

            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("sid", "2");

            Gson gson = new GsonBuilder().create();
            String jsonData = gson.toJson(dataMap);


            RequestBody requestBody = RequestBody.create(JSON, jsonData);

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(URL)
                    .put(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        Log.e("respo", responseBody);

                        try {
                            JSONArray jsonArray = new JSONArray(responseBody);
                            if (jsonArray.length() > 0) {
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String name = jsonObject.getString("name");
                                System.out.println("Name: " + name);
                                Log.e("name",name);


                            } else {
                                System.out.println("No data found.");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        }
                    }

                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("translate_api", "Request failed: " + e.getMessage());
                    result_call_interface.failed(e.toString());
                }
            });

        } catch (Exception e) {
            Log.e("translate_api", e.getMessage());
            result_call_interface.failed(e.toString());
        }
    }


}
