package com.example.ManageYourSelf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class GridItemActivity extends AppCompatActivity {

    TextView name , detailsText;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_item);


        name = findViewById(R.id.griddata);
        detailsText = findViewById(R.id.detailsText);
        image = findViewById(R.id.imageView);


        Intent intent = getIntent();
        name.setText(intent.getStringExtra("name"));
        detailsText.setText(intent.getStringExtra("details"));
        image.setImageResource(intent.getIntExtra("image",0));
    }
}