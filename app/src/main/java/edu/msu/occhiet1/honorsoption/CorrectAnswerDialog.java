package edu.msu.occhiet1.honorsoption;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class CorrectAnswerDialog extends DialogFragment {

    /**
     * Create the dialog box
     * @param savedInstanceState The saved instance bundle
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Set the title
        builder.setTitle(R.string.correct_title);

        builder.setNegativeButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });

        builder.setPositiveButton(R.string.new_series,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Generate new harmonic series and redraw screen
                        getHarmonicSeriesActivity().setUpCorrectHarmonicSeries();
                        getHarmonicSeriesActivity().findViewById(R.id.harmonicSeriesView).invalidate();
                        dismiss();
                    }
                });

        AlertDialog dlg = builder.create();

        return dlg;
    }

    private HarmonicSeriesActivity getHarmonicSeriesActivity(){ return (HarmonicSeriesActivity) getActivity();}

}
