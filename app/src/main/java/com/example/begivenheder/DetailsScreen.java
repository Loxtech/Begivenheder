package com.example.begivenheder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetailsScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Event event = getEventFromIntent();
        if (event != null) {
            updateUIWithEventData(event);
        }
    }

    private Event getEventFromIntent() {
        return (Event) getIntent().getSerializableExtra("event");
    }

    private void updateUIWithEventData(Event event) {
        TextView tvName = findViewById(R.id.tvDetailName);
        TextView tvDate = findViewById(R.id.tvDetailDate);
        TextView tvDescription = findViewById(R.id.tvDetailFullDescription);
        Button btnOpenBrowser = findViewById(R.id.btnOpenBrowser);
        Button btnBack = findViewById(R.id.btnBack);

        // Opdater UI med begivenhedens data
        tvName.setText(event.getName());
        tvDate.setText("Dato: " + event.getDate());
        tvDescription.setText(event.getFullDescription());

        // "Åbn i browser"-knap funktionalitet
        btnOpenBrowser.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.getLink()));
            startActivity(browserIntent);
        });

        // "Tilbage"-knap funktionalitet
        btnBack.setOnClickListener(v -> {
            finish(); // Lukker denne aktivitet og går tilbage til den forrige
        });
    }
}