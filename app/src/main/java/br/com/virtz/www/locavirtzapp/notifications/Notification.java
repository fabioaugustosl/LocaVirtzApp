package br.com.virtz.www.locavirtzapp.notifications;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class Notification {

    private String title;
    private String text;
    private Intent intent;
    private Context context;


    public Notification(String title, String text, Intent intent, Context context) {
        this.title = title;
        this.text = text;
        this.intent = intent;
        this.context = context;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }
}
