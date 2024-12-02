package com.monsler.drumsynth.core;

public class TempoConverter {

    public static double calculateIncreaseCoefficient(int bpm, int desiredHits) {
        double hitsPer10ms = bpm / 6000.0;
        return desiredHits / hitsPer10ms;

    }


}