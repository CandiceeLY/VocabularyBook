package com.example.luyue.vocabularybook3;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by luyue on 2017/10/24.
 */

public class WordListDetailsActivity extends AppCompatActivity {
    private Word word;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WordListDetailsFragment detailFragment = new WordListDetailsFragment();
        detailFragment.setArguments(getIntent().getExtras());
        getFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, detailFragment)
                .commit();
    }

}
