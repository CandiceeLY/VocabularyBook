package com.example.luyue.vocabularybook3;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import static android.content.ContentValues.TAG;

/**
 * Created by luyue on 2017/10/23.
 */

public class WordListDetailsFragment extends Fragment {
    private Bundle args;
    private Word word;
    private String str_url_pro = "http://dict-co.iciba.com/api/dictionary.php?type=json&key=D557FA9491DA1F5B30812532EF30B203&w=";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        args = getArguments();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.words_details, container, false);
        TextView tv_word = (TextView) view.findViewById(R.id.tv_Word);
        TextView tv_meaning = (TextView) view.findViewById(R.id.tv_Meaning);
        TextView tv_sample = (TextView) view.findViewById(R.id.tv_Sample);
        if (args != null) {
            word = (Word) args.getSerializable("word");
            if(word != null){
                tv_word.setText(word.getWord());
                tv_meaning.setText(word.getMeaning());
                tv_sample.setText(word.getSample());
            }
        }

        ImageView imageView = (ImageView)view.findViewById(R.id.laba);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(str_url_pro + word.getWord());
                            URLConnection connection = url.openConnection();
                            connection.connect();

                            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                            String str_data = "";
                            String readline;
                            while ((readline = in.readLine()) != null) {
                                str_data += readline;
                            }
                            Log.d("data_str", str_data);

                            parseJson_Pro(str_data);

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

        return view;
    }

    public void parseJson_Pro(String json){
        try {
            String str = "";
            JSONObject result = new JSONObject(json);
            JSONArray symbol = result.getJSONArray("symbols");
            JSONObject _symbol =  symbol.getJSONObject(0);
            String mp3 = _symbol.getString("ph_en_mp3");

            //这里给一个歌曲的网络地址就行了

            Uri uri = Uri.parse(mp3);

            MediaPlayer player  = MediaPlayer.create(getActivity(), uri);

            player.start();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
