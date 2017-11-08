package com.example.luyue.vocabularybook3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import static com.example.luyue.vocabularybook3.R.layout.activity_main;

public class MainActivity extends AppCompatActivity implements mainView,WordListFragment.itemListener,SearchView.OnQueryTextListener{
    private View insertWordDialog;
    private WordYOUDAO presenter;
    ZSG db;

    private SearchView searchView;

    private ListView mListView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new WordYOUDAO(this);

        db = presenter.db;
        setContentView(activity_main);
        // db.onUpgrade(db.getReadableDatabase(),1,2);
        searchView = (SearchView)findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(false);

        View root = getLayoutInflater().inflate(R.layout.word_list,null);
        mListView = (ListView)root.findViewById(R.id.ListOfWords);
    }

    @Override
    public void onItemClick(String wordContent) {
        Word w = presenter.getWordByContent(wordContent);
        if (Tongyong.isLand(this)) {
            updateDetailFragment(w);
        } else {
            Intent intent = new Intent(MainActivity.this, WordListDetailsActivity.class);
            intent.putExtra("word", w);
            startActivity(intent);
        }
    }

    @Override
    public void updateDetailFragment(Word w) {
        Bundle args = new Bundle();
        args.putSerializable("word", w);
        WordListDetailsFragment fragment = new WordListDetailsFragment();
        fragment.setArguments(args);
        getFragmentManager().beginTransaction().replace(R.id.frag_word_detail, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuonthetop, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret = false;
        if (item.getItemId() == R.id.menu_item_add_word) {
            showAddWordDialog();
            ret = true;
        }
        if (item.getItemId() == R.id.menu_item_translate){

            Intent intent=
                    new Intent(MainActivity.this,TranslateActivity.class);
            startActivity(intent);

            ret = true;
        }
        return ret;
    }
    /*
        private TextView mTextMessage;

        private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        mTextMessage.setText(R.string.title_home);
                        return true;
                    case R.id.navigation_dashboard:
                        mTextMessage.setText(R.string.title_dashboard);
                        return true;
                    case R.id.navigation_notifications:
                        mTextMessage.setText(R.string.title_notifications);
                        return true;
                }
                return false;
            }

        };
    */

        /*
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
      */

    @Override
    public void showAddWordDialog() {
        insertWordDialog = getLayoutInflater().inflate(R.layout.word_add, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("新增单词");//标题

        builder.setView(insertWordDialog);//设置视图
        //确定按钮及其动作
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final EditText text_word = (EditText) insertWordDialog.findViewById(R.id.ed_Word);
                final EditText text_meaning = (EditText) insertWordDialog.findViewById(R.id.ed_Meaning);
               // final EditText text_sample = (EditText) insertWordDialog.findViewById(R.id.ed_Simple);
/*
                new Thread(new Runnable() {
                    @Override
                    public void run() {
*/
                presenter.insertWord(new Word(0, text_word.getText() + "", "",text_meaning.getText() + ""));
                refreshListFragment();

  /*                  }
                }).start();
*/
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
    public void refreshListFragment() {
        WordListFragment wordListFragment = (WordListFragment) getFragmentManager().findFragmentById(R.id.frag_word_list);
        wordListFragment.itemListener = this;
        wordListFragment.refresh();
    }


    public void refreshListFragment(String wordContent) {
        WordListFragment wordListFragment = (WordListFragment) getFragmentManager().findFragmentById(R.id.frag_word_list);
        wordListFragment.itemListener = this;
        wordListFragment.refresh(wordContent);
    }

    @Override
    public void refreshFuzzyListFragment(String text) {
        WordListFragment wordListFragment = (WordListFragment)getFragmentManager().findFragmentById(R.id.frag_word_list);
        wordListFragment.itemListener = this;
        wordListFragment.refreshFuzzyQuery(text);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        try {
            if (TextUtils.isEmpty(newText)) {
                refreshListFragment();
            } else {
                refreshFuzzyListFragment(newText);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}