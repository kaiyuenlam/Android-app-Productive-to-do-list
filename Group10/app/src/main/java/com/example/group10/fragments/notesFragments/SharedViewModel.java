package com.example.group10.fragments.notesFragments;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<String> title = new MutableLiveData<String>();
    private MutableLiveData<String> content = new MutableLiveData<String>();

    //getter and setter
    public MutableLiveData<String> getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title.setValue(title);
    }

    public MutableLiveData<String> getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content.setValue(content);
    }


}
