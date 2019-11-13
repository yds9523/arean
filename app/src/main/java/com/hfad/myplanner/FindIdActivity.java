package com.hfad.myplanner;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

public class FindIdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);
    }

    public void Find_Password(View view){
        Intent intent = new Intent(this, FindPasswordActivity.class);
        finish();
        startActivity(intent);
    }
    public void Login(View view){
        finish();
    }
}
