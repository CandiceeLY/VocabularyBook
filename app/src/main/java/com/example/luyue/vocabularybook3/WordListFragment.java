package com.example.luyue.vocabularybook3;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import java.util.List;

/**
 * Created by luyue on 2017/10/22.
 */

public class WordListFragment extends ListFragment {
    public itemListener itemListener;
    private List<Word> words;
    private ZSG db;
    private Word wordd;

    private EditText text_word;
    private EditText text_meaning;
    private EditText text_sample;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new implementsZSG(getActivity()) ;
        words = db.getAllWords(); // 没有初始化就调用了，值为null，所以会闪退，而且是xml加载，所以报不出错误
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.word_list, container, false);
        if (view instanceof ListView) {
            setListAdapter(new WordListAdapter(getActivity(), words));
            ListView list_words = (ListView) view;
            registerForContextMenu(list_words);
        }
        return view;
    }

   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof itemListener) {
            itemListener = (itemListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement itemListener");
        }
    }*/

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        Context context = activity.getApplicationContext();

        if (activity instanceof itemListener) {
            itemListener = (itemListener) activity;
        } else {
            throw new RuntimeException(context.toString() + " must implement itemListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        itemListener = null;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.menucontext, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        View clickedItem = info.targetView;
        TextView tv_word = (TextView) clickedItem.findViewById(R.id.ItemOfWordslist);
        switch (item.getItemId()) {
            case R.id.menu_item_update_word:
                updateWord(tv_word.getText() + "");
                break;
            case R.id.menu_item_delete_word:
                db.deleteWord(tv_word.getText() + "");
                refresh();
                break;
        }
        return true;
    }

    private void updateWord(final String word) {
        wordd = db.getWordByContent(word); // wordd 啥
        View updateWordDialog = getActivity().getLayoutInflater().inflate(R.layout.word_add, null);
        text_word = (EditText) updateWordDialog.findViewById(R.id.ed_Word);
        text_meaning = (EditText) updateWordDialog.findViewById(R.id.ed_Meaning);
        //checkBox_Type1 = (CheckBox) updateWordDialog.findViewById(R.id.checkboxN);
        //text_sample = (EditText) updateWordDialog.findViewById(R.id.ed_Simple);
        text_word.setText(wordd.getWord());
        text_meaning.setText(wordd.getMeaning());
        //checkBox_Type1.setOnClickListene\r();
       // text_sample.setText(wordd.getSample());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("修改单词");//标题
        builder.setView(updateWordDialog);//设置视图
        //确定按钮及其动作
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Word newWord = db.getWordByContent(wordd.getWord());
                newWord.setWord(text_word.getText() + "");
                newWord.setMeaning(text_meaning.getText() + "");
               // newWord.setSample(text_sample.getText() + "");
                db.updateWord(newWord);
                //单词已经插入到数据库，更新显示列表
                refresh(wordd.getWord());
            }
        });
        //取消按钮及其动作
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create();//创建对话框
        builder.show();//显示对话框
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String s = ((TextView)v).getText()+"";
        itemListener.onItemClick(s);
    }

    public void refresh() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                words = db.getAllWords();
                setListAdapter(new WordListAdapter(getActivity(), words));
            }
        }, 500);
    }

    public void refresh(String wordContent) {
        refresh();
        if (Tongyong.isLand(getActivity())) {
            itemListener.onItemClick(wordContent);
        }
    }

    public void refreshFuzzyQuery(final String text) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                words = db.SearchUseSql(text);
                setListAdapter(new WordListAdapter(getActivity(), words));
            }
        }, 400);
    }

    public interface itemListener {
        void onItemClick(String wordContent);
        void refreshListFragment();
        void refreshFuzzyListFragment(String text);
    }

}
