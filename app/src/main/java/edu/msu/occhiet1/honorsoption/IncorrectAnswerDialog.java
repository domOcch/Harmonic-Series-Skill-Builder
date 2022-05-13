package edu.msu.occhiet1.honorsoption;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

public class IncorrectAnswerDialog extends DialogFragment {
    AlertDialog dlg;
    /**
     * Create the dialog box
     * @param savedInstanceState The saved instance bundle
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Set the title
        builder.setTitle(R.string.incorrect_title);
        builder.setMessage(getString(R.string.incorrect_dialogue_message) + getArguments().getString("INCORRECT", "failed to find text"));


        builder.setNegativeButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });


        builder.setPositiveButton(R.string.show_answer,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Will display the answer harmonic series
                        getHarmonicSeriesView().displayCorrectHarmonicSeries();
                        getHarmonicSeriesView().invalidate();
                        dismiss();
                    }
                });



        dlg = builder.create();

        return dlg;
    }

    private HarmonicSeriesView getHarmonicSeriesView(){ return (HarmonicSeriesView) getActivity().findViewById(R.id.harmonicSeriesView);}


}
