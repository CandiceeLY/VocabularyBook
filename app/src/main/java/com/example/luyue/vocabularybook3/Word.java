package com.example.luyue.vocabularybook3;

import java.io.Serializable;

/**
 * Created by luyue on 2017/10/22.
 */

public class Word implements Serializable {
    private int id;
    private String word;
    private String type;
    private String meaning;
    private String sample;

    public Word() {
    }

    public Word(int id, String word,String type, String meaning) {
        this.id = id;
        this.word = word;
       // this.type = type;
        this.meaning = meaning;
        this.sample = sample;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    /*public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }*/

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }
}
