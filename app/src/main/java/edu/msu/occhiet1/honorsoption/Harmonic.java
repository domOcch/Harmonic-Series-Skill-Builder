package edu.msu.occhiet1.honorsoption;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

public class Harmonic {

    /**
     * Enumeration to represent three possible accidentals
     */
    enum Accidentals {
        NATURAL,
        SHARP,
        FLAT
    }

    /**
     * Scale the harmonic down since it's too big by default.
     */
    final static float NOTE_SCALE = 0.24f;

    final static float VERTICAL_SPACING = 0.019f;

    final static float VERTICAL_SNAP_DISTANCE = 0.009f;

    /**
     * B3
     */
    final static float MAX_BASS_CLEF_Y = .481f;

    /**
     * C4
     */
    final static float MIN_TREBLE_CLEF_Y = .286f;

    /**
     * E2
     */
    final static float MIN_BASS_CLEF_Y = .69f;

    /**
     * A5
     */
    final static float MAX_TREBLE_CLEF_Y = .056f;

    private float c0 = .994f;



    // + .0554f * 9
    private float x = .17f ;

    private float y = .4f;

    private String letterName;
    private int letterOctave;

    /**
     * Determines how much a note is to be displaced by (8va, 8vb, etc)
     * -2 = 15va
     * -1 = 8va
     * 0 = loco
     * 1 = 8vb
     * 2 = 15vb
     */
    private int octaveDisplacement = 0;

    /**
     * note bitmap
     */
    private Bitmap noteBitmap;

    /**
     * bitmap for accidental
     */
    private Bitmap accidentalBitmap;

    /**
     * Brush for drawing ledger lines
     */
    private Paint ledgerBrush;

    private Context context;

    /**
     * Accidental for the note
     */
    private Accidentals accidental;

    /**
     * Constructor for initializing a harmonic starting with an x coordinate
     * @param context
     * @param accidental
     * @param x
     */
    public Harmonic(Context context, Accidentals accidental, float x, int octaveDisplacement){
        init(context, accidental, x);
        this.octaveDisplacement = octaveDisplacement;

        convertYPositionToNameAndOctave();
    }

    public Harmonic(Context context, Accidentals accidental, float x, String letterName, int letterOctave){
        init(context, accidental, x);
        this.letterName = letterName;
        this.letterOctave = letterOctave;

        convertNameToYPosition();
    }

    private void init(Context context, Accidentals accidental, float x){
        this.context = context;
        this.accidental = accidental;
        this.x = x;

        // Load the whole note image
        noteBitmap =
                BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.whole_note_edited);

        // Load the accidental
        updateAccidentalBitmap();

        // instantiate brush for painting leddger lines
        ledgerBrush = new Paint(Paint.ANTI_ALIAS_FLAG);
        ledgerBrush.setStrokeWidth(20);
    }

    public int getOctaveDisplacement(){ return octaveDisplacement; }

    public float getX() {return x;}

    /**
     * Set Y positions for a harmonic given its name and octave
     *
     * This is called in the factory class, when harmonics are instantiated from note name
     * instead of position
     */
    private void convertNameToYPosition(){

        int distanceFromCtoNote = 0;

        // Distance between this note name and an arbitrary C
        switch(letterName){
            case "C":{
                distanceFromCtoNote = 0;
                break;
            }
            case "D":{
                distanceFromCtoNote = 1;
                break;
            }
            case "E":{
                distanceFromCtoNote = 2;
                break;
            }
            case "F":{
                distanceFromCtoNote = 3;
                break;
            }
            case "G":{
                distanceFromCtoNote = 4;
                break;
            }
            case "A":{
                distanceFromCtoNote = 5;
                break;
            }
            case "B":{
                distanceFromCtoNote = 6;
                break;
            }

        }

        // Calculate y position based on note's distance from arbitrary C and its octave
        y = c0 - VERTICAL_SPACING * distanceFromCtoNote - letterOctave * 7 * VERTICAL_SPACING;


        // MAKE INTO ITS OWN FUNCTION
        // Adjust octave below displacement
        if(y > MIN_BASS_CLEF_Y){
            while(y > MIN_BASS_CLEF_Y){
                y -= VERTICAL_SPACING * 7;
                octaveDisplacement--;
            }
        }

        //.481 is B3, .286 is C4
        // Adjust for gap between treble and bass clef if note is high enough
        if (y < MAX_BASS_CLEF_Y){
            y -= MAX_BASS_CLEF_Y - MIN_TREBLE_CLEF_Y - VERTICAL_SPACING;

            // Adjust octave above displacement
            if(y < MAX_TREBLE_CLEF_Y){
                while(y < MAX_TREBLE_CLEF_Y){
                    y += VERTICAL_SPACING * 7;
                    octaveDisplacement++;
                }
            }
        }

    }

    /**
     * Given a y position, converts to a note name and octave
     */
    private void convertYPositionToNameAndOctave(){
        String name = "";

        // Number used to translate distance to a letter name
        int noteNumber = 0;

        // note is C4 or above
        // MIN_TREBLE_CLEF_Y - MAX_BASS_CLEF_Y  is the distance of the "no man's land" in the middle
        if (y < MAX_BASS_CLEF_Y){
            //  this.letterOctave = (int)(((c0 - y - octaveDisplacement * 7 * VERTICAL_SPACING + VERTICAL_SPACING -(MAX_BASS_CLEF_Y - MIN_TREBLE_CLEF_Y) - VERTICAL_SNAP_DISTANCE) / VERTICAL_SPACING) / 7);
            this.letterOctave = (int)(((c0 - y + VERTICAL_SPACING + octaveDisplacement * 7 * VERTICAL_SPACING + (MIN_TREBLE_CLEF_Y - MAX_BASS_CLEF_Y) ) / VERTICAL_SPACING) / 7);
            noteNumber = (int)(((c0 - y + 2 * VERTICAL_SPACING - (MIN_TREBLE_CLEF_Y - MAX_BASS_CLEF_Y)) / VERTICAL_SPACING) % 7);
        // note is b3 or lower
        }else{
            this.letterOctave = (int)(((c0 - y + octaveDisplacement * 7 * VERTICAL_SPACING) / VERTICAL_SPACING) / 7);
            noteNumber = (int)(((c0 - y) / VERTICAL_SPACING) % 7);
        }


                // Distance between this note name and an arbitrary C
        switch(noteNumber){
            case 0:{
                name = "C";
                break;
            }
            case 1:{
                name = "D";
                break;
            }
            case 2:{
                name = "E";
                break;
            }
            case 3:{
                name = "F";
                break;
            }
            case 4:{
                name = "G";
                break;
            }
            case 5:{
                name = "A";
                break;
            }
            case 6:{
                name = "B";
                break;
            }
        }
        this.letterName = name;

    }


    /**
     * Draw the harmonic
     */
    public void draw(Canvas canvas, int marginX, int marginY,
                     int staffWidth, int staffHeight, float scaleFactor) {
        canvas.save();

        // Convert x,y to pixels and add the margin, then draw
        canvas.translate(marginX + x * staffWidth, marginY + y * staffHeight);

        // Scale it to the right size
        canvas.scale(scaleFactor * NOTE_SCALE, scaleFactor * NOTE_SCALE);

        // This magic code makes the center of the harmonic at 0, 0
        canvas.translate(-noteBitmap.getWidth() / 2f, -noteBitmap.getHeight() / 2f);

        // Draw the bitmap
        canvas.drawBitmap(noteBitmap, 0, 0, null);

        // If a ledger line is necessary, draw it

        if(y <.075 - VERTICAL_SNAP_DISTANCE && y > .018 ||                                      // greater than G5 less than C6
                y <= .286 + VERTICAL_SNAP_DISTANCE && y >.267 + VERTICAL_SNAP_DISTANCE ||       // C4
                y <= MIN_BASS_CLEF_Y + VERTICAL_SNAP_DISTANCE && y > .673)                                  // E2 .69
        {
            canvas.drawLine(-50, noteBitmap.getHeight() / 2f, noteBitmap.getWidth() + 50f, noteBitmap.getHeight() / 2f, ledgerBrush);

        }

        // Adjust scaling for the accidental
        canvas.scale(scaleFactor, scaleFactor);

        // Draw the accidental
        switch (accidental)
        {
            case NATURAL:
                break;
            case SHARP:
                canvas.drawBitmap(accidentalBitmap, -150, -110, null);
                break;
            case FLAT:
                canvas.drawBitmap(accidentalBitmap, -150, -180, null);
                break;
        }

        canvas.restore();
    }

    /**
     * Test to see if we have touched a harmonic
     */
    public boolean hit(float testX, float testY,
                       int staffWidth, int staffHeight, float scaleFactor) {

        // Make relative to the location and size to the piece size
        int pX = (int)((testX - x) * staffWidth / scaleFactor / NOTE_SCALE) +
                noteBitmap.getWidth() / 2;
        int pY = (int)((testY - y) * staffHeight / scaleFactor/ NOTE_SCALE) +
                noteBitmap.getHeight() / 2;

        if(pX < -60 || pX >= noteBitmap.getWidth() + 60 ||
                pY < -60 || pY >= noteBitmap.getHeight() + 60) {
            return false;
        }

        return true;
    }

    /**
     * Move the harmonic by dx, dy
     * NOTE: X value does NOT change so partials stay in their columns
     * @param dy y amount to move
     */
    public void move(float dy) {
        // .056 is A5, .69 is E2

        // User attempted to drag above A5
        if(y + dy < MAX_TREBLE_CLEF_Y  ){
            y = MAX_TREBLE_CLEF_Y;

        // User attempted to drag below E2
        }else if (y + dy > MIN_BASS_CLEF_Y){
            y = MIN_BASS_CLEF_Y;

        // Dragging within valid bounds
        }else{
            y += dy;
        }

    }

    /**
     * If we are within SNAP_DISTANCE of the correct
     * answer, snap to the correct answer exactly.
     * @return
     */
    public boolean maybeSnap() {

        // User is in "no man's land" between staves
        // Either snap to B3 in bass clef OR C4 in treble
        // No in between placement is allowed currently
        if (y > MIN_TREBLE_CLEF_Y && y < MAX_BASS_CLEF_Y){
            if ((y - MIN_TREBLE_CLEF_Y) / (MAX_BASS_CLEF_Y - MIN_TREBLE_CLEF_Y) < .5f) {
                y = MIN_TREBLE_CLEF_Y;
            } else {
                y = MAX_BASS_CLEF_Y;
            }
        }

        // .303 is "B3" aka treble B3
        // .096 is F5
        // .481 is B3
        if(y < MIN_TREBLE_CLEF_Y + VERTICAL_SNAP_DISTANCE) {
            verticalSnap(.096f);
        }
        else if (y > MAX_BASS_CLEF_Y - VERTICAL_SNAP_DISTANCE){
            verticalSnap(MAX_BASS_CLEF_Y);
        }

        convertYPositionToNameAndOctave();

        return true;
    }

    /**
     * Performs the vertical snap for a harmonic
     * @param threshold the virtual pixel location of the highest possible
     *                  note on the staff
    *
     */
    private void verticalSnap(float threshold) {

        // Normal snapping range
        //
        int multiple = (int) ((y - threshold) / VERTICAL_SPACING);
        if ((y - threshold) % VERTICAL_SPACING < VERTICAL_SNAP_DISTANCE) {
            y = multiple * VERTICAL_SPACING + threshold;
        } else if ((multiple * VERTICAL_SPACING - y) % VERTICAL_SPACING < VERTICAL_SNAP_DISTANCE) {
            y = multiple * VERTICAL_SPACING + threshold;
        }

    }

    /** Set the accidental for this harmonic
     *
     * @param accidental new accidental for the note
     */
    public void setAccidental(Accidentals accidental){
        this.accidental = accidental;
        updateAccidentalBitmap();
    }

    /**
     * Set the proper bitmap based on the current accidental
     */
    private void updateAccidentalBitmap(){
        switch (accidental)
        {
            case NATURAL:
                accidentalBitmap = null;
                break;
            case SHARP:
                accidentalBitmap =
                        BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.sharp_edited);
                break;
            case FLAT:
                accidentalBitmap =
                        BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.flat_edited);
                break;
        }
    }

    /**
     * Check to see if two harmonics are equivalent
     * Equivalent harmonics have the same noteName and accidental
     * @param other other harmonic
     * @return true if harmonics are equivalent
     */
    public boolean equals(Harmonic other){
        if(this.accidental == other.accidental && this.letterName.equals(other.letterName) && this.letterOctave == other.letterOctave)
            return true;

        return false;
    }

    /** Performs a deep copy of this harmonic
     *
     * @return a new harmonic with the same state
     */
    public Harmonic deepCopy(){
        return new Harmonic(context, accidental, x, letterName, letterOctave);
    }




}
