package com.example.luyue.vocabularybook3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by luyue on 2017/10/22.
 */

public class WordsDBHelper extends SQLiteOpenHelper {
    private final static String DATABASE_Name = "WordSDB";//数据库的名字
    private final static int DATABASE_Version = 1 ; //数据库版本


    //建表SQL
    private final static String SQL_CREATE_DATABASE = "Create Table "+
            WordsDB.wordsDB.Table_Name+"("+
            WordsDB.wordsDB._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            WordsDB.wordsDB.Column_Name_Word+" TEXT,"+
            WordsDB.wordsDB.Column_Name_Type+" TEXT,"+
            WordsDB.wordsDB.Column_Name_Meaning+" TEXT,"+
            WordsDB.wordsDB.Column_Name_Sample+" TEXT)";

    //删表SQL
    private final static String SQL_DELETE_DATABASE = "DROP TABLE IF EXISTS " + WordsDB.wordsDB.Table_Name;


    public WordsDBHelper(Context context){
        super(context,DATABASE_Name,null,DATABASE_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        //创建数据库
        sqLiteDatabase.execSQL(SQL_CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase,int oldVersion,int newVersion){
        //当数据库升级时被调用，首先删除旧表，然后调用OnCreate()创建新表
        sqLiteDatabase.execSQL(SQL_DELETE_DATABASE);
        onCreate(sqLiteDatabase);
    }
}



