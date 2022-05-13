package edu.msu.occhiet1.honorsoption;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

/**
 * Class which manages the harmonic series
 */
public class HarmonicSeries {

    /**
     * Percentage of the display width or height that
     * is occupied by the puzzle.
     */
    final static float SCALE_IN_VIEW = .9f;

    /**
     * The total number of harmonics that have been added
     *
     * Starts at 1 because the fundamental is always immediately loaded
     */
    private int totalHarmonics = 1;

    /**
     * Cursor responsible for all harmonic insertion/editing
     */
    private Cursor cursor;

    /**
     * The height of the puzzle in pixels
     */
    private int staffHeight;

    /**
     * The width of the puzzle in pixels
     */
    private int staffWidth;

    /**
     * How much we scale the puzzle pieces
     */
    private float scaleFactor;

    /**
     * Left margin in pixels
     */
    private int marginX;

    /**
     * Top margin in pixels
     */
    private int marginY;


    /**\
     * Paint for the bitmap
     */
    private Paint textPaint;

    /**
     * Grant staff image
     */
    private Bitmap grandStaff;

    /**
     * Context we are working in
     */
    private Context appContext;

    /**
     * Array of all harmonics on screen currently
     */
    private Harmonic[] harmonics;

    /**
     * Array of correct harmonics
     */
    private Harmonic[] answerHarmonics;

    /**
     * This variable is set to a harmonic we are dragging. If
     * we are not dragging, the variable is null.
     */
    private Harmonic dragging = null;

    /**
     * Most recent relative X touch when dragging
     */
    private float lastRelX;

    /**
     * Most recent relative Y touch when dragging
     */
    private float lastRelY;

    private HarmonicSeriesView harmonicSeriesView;

    public HarmonicSeries(Context context, HarmonicSeriesView harmonicSeriesView) {
        // Load the grand staff puzzle image
        grandStaff =
                BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.wider_staff_numbers);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        appContext = context;

        this.harmonicSeriesView = harmonicSeriesView;

        // Initialize harmonics array with null harmonics and add fundamental harmonic
        harmonics = new Harmonic[16];
    }


    public void draw(Canvas canvas) {
        int wid = canvas.getWidth();
        int hit = canvas.getHeight();

        staffHeight = (int)(hit * SCALE_IN_VIEW);
        staffWidth = (int)(wid * SCALE_IN_VIEW);

        // Compute the margins so we center the puzzle
        marginX = (wid - staffWidth) / 2;
        marginY = hit - staffHeight;

        int grandStaffWidth = grandStaff.getWidth();

        scaleFactor = (float)staffWidth /
                (float)grandStaffWidth;

        // Draw the staff
        canvas.save();
        canvas.translate(marginX, marginY);
        canvas.scale(scaleFactor, scaleFactor);
        canvas.drawBitmap(grandStaff, 0, 0, null);
        canvas.restore();

        // Draw harmonics & octave designation
        for(Harmonic harmonic: harmonics){
            if(harmonic != null){
                harmonic.draw(canvas, marginX, marginY, staffWidth, staffHeight, scaleFactor);

            }
        }

        // Draw octave displacement indications
        // FIX SCALING AND SCREEN PLACEMENT
        for(Harmonic harmonic: answerHarmonics){
            Paint paint = new Paint();
            paint.setTextSize(40 * scaleFactor);
            paint.setTextAlign(Paint.Align.CENTER);

            canvas.save();
            canvas.translate(0, -40 * scaleFactor / 2);

            int displacement = harmonic.getOctaveDisplacement();

            if(displacement< 0){
                String octaveDesgination = Integer.toString(-(displacement) * 8) + "vb";
                canvas.drawText(octaveDesgination, marginX + harmonic.getX() * staffWidth, marginY +.39f * staffHeight, paint);

            }else if(displacement > 0){
                String octaveDesgination = Integer.toString(displacement * 8) + "va";
                canvas.drawText(octaveDesgination, marginX + harmonic.getX() * staffWidth, marginY , paint);
            }

            canvas.restore();
        }

        // Draw the cursor
        cursor.draw(canvas, marginX, marginY, staffWidth, staffHeight, scaleFactor);

    }

    /**
     * Handle a touch event from the view.
     * @param view The view that is the source of the touch
     * @param event The motion event describing the touch
     * @return true if the touch is handled.
     */
    public boolean onTouchEvent(View view, MotionEvent event) {
        //
        // Convert an x,y location to a relative location in the
        // puzzle.
        //

        float relX = (event.getX() - marginX) / staffWidth;
        float relY = (event.getY() - marginY) / staffHeight;

        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:
                if(cursor.setPosition(relX)){
                    view.invalidate();
                }

                return onTouched(relX, relY);

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                return onReleased(view, relX, relY);

            case MotionEvent.ACTION_MOVE:
                // If we are dragging, move the harmonic and force a redraw
                if(dragging != null) {
                    dragging.move(relY - lastRelY);
                    lastRelX = relX;
                    lastRelY = relY;
                    view.invalidate();
                    return true;
                }

        }

        return false;
    }

    /**
     * Handle a touch message. This is when we get an initial touch
     * @param x x location for the touch, relative to the puzzle - 0 to 1 over the puzzle
     * @param y y location for the touch, relative to the puzzle - 0 to 1 over the puzzle
     * @return true if the touch is handled
     */
    private boolean onTouched(float x, float y) {
        // Check each harmonic to see if it has been hit
        // We do this in reverse order so we find the harmonics in front
        for(int p=harmonics.length-1; p>=0;  p--) {
            if(harmonics[p] != null && harmonics[p].hit(x, y, staffWidth, staffHeight, scaleFactor)) {
                // We hit a piece!
                dragging = harmonics[p];
                lastRelX = x;
                lastRelY = y;
                return true;
            }
        }

        return false;
    }

    /**
     * Handle a release of a touch message.
     * @param x x location for the touch release, relative to the harmonic - 0 to 1 over the puzzle
     * @param y y location for the touch release, relative to the puzzle - 0 to 1 over the puzzle
     * @return true if the touch is handled
     */
    private boolean onReleased(View view, float x, float y) {

        if(dragging != null) {
            if(dragging.maybeSnap()){
                view.invalidate();
            }
            dragging = null;
            return true;
        }

        return false;
    }

    /** Add harmonic into array
     * Called by the cursor
     *
     * @param accidental the accidental that should be applied to this harmonic
     * @param index index in array where harmonic will be instantiated
     * @param x location where harmonic will be inserted
     */
    public void addHarmonic(Harmonic.Accidentals accidental, int index, float x){
        // Answer harmonic will determine how much a user's harmonic needs to be displaced
        harmonics[index] = new Harmonic(appContext, accidental, x, answerHarmonics[index].getOctaveDisplacement());
        totalHarmonics++;

        // If all harmonics are added, enable the playYours/CheckSeries buttons
        if(totalHarmonics == 16){
            harmonicSeriesView.setButtonsEnabled(true);
        }

        harmonicSeriesView.invalidate();
    }

    /** Edit existing harmonic in array
     * Called by the cursor
     *
     * @param accidental the accidental that should be applied to this harmonic
     * @param index index in array where harmonic will be instantiated
     */
    public void editHarmonic(Harmonic.Accidentals accidental, int index){
        if(harmonics[index] != null){
            harmonics[index].setAccidental(accidental);
            harmonicSeriesView.invalidate();
        }

    }

    /** Determines if a harmonic exists at a given harmonic number
     *
     * @param harmonicIndex index of harmonic being checked for
     * @return true if the harmonic is not null
     */
    public boolean harmonicExists(int harmonicIndex){
        if(harmonics[harmonicIndex] != null){
            return true;
        }

        return false;
    }


    /** Sets up two directional association between cursor and harmonicSeries
     *
     * @param cursor the cursor object instantiated by HarmonicSeriesActivity
     */
    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
        cursor.setHarmonicSeries(this);
    }

    /** Compares user-inputted harmonic series against answer harmonic series
     *
     * @return string containing all incorrect harmonics; empty if the series is correct
     */
    public String checkSeries(){
        String incorrectHarmonics = "";

        for(int i = 0; i < 16; i++){
            if(!harmonics[i].equals(answerHarmonics[i])){
                incorrectHarmonics += Integer.toString(i + 1) +", ";
            }
        }

        if(incorrectHarmonics.length() == 0){
            return incorrectHarmonics;
        }else{
            return incorrectHarmonics.substring(0,incorrectHarmonics.length() - 2);
        }

    }

    /** use HarmonicSeriesFactory to create the answer harmonic series
     * Set the fundamental in the user series
     * Called when the view is created
     *
     * @param octave octave for fundamental harmonic
     * @param pc pitch class for fundamental harmonic
     * @param accidental accidental found in key signature of series
     */
    public void setUpCorrectHarmonicSeries(int octave, int pc, Harmonic.Accidentals accidental) {
        // Reset the harmonics array, reset buttons
        harmonics = new Harmonic[16];
        harmonicSeriesView.setButtonsEnabled(false);

        // Factory for making harmonic series
        HarmonicSeriesFactory factory = new HarmonicSeriesFactory(appContext);

        // Harmonic Series Settings
        factory.setKeySignatureType(accidental);
        factory.setPitchClass(pc);
        factory.setOctave(octave);

        // Generate harmonic series
        answerHarmonics = factory.buildHarmonicSeries();

        // Set up fundamental harmonic
        harmonics[0] = answerHarmonics[0];
    }

    /** Deep copies answerHarmonics array into harmonics array
     * Deep copying allows the user to manipulate the answer when displayed
     * WITHOUT breaking the correct series
     *
     */
    public void displayCorrectHarmonicSeries(){
        for(int i = 0; i < 16; i ++){
            if(answerHarmonics[i] != null){
                harmonics[i] = answerHarmonics[i].deepCopy();
            }
        }
    }

}
