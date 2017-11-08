package com.example.luyue.vocabularybook3;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by luyue on 2017/10/22.
 */

public class WordsDB {

    public static final String AUTHORITY = "com.example.luyue.wordsprovider";//URI授权者

    public WordsDB(){
    }

    public static abstract class wordsDB implements BaseColumns {
        public static final String Table_Name="Table_words";
        public static final String _ID=BaseColumns._ID;
        public static final String Column_Name_Word="word";
        public static final String Column_Name_Type="type";
        public static final String Column_Name_Sample="sample";
        public static final String Column_Name_Meaning="meaning";

       //MIME类型
        public static final String MIME_DIR_PREFIX = "vnd.android.cursor.dir";
        public static final String MIME_ITEM_PREFIX = "vnd.android.cursor.item";
        public static final String MINE_ITEM = "vnd.com.example.luyue.Table_words";

        public static final String MINE_TYPE_SINGLE = MIME_ITEM_PREFIX + "/" + MINE_ITEM;
        public static final String MINE_TYPE_MULTIPLE = MIME_DIR_PREFIX + "/" + MINE_ITEM;

        public static final String PATH_SINGLE = "Table_words/#";//单条数据的路径
        public static final String PATH_MULTIPLE = "Table_words";//多条数据的路径

        //Content Uri
        public static final String CONTENT_URI_STRING = "content://" + AUTHORITY + "/" + PATH_MULTIPLE;
        //public static final Uri CONTENT_URI = Uri.parse("content://com.example.luyue.wordsprovider");
        public static final Uri  CONTENT_URI = Uri.parse(CONTENT_URI_STRING);

    }


}
