package edu.msu.occhiet1.honorsoption;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import java.util.Random;

public class HarmonicSeriesActivity extends AppCompatActivity {

    private Cursor cursor = new Cursor();

    private RadioButton previousRadioButton = null;

    private int instrumentSelection = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harmonic_series);

        // Make the natural button blue initially
        // set up state so natural button is the previous button
        RadioButton button =findViewById(R.id.naturalRadioButton);
        button.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
        previousRadioButton = button;

        // Set the cursor
        getHarmonicSeriesView().setCursor(cursor);

        // initialize answer harmonic series using instrument passed from activity
        instrumentSelection = getIntent().getIntExtra(MainActivity.INSTRUMENT_SELECTION, 0);
        setUpCorrectHarmonicSeries();
    }

    public void setUpCorrectHarmonicSeries(){
        Random random = new Random();

        Harmonic.Accidentals accidental = Harmonic.Accidentals.NATURAL;
        int octave = 0;
        int pitchClass = 0;

        switch(instrumentSelection){
            // Horn
            // Harmonics range from F#1 to F2
            case 0:{
                pitchClass = (int)(random.nextDouble() * (12 + 1));
                accidental = getAccidental(pitchClass);

                if(pitchClass <= 5){
                    octave = 2;
                }else{
                    octave = 1;
                }

                break;
            }
            // Trumpet
            // Harmonics range from F#2 to C3
            case 1:{
                // Mod 12 so that a result of 12 yields the correct pitch class of zero
                pitchClass = ((int)(random.nextDouble() * (13 - 6 + 1) + 6)) % 12;
                accidental = getAccidental(pitchClass);

                if(pitchClass == 0){
                    octave = 3;
                }else{
                    octave = 2;
                }

                break;
            }
            // Trombone
            // Harmonics range from E1 to Bb1
            case 2:{
                pitchClass = (int)(random.nextDouble() * (11 - 4 + 1) + 4);
                accidental = getAccidental(pitchClass);

                octave = 1;

                break;
            }

            // Tuba
            // Harmonics range from F#0 to C1
            // Why did it generate Dd?????
            case 3:{
                // Mod 12 so that a result of 12 yields the correct pitch class of zero
                pitchClass = ((int)(random.nextDouble() * (13 - 6 + 1) + 6)) % 12;
                accidental = getAccidental(pitchClass);

                if(pitchClass == 0){
                    octave = 1;
                }else{
                    octave = 0;
                }

                break;
            }
        }

        // Pass to view to set up harmonic series
        getHarmonicSeriesView().setUpCorrectHarmonicSeries(octave, pitchClass, accidental);

    }

    private HarmonicSeriesView getHarmonicSeriesView(){ return findViewById(R.id.harmonicSeriesView);}

    /**
     * Gets the key type for a given key
     *
     * @param pc pitch class we are getting key for
     * @return accidental of that key
     */
    private Harmonic.Accidentals getAccidental(int pc){
        switch(pc){
            case 0:{
                return Harmonic.Accidentals.NATURAL;
            }case 1:
            case 3:
            case 5:
            case 8:
            case 10: {
                return Harmonic.Accidentals.FLAT;
            }case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11: {
                return Harmonic.Accidentals.SHARP;
            }
        }

        return Harmonic.Accidentals.NATURAL;
    }






    public void radioButtonClicked(View view){
        RadioButton button = (RadioButton)view;
        int id = view.getId();

        // Check which radio button was clicked and update the cursor
        if(id == R.id.naturalRadioButton){
            cursor.setAccidental(Harmonic.Accidentals.NATURAL);
        }else if (id == R.id.sharpRadioButton){
            cursor.setAccidental(Harmonic.Accidentals.SHARP);
        }else if (id == R.id.flatRadioButton){
            cursor.setAccidental(Harmonic.Accidentals.FLAT);
        }

        // Make the unselected button black, and the new selected button blue

        previousRadioButton.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
        button.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));

        previousRadioButton = button;
    }

    /** Adds a harmonic with an accidental based on which radio button is toggled
     *
     * @param view
     */
    public void onAddNoteClicked(View view){
        cursor.addHarmonic();
    }

    /** Click handler for checking whether user inputted harmonic series is correct
     *
     * @param view
     */
    public void onCheckSeriesClicked(View view){
        // Make the natural button blue initially
        // set up state so natural button is the previous button
        HarmonicSeriesView harmonicSeriesView =findViewById(R.id.harmonicSeriesView);
        String result = harmonicSeriesView.checkSeries();

        if(result.length() == 0){
            CorrectAnswerDialog correctAnswerDialog = new CorrectAnswerDialog();
            correctAnswerDialog.show(this.getSupportFragmentManager(), "correct");
        }else{
            IncorrectAnswerDialog incorrectAnswerDialog = new IncorrectAnswerDialog();

            Bundle bundle = new Bundle();
            bundle.putString("INCORRECT", result);
            incorrectAnswerDialog.setArguments(bundle);
            incorrectAnswerDialog.show(this.getSupportFragmentManager(), "incorrect");
        }
    }





}