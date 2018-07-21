package com.respireplus.respire.Math;

/**
 * Created by peekay on 23/2/18.
 */
/**
 * Abstract form of the a filter which can have different state variables
 *
 * Direct form I or II is derived from it
 */
public abstract class DirectFormAbstract {

    public DirectFormAbstract () {
        reset();
    }

    public abstract void reset();

    public abstract double process1 (double in, Biquad s);

    public static final int DIRECT_FORM_I = 0;
    public static final int DIRECT_FORM_II = 1;

};