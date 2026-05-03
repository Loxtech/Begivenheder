package com.example.begivenheder;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StartScreen extends AppCompatActivity {

    private List<Event> allEvents;
    private List<Event> displayedEvents;
    private EventAdapter adapter;

    /**
     * Launcher til at starte AddScreen og modtage resultatet (den nye begivenhed) tilbage.
     */
    private final ActivityResultLauncher<Intent> addEventLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Event newEvent = (Event) result.getData().getSerializableExtra("newEvent");
                    if (newEvent != null) {
                        allEvents.add(newEvent);
                        sortEvents(); // Sorter efter dato
                        saveEvents(); // Gem i SharedPreferences
                        filterEvents(""); // Opdater visning
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeDataAndAdapter();
        setupListView();
        setupFab();
        setupSearch();
    }

    private void initializeDataAndAdapter() {
        initializeData();
        sortEvents();
        displayedEvents = new ArrayList<>(allEvents);
        adapter = new EventAdapter(this, displayedEvents);
    }

    private void setupListView() {
        ListView lvEvents = findViewById(R.id.lvEvents);
        lvEvents.setAdapter(adapter);
    }

    private void setupFab() {
        FloatingActionButton fabAddEvent = findViewById(R.id.fabAddEvent);
        fabAddEvent.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddScreen.class);
            addEventLauncher.launch(intent);
        });
    }

    private void setupSearch() {
        EditText etSearch = findViewById(R.id.etSearch);
        ImageButton btnSearch = findViewById(R.id.btnSearch);

        // Realtids søge-funktionalitet (opdaterer mens man skriver)
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterEvents(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Knappen kan stadig bruges til at lukke tastaturet eller tvinge en søgning
        btnSearch.setOnClickListener(v -> {
            String query = etSearch.getText().toString().toLowerCase();
            filterEvents(query);
        });
    }

    /**
     * Henter data fra SharedPreferences. Hvis tom, oprettes standardlisten.
     */
    private void initializeData() {
        allEvents = loadEvents();
        
        if (allEvents == null || allEvents.isEmpty()) {
            allEvents = new ArrayList<>();
            allEvents.add(new Event("Sommerfest", "15. Juni 2026", "Årets hyggeligste sommerfest.",
                    "Kom og vær med til en fantastisk dag med grill, musik og gode venner.",
                    "https://Google.com"));
            allEvents.add(new Event("IT Konference", "22. September 2026", "Lær om de nyeste teknologier.",
                    "En dag spækket med spændende oplæg fra eksperter inden for AI og Cloud.",
                    "https://Google.com"));
            allEvents.add(new Event("Julefrokost", "1. December 2026", "Traditionel julefrokost.",
                    "Vi fejrer julen med sild, snaps og masser af hygge.",
                    "https://Google.com"));
            saveEvents();
        }
    }

    /**
     * Gemmer hele listen af begivenheder som en JSON-streng i SharedPreferences.
     */
    public void saveEvents() {
        android.content.SharedPreferences sharedPreferences = getSharedPreferences("BegivenhederPrefs", MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(allEvents);
        editor.putString("eventList", json);
        editor.apply();
    }

    /**
     * Indlæser og deserialiserer listen af begivenheder fra SharedPreferences.
     */
    private List<Event> loadEvents() {
        android.content.SharedPreferences sharedPreferences = getSharedPreferences("BegivenhederPrefs", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("eventList", null);
        Type type = new TypeToken<ArrayList<Event>>() {}.getType();
        return gson.fromJson(json, type);
    }

    /**
     * Filtrerer listen af begivenheder baseret på brugerens søgeord.
     */
    private void filterEvents(String query) {
        displayedEvents.clear();
        if (query.isEmpty()) {
            displayedEvents.addAll(allEvents);
        } else {
            for (Event event : allEvents) {
                if (event.getName().toLowerCase().contains(query)) {
                    displayedEvents.add(event);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * Sorterer listen kronologisk baseret på dato-objektet i hver Event.
     */
    private void sortEvents() {
        Collections.sort(allEvents, (e1, e2) -> e1.getDateAsObject().compareTo(e2.getDateAsObject()));
    }
}