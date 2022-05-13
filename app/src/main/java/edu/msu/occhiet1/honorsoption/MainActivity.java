package edu.msu.occhiet1.honorsoption;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public final static String INSTRUMENT_SELECTION = "edu.msu.occhiet1.honorsoption.INSTRUMENT_SELECTION";

    /**
     * The user instrument selection from the dropdown menu
     * <p>
     * 0 = horn
     * 1 = trumpet
     * 2 = trombone
     * 3 = tuba
     */
    private int instrumentSelection = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = (Spinner) findViewById(R.id.instrumentDropdown);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.instrument_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view,
                                       int pos, long id) {
                instrumentSelection = pos;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });
    }

    /**
     * This starts the harmonic series activity
     * Passes an integer representing the instrument chosen through the intent
     *
     * @param view
     */
    public void onStartHarmonicSeries(View view) {
        Intent intent = new Intent(this, HarmonicSeriesActivity.class);
        intent.putExtra(INSTRUMENT_SELECTION, instrumentSelection);
        startActivity(intent);
    }
}
