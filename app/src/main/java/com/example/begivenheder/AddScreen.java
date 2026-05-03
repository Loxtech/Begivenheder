package com.example.begivenheder;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.Locale;

public class AddScreen extends AppCompatActivity {

    private EditText etName, etDate, etShortDesc, etFullDesc, etLink;
    private Button btnSave, btnCancel;
    private com.google.android.material.textfield.TextInputLayout tilDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        etName = findViewById(R.id.etName);
        etDate = findViewById(R.id.etDate);
        etShortDesc = findViewById(R.id.etShortDesc);
        etFullDesc = findViewById(R.id.etFullDesc);
        etLink = findViewById(R.id.etLink);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        tilDate = findViewById(R.id.tilDate);

        // Sæt dags dato som standard
        setTodayDate();
    }

    private void setTodayDate() {
        final Calendar c = Calendar.getInstance();
        String today = String.format(new Locale("da", "DK"), "%02d. %s %d",
                c.get(Calendar.DAY_OF_MONTH), getMonthName(c.get(Calendar.MONTH)), c.get(Calendar.YEAR));
        etDate.setText(today);
    }

    private void setupListeners() {
        // DatePicker setup - både for klik på tekstfelt og på ikonet
        etDate.setOnClickListener(v -> showDatePicker());
        tilDate.setStartIconOnClickListener(v -> showDatePicker());

        btnSave.setOnClickListener(v -> saveEvent());

        btnCancel.setOnClickListener(v -> finish());
    }

    /**
     * Viser en DatePickerDialog og opdaterer tekstfeltet med den valgte dato.
     */
    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = String.format(new Locale("da", "DK"), "%02d. %s %d",
                            dayOfMonth, getMonthName(monthOfYear), year1);
                    etDate.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private String getMonthName(int month) {
        String[] months = {"Januar", "Februar", "Marts", "April", "Maj", "Juni", 
                           "Juli", "August", "September", "Oktober", "November", "December"};
        return months[month];
    }

    /**
     * Validerer input og sender den nye begivenhed tilbage til StartScreen.
     */
    private void saveEvent() {
        String name = etName.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String shortDesc = etShortDesc.getText().toString().trim();
        String fullDesc = etFullDesc.getText().toString().trim();
        String link = etLink.getText().toString().trim();

        if (name.isEmpty() || date.isEmpty() || shortDesc.isEmpty() || fullDesc.isEmpty()) {
            Toast.makeText(this, "Udfyld venligst alle felter (undtagen link)", Toast.LENGTH_SHORT).show();
            return;
        }

        Event newEvent = new Event(name, date, shortDesc, fullDesc, link);
        
        Intent resultIntent = new Intent();
        resultIntent.putExtra("newEvent", newEvent);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}