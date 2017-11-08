package com.example.luyue.vocabularybook3;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Object.*;
import java.net.URL;
import java.net.URLConnection;

import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.view.LayoutInflater;
/*
import com.loopj.android.http.*;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
*/
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by luyue on 2017/10/24.
 */

public class WordYOUDAO {
    private mainView view;
    public ZSG db;
    private static Word word;
    private TextView textView;

    public WordYOUDAO(mainView view) {
        this.view = view;
        db = new implementsZSG((Context) view);
    }

    public void insertWord(final Word word) {
        this.word = word;
        final String str_url = "http://fanyi.youdao.com/openapi.do?keyfrom=ghyghyghy&key=1853216072&type=data&doctype=json&version=1.1&q="
                + word.getWord();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(str_url);
                    URLConnection connection = url.openConnection();
                    connection.connect();

                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String str_data = "";
                    String readline;
                    while ((readline = in.readLine()) != null) {
                        str_data += readline;
                    }
                    Log.d("data_str",str_data);

                    parseJson(str_data);

                    //单词已经插入到数据库，更新显示列表
                    db.insertWord(word);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


            view.refreshListFragment();


    }

    public void parseJson(String json) {
        try {
            String str1 = "";
            JSONObject result = new JSONObject(json);
            if (0 != (int) result.get("errorCode")) return;

            JSONObject basic = (JSONObject)result.get("basic");
            // JSONObject yinbiaos = (JSONObject)basic.get("phonetic");
            // String yinbiao = yinbiaos.toString() + "\n";
            String phonetic = basic.getString("phonetic");

            JSONArray explains = (JSONArray)basic.get("explains");
            String explain = "";
            for(int j = 0; j < explains.length();j++){
                explain = explain+explains.get(j)+"\n"+"\n";
            }

            str1 = "音标 : " + "[" + phonetic + "]\n\n" + explain;
            Log.d("data_explain", str1);

            this.word.setMeaning(str1);
/*
网络释义
 */
            String str2 = "";
            JSONArray web = result.getJSONArray("web");
            for (int i=0 ; i<web.length(); i++){
                String key = ((JSONObject) web.get(i)).getString("key");

                JSONArray _value = ((JSONObject)web.get(i)).getJSONArray("value");
                String value="";
                for(int j=0;j<_value.length();j++){
                    value += _value.get(j) + "; ";
                }
                //  _key = ((JSONObject)web.get(i)).getJSONObject("key");
                str2 += key + ":" + value + "\n\n";
            }

            this.word.setSample(str2);

/*
            LayoutInflater inflater = getLayoutInflater();
            View root = inflater.inflate(R.layout.words_details,null);
            textView = (TextView) root.findViewById(R.id.tv_Sample);
            textView.setText(str2);
*/

            /*JSONArray translations = (JSONArray) result.get("translation");
            String translation = "";
            for (int i = 0; i < translations.length(); i++) {
                translation += translations.get(i);
            }
            this.word.setMeaning(translation);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Word getWordByContent(String wordContent) {
        return db.getWordByContent(wordContent);
    }



}


