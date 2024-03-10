package com.example.learndigitalskills.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.learndigitalskills.R;

public class helpPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_page);
    }

    public static Intent getIntent(Context context){
        return new Intent(context, helpPage.class);
    }
}