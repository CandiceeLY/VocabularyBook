package com.example.luyue.vocabularybook3;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class WordsProvider extends ContentProvider {
    //UriMathcher匹配结果码
    private static final int MULTIPLE_WORDS = 1;
    private static final int SINGLE_WORD = 2;

    private SQLiteDatabase db;
    private WordsDBHelper wordsDBHelper;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(WordsDB.AUTHORITY, WordsDB.wordsDB.PATH_SINGLE, SINGLE_WORD);
        uriMatcher.addURI(WordsDB.AUTHORITY, WordsDB.wordsDB.PATH_MULTIPLE, MULTIPLE_WORDS);
    }

    public WordsProvider() {

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case MULTIPLE_WORDS:
                count = db.delete(WordsDB.wordsDB.Table_Name, selection, selectionArgs);
                break;
            case SINGLE_WORD:
                String whereClause = WordsDB.wordsDB._ID + "=" + uri.getPathSegments().get(1);
                count = db.delete(WordsDB.wordsDB.Table_Name, whereClause, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unkonwn Uri:" + uri);
        }
        //通知ContentResolver,数据已经发生改变
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case MULTIPLE_WORDS://多条数据记录
                return WordsDB.wordsDB.MINE_TYPE_MULTIPLE;
            case SINGLE_WORD://单条数据记录
                return WordsDB.wordsDB.MINE_TYPE_SINGLE;
            default:
                throw new IllegalArgumentException("Unkonwn Uri:" + uri);
        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        long id = db.insert(WordsDB.wordsDB.Table_Name, null, values);
        if ( id > 0 ){
            //在已有的Uri后面添加id
            Uri newUri = ContentUris.withAppendedId(WordsDB.wordsDB.CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(newUri, null);
            return newUri;
        }
        throw new SQLException("Failed to insert row into " + uri);

    }

    @Override
    public boolean onCreate() {
        wordsDBHelper = new WordsDBHelper(getContext());
        db = wordsDBHelper.getReadableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(WordsDB.wordsDB.Table_Name);

        switch (uriMatcher.match(uri)) {
            case MULTIPLE_WORDS:
                return db.query(WordsDB.wordsDB.Table_Name, projection, selection, selectionArgs, null, null, sortOrder);

            case SINGLE_WORD:
                qb.appendWhere(WordsDB.wordsDB._ID + "=" + uri.getPathSegments().get(1));
                return qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

            default:
                throw new IllegalArgumentException("Unkonwn Uri:" + uri);
        }

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        db = wordsDBHelper.getReadableDatabase();
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case MULTIPLE_WORDS:
                count = db.update(WordsDB.wordsDB.Table_Name, values, selection, selectionArgs);
                break;
            case SINGLE_WORD:
                String segment = uri.getPathSegments().get(1);
                count = db.update(WordsDB.wordsDB.Table_Name, values, WordsDB.wordsDB._ID +"="+segment, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unkonwn Uri:" + uri);
        }

        //通知ContentResolver,数据已经发生改变
        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }
}
