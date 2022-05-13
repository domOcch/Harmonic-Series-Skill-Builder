package edu.msu.occhiet1.honorsoption;

import static edu.msu.occhiet1.honorsoption.Harmonic.Accidentals.NATURAL;
import static edu.msu.occhiet1.honorsoption.Harmonic.Accidentals.SHARP;
import static edu.msu.occhiet1.honorsoption.Harmonic.Accidentals.FLAT;

import android.content.Context;
import android.util.Pair;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Builds a harmonic series in any of TWELVE keys
 * unsupported keys: Cb and C# (enharmonic equivalents are valid though)
 */
public class HarmonicSeriesFactory {
    /**
     * Order pitch class set representing a generic harmonic series
     */
    int[] basicHarmonicSeries = {0,0,7,0,4,7,10,0,2,4,6,7,9,10,11,0};

    /**
     * The accidental of the key we are in
     * I.E. any key with flats is FLAT, any with sharps is SHARP, no sharps/flat NATURAL
     */
    Harmonic.Accidentals accidental;

    /**
     * The octave designation for the pitch, based on middle C as C4
     */
    int octave;

    /**
     * Context, needed to construct harmonics
     */
    Context context;


    HarmonicSeriesFactory(Context context){
        this.context = context;
    }

    public void setKeySignatureType(Harmonic.Accidentals accidental){
        this.accidental = accidental;
    }

    public void setOctave(int octave){
        this.octave = octave;
    }

    /** PC for series
     * Transpose harmonicSeries set using this pitch class
     * @param pc pitch class we are transposing the series to
     */
    public void setPitchClass(int pc){
        for(int i = 0; i < 16; i++){
            basicHarmonicSeries[i] = (basicHarmonicSeries[i] + pc) % 12;
        }
    }

    /** Creates harmonics using the transposed basicHarmonicSeries by converting
     * pitch classes to their proper letter names
     *
     * @return harmonic series in correct key
     */
    public Harmonic[] buildHarmonicSeries(){
        Harmonic[] harmonicSeries = new Harmonic[16];

        // REMEMBER!!!! You know you've changed octaves if the next PC
        // is the same or less than the previous PC

        // Previous pc is necessary so you know if you've changed octaves
        int previousPC = 0;
        int currentOctave = octave;

        // so the harmonic draws at the appropriate harmonic number on screen
        float xPos = Cursor.NUMBER_OFFSET_X;

        for(int i = 0; i < 16; i ++){
            // Check if octave has changed
            if(i != 0 && previousPC >= basicHarmonicSeries[i]){
                currentOctave ++;
            }

            // Create harmonic
            Pair<String, Harmonic.Accidentals> harmonicInfo = convertPitchClassToPitch(basicHarmonicSeries[i], i);

            // Check for fundamental harmonic
            if(i != 0){
                harmonicSeries[i] = new Harmonic(context, harmonicInfo.second, xPos,harmonicInfo.first, currentOctave );
            }else{
                harmonicSeries[i] = new FundamentalHarmonic(context, harmonicInfo.second, xPos,harmonicInfo.first, currentOctave );
            }

            // Move position to the right
            xPos += Cursor.NUMBER_SPACING;
            previousPC =  basicHarmonicSeries[i];
        }

        return harmonicSeries;
    }

    /** Converts a given pitch class to its letter name and assigns it the appropriate accidental
     * Takes into account the harmonic number so enharmonic equivalences can be addressed properly
     *
     * @param pc integer pitch class representation of note
     * @param harmonicNumber current number we are in the harmonic series (remember, it goes 0-15 NOT 1-16)
     * @return pair containing the note name and its accidental
     */
    private Pair<String, Harmonic.Accidentals> convertPitchClassToPitch(int pc, int harmonicNumber){
        // convert PC to pitch using rule for sharp/flat/natural key
        // account for 7,11,14 harmonics

        switch(pc){
            case 0:
                // Account for 11th harmonic in F# major
                if ((harmonicNumber == 10) && accidental == SHARP){
                    return new Pair<String, Harmonic.Accidentals>("B", SHARP);
                }else{
                    return new Pair<String, Harmonic.Accidentals>("C", NATURAL);
                }
            case 1:
                if(accidental == SHARP){
                    return new Pair<String, Harmonic.Accidentals>("C", accidental);
                }else{
                    return new Pair<String, Harmonic.Accidentals>("D", accidental);
                }
            case 2:
                return new Pair<String, Harmonic.Accidentals>("D", NATURAL);
            case 3:
                if(accidental == SHARP){
                    return new Pair<String, Harmonic.Accidentals>("D", accidental);
                }else{
                    return new Pair<String, Harmonic.Accidentals>("E", accidental);
                }
            case 4:
                // Account for Fb in Gb major
                if((harmonicNumber == 6 || harmonicNumber == 13) && accidental == FLAT){
                    return new Pair<String, Harmonic.Accidentals>("F", FLAT);
                }else{
                    return new Pair<String, Harmonic.Accidentals>("E", NATURAL);
                }

            case 5:
                /// F# major 15th, B major 11th
                if ((harmonicNumber == 14) && accidental == SHARP || (harmonicNumber == 10) && accidental == SHARP){
                    return new Pair<String, Harmonic.Accidentals>("E", SHARP);
                }else{
                    return new Pair<String, Harmonic.Accidentals>("F", NATURAL);
                }
            case 6:
                // sharp key OR F# 11th harmonic in C major
                if(accidental == SHARP || accidental == NATURAL){
                    return new Pair<String, Harmonic.Accidentals>("F", SHARP);
                }else{
                    return new Pair<String, Harmonic.Accidentals>("G", accidental);
                }
            case 7:
                return new Pair<String, Harmonic.Accidentals>("G", NATURAL);
            case 8:
                if(accidental == SHARP){
                    return new Pair<String, Harmonic.Accidentals>("G", accidental);
                }else{
                    return new Pair<String, Harmonic.Accidentals>("A", accidental);
                }
            case 9:
                return new Pair<String, Harmonic.Accidentals>("A", NATURAL);
            case 10:
                if(accidental == SHARP){
                    return new Pair<String, Harmonic.Accidentals>("A", accidental);

                    // Account for Bb in C major
                }else if ((harmonicNumber == 6 || harmonicNumber == 13) && accidental == NATURAL){
                    return new Pair<String, Harmonic.Accidentals>("B", FLAT);
                }else{
                    return new Pair<String, Harmonic.Accidentals>("B", accidental);
                }
            case 11:
                // Check enharmonic equivalence for Cb in Db major
                if((harmonicNumber == 6 || harmonicNumber == 13) && accidental == FLAT){
                    return new Pair<String, Harmonic.Accidentals>("C", FLAT);
                }else{
                    return new Pair<String, Harmonic.Accidentals>("B", NATURAL);
                }


        }

        return null;
    }

}
