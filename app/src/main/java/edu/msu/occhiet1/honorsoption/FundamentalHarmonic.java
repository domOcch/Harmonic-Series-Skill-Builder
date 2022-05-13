package edu.msu.occhiet1.honorsoption;

import android.content.Context;

/**
 * A harmonic which can neither be moved nor edited
 * The first harmonic is always the fundamental, and the ONLY fundamental
 */
public class FundamentalHarmonic extends Harmonic {
    public FundamentalHarmonic(Context context, Accidentals setAccidental, float x, String letterName, int octaveDisplacement){
        super(context,setAccidental, x, letterName, octaveDisplacement);
    }

    /** Fundamental harmonics cannot be moved and thus should not be hit
     *
     * @param testX
     * @param testY
     * @param staffWidth
     * @param staffHeight
     * @param scaleFactor
     * @return false because fundamental can't be moved
     */
    @Override
    public boolean hit(float testX, float testY,
                    int staffWidth, int staffHeight, float scaleFactor){
        return false;
    }

    /** Cannot be moved so this function simply returns
     *
     * @param dy y amount to move
     */
    @Override
    public void move(float dy) {
        return;
    }
}
