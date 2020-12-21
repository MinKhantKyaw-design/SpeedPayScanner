package com.example.speedpayscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        TextView textView=(TextView) this.findViewById(R.id.text);

        SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
        String value = sharedPreferences.getString("value","");

        if (value!="") {
            textView.setText(value);
        }

        String a = textView.getText().toString();

        Intent intent=getIntent();
        String b=intent.getStringExtra("Amount");

        if (b!=null) {
            int first = Integer.parseInt(a);
            int second = Integer.parseInt(b);
            int result = first-second;

            value = (""+result);
            SharedPreferences sharedPref = getSharedPreferences("myKey", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("value", value);
            editor.apply();

            textView.setText(value);
        }



        FloatingActionButton floatingActionButton=(FloatingActionButton)findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, Scanner.class);
                startActivity(intent);
                finish();
            }
        });


    }



}
