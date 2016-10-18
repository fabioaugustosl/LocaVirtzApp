package br.com.virtz.www.locavirtzapp.notifications;

import android.app.Activity;
import android.content.Context;

public class Notification {

    private String title;
    private String text;
    private Activity activity;
    private Context context;


    public Notification(String title, String text, Activity activity, Context context) {
        this.title = title;
        this.text = text;
        this.activity = activity;
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

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
