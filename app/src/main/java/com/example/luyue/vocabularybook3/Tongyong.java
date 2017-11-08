package com.example.luyue.vocabularybook3;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luyue on 2017/10/23.
 */

public class Tongyong {
    public static boolean isLand(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
    //Cursor 是每行的集合。使用 moveToNext() 定位下一行
    public static List<Word> addintolist(Cursor cursor) {
        List<Word> list_show = new ArrayList<>();
        while (cursor.moveToNext()) {
            Word word = new Word();
            word.setId(cursor.getInt(cursor.getColumnIndex(WordsDB.wordsDB._ID)));
            word.setWord(cursor.getString(cursor.getColumnIndex(WordsDB.wordsDB.Column_Name_Word)));
          //  word.setType(cursor.getString(cursor.getColumnIndex(WordsDB.wordsDB.Column_Name_Type)));
            word.setMeaning(cursor.getString(cursor.getColumnIndex(WordsDB.wordsDB.Column_Name_Meaning)));
            word.setSample(cursor.getString(cursor.getColumnIndex(WordsDB.wordsDB.Column_Name_Sample)));
            list_show.add(word);
        }
        return list_show;
    }
}
