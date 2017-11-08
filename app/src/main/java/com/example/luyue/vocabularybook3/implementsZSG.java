package com.example.luyue.vocabularybook3;
import android.provider.BaseColumns;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.UserDictionary;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by luyue on 2017/10/22.
 */

public class implementsZSG implements ZSG {
    private Context context;
    private static WordsDBHelper db;

    public implementsZSG() {
        context = WordApplication.getContext();
        db = new WordsDBHelper(context);
    }


    public implementsZSG(Context context) {
        this.context = context;
        db = new WordsDBHelper(context);
    }

   @Override
   public void insertWord(Word word){
       String sql = "insert into Table_words(word,meaning,sample) values(?,?,?)";
       SQLiteDatabase DB = db.getWritableDatabase();
       DB.execSQL(sql,new String[]{word.getWord(),word.getMeaning(),word.getSample()});
   }

   @Override
   public void updateWord(Word word){
       String sql = "update Table_words set word=?,meaning=?,sample=? where _id=" + word.getId();
       SQLiteDatabase DB = db.getWritableDatabase();
       DB.execSQL(sql,new String[]{word.getWord(),word.getMeaning(),word.getSample()});
   }

    @Override
    public void deleteWord(String wordContent) {
        String sql = "delete from Table_words where word='" + wordContent +"'";
        SQLiteDatabase DB = db.getWritableDatabase();
        DB.execSQL(sql);
    }

    @Override
    public Word getWordById(int wordId) {
        Word w = null;
        SQLiteDatabase DB = implementsZSG.db.getReadableDatabase();
        String sql = "select * from words where _ID=?";
        Cursor cursor = DB.rawQuery(sql, new String[]{wordId + ""});
        if (cursor.moveToNext()) {
            w = new Word();
            w.setId(cursor.getInt(cursor.getColumnIndex(WordsDB.wordsDB._ID)));
            w.setWord(cursor.getString(cursor.getColumnIndex(WordsDB.wordsDB.Column_Name_Word)));
            w.setMeaning(cursor.getString(cursor.getColumnIndex(WordsDB.wordsDB.Column_Name_Meaning)));
            w.setSample(cursor.getString(cursor.getColumnIndex(WordsDB.wordsDB.Column_Name_Sample)));
        }
        close();
        return w;
    }


    @Override
    public Word getWordByContent(String word) {
        Word word1 = null;
        SQLiteDatabase DB = implementsZSG.db.getReadableDatabase();
        String sql = "select * from "+WordsDB.wordsDB.Table_Name+" where word=?";
        Cursor cursor = DB.rawQuery(sql, new String[]{word});
        if (cursor.moveToNext()) {
            word1 = new Word();
            word1.setId(cursor.getInt(cursor.getColumnIndex(WordsDB.wordsDB._ID)));
            word1.setWord(cursor.getString(cursor.getColumnIndex(WordsDB.wordsDB.Column_Name_Word)));
            //word1.setType(cursor.getString(cursor.getColumnIndex(WordsDB.wordsDB.Column_Name_Type)));
            word1.setMeaning(cursor.getString(cursor.getColumnIndex(WordsDB.wordsDB.Column_Name_Meaning)));
            word1.setSample(cursor.getString(cursor.getColumnIndex(WordsDB.wordsDB.Column_Name_Sample)));
        }
        close();
        return word1;
    }

    //使用Sql语句模糊查找
    public List<Word> SearchUseSql(String strWordSearch) {
        SQLiteDatabase DB = implementsZSG.db.getReadableDatabase();
        String sql = "select * from "+WordsDB.wordsDB.Table_Name+" where word like ? order by word desc";
        Cursor cursor = DB.rawQuery(sql, new String[]{"%" + strWordSearch + "%"});
        return Tongyong.addintolist(cursor);
    }


    public void close() {
        if (db != null)
            db.close();
    }

    @Override
    public List<Word> getAllWords() {
        SQLiteDatabase DB = db.getWritableDatabase();
        String[] projection = {
                WordsDB.wordsDB._ID,
                WordsDB.wordsDB.Column_Name_Word,
               // WordsDB.wordsDB.Column_Name_Type,
                WordsDB.wordsDB.Column_Name_Meaning,
                WordsDB.wordsDB.Column_Name_Sample,
        };

        String sortOrder = WordsDB.wordsDB.Column_Name_Word + " ASC";

        Cursor cursor1 = DB.query(WordsDB.wordsDB.Table_Name, projection, null, null, null, null, sortOrder);
        return Tongyong.addintolist(cursor1);
    }
}
