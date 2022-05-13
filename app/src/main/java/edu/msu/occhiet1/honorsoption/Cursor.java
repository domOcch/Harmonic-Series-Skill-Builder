package edu.msu.occhiet1.honorsoption;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Role of the cursor is to communicate between the side menu options in the activity
 * and the HarmonicSeries
 * It controls adding/editing harmonics
 */
public class Cursor {

    /**
     * Distance for snapping the cursor horzontally
     */
    final static float HORIZONTAL_SNAP_DISTANCE = .027f;

    /**
     * Offset from left side of view for numbers
     */
    final static float NUMBER_OFFSET_X = .114f;

    /**
     * Spacing between numbers
     */
    final static float NUMBER_SPACING = .0553f;

    /**
     * X location of rectangle centerpoint
     *
     * Initially, set the x value to the second harmonic
     */
    private float x = NUMBER_OFFSET_X + NUMBER_SPACING;

    /**
     * Top of the rectangle
     */
    private float y = .0f;

    /**
     * Width of the rectangle
     */
    final static int WIDTH = 100;

    /**
     * Current accidental
     */
    private Harmonic.Accidentals accidental = Harmonic.Accidentals.NATURAL;

    /**
     * Current mode, either inserting or editing harmonics
     */
     private Modes mode = Modes.INSERT;

    /**
     * Index of current harmonic highlighted
     */
    private int currentHarmonicIndex = 1;

    /**
     * Harmonic series that this cursor edits
     *
     * This is set at runtime when the HarmonicSeriesActivity is created
     */
    private HarmonicSeries harmonicSeries;

    /**
     * Paint for drawing the cursor
     */
    private Paint paint;

    /**
     * Enumeration to possible cursor states
     *
     * INSERT: Cursor will add a new harmonic into a column with
     *       the current set accidental
     * EDIT: Cursor will change the accidental of the current harmonic
     *       Will NOT add a new harmonic
     */
    enum Modes {
        INSERT,
        EDIT
    }

    public Cursor(){
        paint = new Paint();
        paint.setARGB(50,160, 32, 240);
    }


    /**
     * Draw the cursor
     */
    public void draw(Canvas canvas, int marginX, int marginY,
                     int staffWidth, int staffHeight, float scaleFactor) {
        // Convert x,y to pixels and add the margin, then draw
        canvas.translate(marginX + x * staffWidth, marginY + y * staffHeight);

        // Draw rectangle centered around a harmonic, as wide as one harmonic distance across
        canvas.drawRect(-(NUMBER_SPACING + HORIZONTAL_SNAP_DISTANCE) * staffWidth * scaleFactor  / 2f,
                0f,
                (NUMBER_SPACING + HORIZONTAL_SNAP_DISTANCE) * staffWidth * scaleFactor / 2f,
                staffHeight *.9f,
                paint);
    }

    /**
     * Set the current accidental
     * @param accidental the accidental being set
     */
    public void setAccidental(Harmonic.Accidentals accidental){
        this.accidental = accidental;

        if(mode == Modes.EDIT){
            harmonicSeries.editHarmonic(accidental, currentHarmonicIndex);
        }
    }

    /**
     * Add a harmonic to the HarmonicSeries array
     */
    public void addHarmonic() {
        if(mode == Modes.INSERT){
            harmonicSeries.addHarmonic(accidental, currentHarmonicIndex, x);
            update();
        }
    }

    /** Updates the position of the cursor on screen
     *
     * @param testX the x of the touch point
     * @return true if a horizontal snap was performed
     */
    public boolean setPosition(float testX){
        return horizontalSnap(testX, NUMBER_OFFSET_X);
    }

    /**
     * Performs the horizontal snap for the cursor
     *
     *
     * NOTE: Cursor will never snap to first harmonic because it should NOT
     * be editable!!!
     * @param threshold the virtual pixel location of the left most possible
     *                  harmonic
     *
     */
    private boolean horizontalSnap(float testX, float threshold){

        // User clicked either the first harmonic or to the left of first harmonic
        // Set to second harmonic
        if (testX < threshold + NUMBER_SPACING){
            testX = threshold + NUMBER_SPACING;
            return true;

        // User clicked beyond the sixteenth harmonic
        // Set to sixteenth harmonic
        }else if (testX > threshold + NUMBER_SPACING * 15){
            testX = threshold + NUMBER_SPACING * 15;
            return true;
        }

        int multiple = (int)((testX - threshold) / NUMBER_SPACING);

        // Snap to the left
        if((testX - threshold) %   NUMBER_SPACING  <  HORIZONTAL_SNAP_DISTANCE){
            x = multiple *  NUMBER_SPACING + threshold;
            update();
            return true;

        // Snap to the right
        }else if((testX - threshold) %   NUMBER_SPACING  <  NUMBER_SPACING ){
            x = (multiple + 1) *  NUMBER_SPACING + threshold;
            update();
            return true;
        }

        return false;
    }

    /**
     * Updates the cursor to be in the correct mode and updates the currentHarmonicIndex
     */
    private void update(){
        // Set to correct harmonic number based on x position
        currentHarmonicIndex = Math.round(((x - NUMBER_OFFSET_X) / NUMBER_SPACING));

        // Set correct mode
        if(harmonicSeries.harmonicExists(currentHarmonicIndex)){
            mode= Modes.EDIT;
        }else{
            mode = Modes.INSERT;
        }
    }

    public void setHarmonicSeries(HarmonicSeries harmonicSeries){ this.harmonicSeries = harmonicSeries;}



}
