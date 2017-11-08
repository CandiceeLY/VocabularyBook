package com.example.luyue.vocabularybook3;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;

import static com.example.luyue.vocabularybook3.R.layout.activity_main;

/**
 * Created by luyue on 2017/11/6.
 */

public class TranslateActivity extends AppCompatActivity {
    private EditText editText;
    private TextView textView;
    private Button button;
    String str_url_tran = "http://fanyi.youdao.com/openapi.do?keyfrom=ghyghyghy&key=1853216072&type=data&doctype=json&version=1.1&q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.translate);
        editText = (EditText)findViewById(R.id.translate_et);
        textView = (TextView)findViewById(R.id.translate_tv);
        button = (Button)findViewById(R.id.translate_bt);

        this.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String keyword = URLEncoder.encode(editText.getText().toString(), "UTF-8");

                            URL url = new URL(str_url_tran + keyword);

                            URLConnection connection = url.openConnection();
                            Log.i("INFO_", url.toString());
                            connection.setAllowUserInteraction(true);

                            connection.connect();

                            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
                            String str_data = "";
                            String readline;

                            while ((readline = in.readLine()) != null) {
                                str_data += readline;
                            }

                            Log.d("data_str_tran", str_data);

                            parseJson_Tran(str_data);
                           /* //单词已经插入到数据库，更新显示列表
                            db.insertWord(word);
*/
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    public void parseJson_Tran(String json){
        try {
            JSONObject result =  new JSONObject(json);
            JSONArray translations = (JSONArray) result.get("translation");
            String translation = "";
            for (int i = 0; i < translations.length(); i++) {
                translation += translations.get(i);
            }
            textView.setText(translation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



