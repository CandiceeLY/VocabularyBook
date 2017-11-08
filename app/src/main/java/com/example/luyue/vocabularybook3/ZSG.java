package com.example.luyue.vocabularybook3;

import android.app.ListFragment;
import android.widget.ListView;

import java.util.List;

/**
 * Created by luyue on 2017/10/22.
 */

public interface ZSG {
    void insertWord(Word word);
    void updateWord(Word word);
    void deleteWord(String wordContent);
    List<Word>SearchUseSql(String strWordSearch);
    Word getWordById(int wordId);
    Word getWordByContent(String word);
    List<Word> getAllWords();
}
