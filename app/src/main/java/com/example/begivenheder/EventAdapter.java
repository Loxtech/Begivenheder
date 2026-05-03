package com.example.begivenheder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {
    private List<Event> events;
    private Context context;

    public EventAdapter(@NonNull Context context, @NonNull List<Event> events) {
        super(context, R.layout.event_item, events);
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Genbrug view hvis muligt, ellers opret et nyt
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.event_item, parent, false);
        }

        Event event = events.get(position);

        TextView tvName = convertView.findViewById(R.id.tvEventName);
        TextView tvDate = convertView.findViewById(R.id.tvEventDate);
        TextView tvShortDesc = convertView.findViewById(R.id.tvEventShortDescription);
        ImageView ivEventImage = convertView.findViewById(R.id.ivEventItemImage);
        ImageButton btnDelete = convertView.findViewById(R.id.btnDelete);
        Button btnDetails = convertView.findViewById(R.id.btnDetails);
        Button btnRegister = convertView.findViewById(R.id.btnRegister);

        tvName.setText(event.getName());
        tvDate.setText(event.getDate());
        tvShortDesc.setText(event.getShortDescription());

        // Slet-knap funktionalitet
        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Slet begivenhed")
                    .setMessage("Er du sikker på, at du vil slette '" + event.getName() + "'?")
                    .setPositiveButton("Slet", (dialog, which) -> {
                        if (context instanceof StartScreen) {
                            StartScreen startScreen = (StartScreen) context;
                            startScreen.getAllEvents().remove(event);
                            startScreen.saveEvents();
                            startScreen.filterEvents(""); // Opdaterer listen
                        }
                    })
                    .setNegativeButton("Annuller", null)
                    .show();
        });

        // Håndter billede i listen med Glide
        // Vi bruger centerCrop for at sikre at billedet fylder containeren pænt
        if (event.getImageUri() != null) {
            ivEventImage.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(Uri.parse(event.getImageUri()))
                    .centerCrop()
                    .into(ivEventImage);
        } else {
            ivEventImage.setVisibility(View.GONE);
        }

        // Opdater knap-udseende baseret på tilmeldingsstatus
        updateRegisterButton(btnRegister, event.isRegistered());

        btnDetails.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailsScreen.class);
            intent.putExtra("event", event);
            context.startActivity(intent);
        });

        // Logik for Tilmeld/Afmeld knappen
        btnRegister.setOnClickListener(v -> {
            if (event.isRegistered()) {
                // Afmeldings-dialog
                new AlertDialog.Builder(context)
                        .setTitle("Afmelding")
                        .setMessage("Vil du afmelde dig " + event.getName() + "?")
                        .setPositiveButton("Ja, afmeld", (dialog, which) -> {
                            event.setRegistered(false);
                            updateRegisterButton(btnRegister, false);
                            // Gem den nye tilmeldingsstatus
                            if (context instanceof StartScreen) {
                                ((StartScreen) context).saveEvents();
                            }
                        })
                        .setNegativeButton("Annuller", null)
                        .show();
            } else {
                // Tilmeldings-dialog
                new AlertDialog.Builder(context)
                        .setTitle("Tilmelding")
                        .setMessage("Vil du tilmelde dig " + event.getName() + "?")
                        .setPositiveButton("Ja, tilmeld", (dialog, which) -> {
                            event.setRegistered(true);
                            updateRegisterButton(btnRegister, true);
                            // Gem den nye tilmeldingsstatus
                            if (context instanceof StartScreen) {
                                ((StartScreen) context).saveEvents();
                            }
                        })
                        .setNegativeButton("Annuller", null)
                        .show();
            }
        });

        return convertView;
    }

    @SuppressLint("SetTextI18n")
    private void updateRegisterButton(Button button, boolean isRegistered) {
        if (isRegistered) {
            button.setText("Tilmeldt!");
            button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green)));
        } else {
            button.setText("Tilmeld");
            button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.accent)));
        }
    }
}