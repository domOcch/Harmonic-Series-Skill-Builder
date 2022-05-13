package edu.msu.occhiet1.honorsoption;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;


/**
 * Custom view class for our Puzzle.
 */
public class HarmonicSeriesView extends View {

    /**
     * The actual harmonic series
     */
    private HarmonicSeries harmonicSeries;

    public HarmonicSeriesView(Context context) {
        super(context);
        init(null, 0);
    }

    public HarmonicSeriesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public HarmonicSeriesView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        harmonicSeries = new HarmonicSeries(getContext(), this);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return harmonicSeries.onTouchEvent(this, event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        harmonicSeries.draw(canvas);
    }

    public void setCursor(Cursor cursor) {
        harmonicSeries.setCursor(cursor);
    }

    /**
     * Enables playYours and checkSeries button once the user has
     * added 16 total harmonics
     */
    public void setButtonsEnabled(boolean enabled){
        HarmonicSeriesActivity harmonicSeriesActivity = (HarmonicSeriesActivity)getContext();
        Button buttonCheckSeries = (Button)harmonicSeriesActivity.findViewById(R.id.buttonCheckSeries);
        Button buttonPlayYours = (Button)harmonicSeriesActivity.findViewById(R.id.buttonPlayYours);

        buttonCheckSeries.setEnabled(enabled);
        buttonPlayYours.setEnabled(enabled);
    }

    public String checkSeries(){
        return harmonicSeries.checkSeries();
    }


    public void setUpCorrectHarmonicSeries(int octave, int pc, Harmonic.Accidentals accidental) {
        harmonicSeries.setUpCorrectHarmonicSeries(octave, pc, accidental);
    }

    public void displayCorrectHarmonicSeries(){
        harmonicSeries.displayCorrectHarmonicSeries();
    }

}