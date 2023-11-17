package com.example.androiddemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.getbase.floatingactionbutton.FloatingActionButton;

public class LauncherActivity  extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher_layout);
        final FloatingActionButton actionA = (FloatingActionButton) findViewById(R.id.action_a);
        actionA.setOnClickListener(new View.OnClickListener() {
         @Override
            public void onClick(View view) {
                       actionA.setTitle("Action A clicked");
             Toast.makeText(view.getContext(),"AAA",Toast.LENGTH_LONG).show();
             }
            });
    }
}
