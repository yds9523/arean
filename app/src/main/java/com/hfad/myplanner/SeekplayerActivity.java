package com.hfad.myplanner;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ListView;

public class SeekplayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seekplayer);

        ListView player_list = (ListView) findViewById(R.id.player_list);
    }
}
