package com.respireplus.respire.Math;

/**
 * Created by peekay on 23/2/18.
 */
import org.apache.commons.math3.complex.Complex;

/**
 * Transforms from an analogue lowpass filter to a digital lowpass filter
 */

public class LowPassTransform {
    private double f;

    private Complex transform(Complex c) {
        if (c.isInfinite())
            return new Complex(-1, 0);

        // frequency transform
        c = c.multiply(f);

        Complex one = new Complex(1, 0);

        // bilinear low pass transform
        return (one.add(c)).divide(one.subtract(c));
    }

    public LowPassTransform(double fc, LayoutBase digital, LayoutBase analog) {
        digital.reset();

        // prewarp
        f = Math.tan(Math.PI * fc);

        int numPoles = analog.getNumPoles();
        int pairs = numPoles / 2;
        for (int i = 0; i < pairs; ++i) {
            PoleZeroPair pair = analog.getPair(i);
            digital.addPoleZeroConjugatePairs(transform(pair.poles.first),
                    transform(pair.zeros.first));
        }

        if ((numPoles & 1) == 1) {
            PoleZeroPair pair = analog.getPair(pairs);
            digital.add(transform(pair.poles.first),
                    transform(pair.zeros.first));
        }

        digital.setNormal(analog.getNormalW(), analog.getNormalGain());
    }

}
