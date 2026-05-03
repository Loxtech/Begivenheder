package com.example.begivenheder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

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

    @SuppressLint("SetTextI18n")
    private void updateUIWithEventData(Event event) {
        TextView tvName = findViewById(R.id.tvDetailName);
        TextView tvDate = findViewById(R.id.tvDetailDate);
        TextView tvDescription = findViewById(R.id.tvDetailFullDescription);
        ImageView ivDetailImage = findViewById(R.id.ivDetailImage);
        Button btnOpenBrowser = findViewById(R.id.btnOpenBrowser);
        Button btnBack = findViewById(R.id.btnBack);

        // Opdater UI med begivenhedens data
        tvName.setText(event.getName());
        tvDate.setText("Dato: " + event.getDate());
        tvDescription.setText(event.getFullDescription());

        /*
          Indlæser begivenhedens billede.
          Glide håndterer automatisk hukommelse, skalering og placeholders.
         */
        if (event.getImageUri() != null) {
            Glide.with(this)
                    .load(Uri.parse(event.getImageUri()))
                    .placeholder(R.drawable.ic_launcher_background) // Vises mens vi henter
                    .error(R.drawable.ic_launcher_background)       // Vises ved fejl
                    .into(ivDetailImage);
        }

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