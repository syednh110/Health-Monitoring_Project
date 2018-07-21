package com.respireplus.respire.Math;

/**
 * Created by peekay on 23/2/18.
 */

/**
 *
 * Implementation of a Direct Form I filter with its states. The coefficients
 * are supplied from the outside.
 *
 */

public class DirectFormI extends DirectFormAbstract{

    public DirectFormI() {
        reset();
    }

    public void reset() {
        m_x1 = 0;
        m_x2 = 0;
        m_y1 = 0;
        m_y2 = 0;
    }

    public double process1(double in, Biquad s) {

        double out = s.m_b0 * in + s.m_b1 * m_x1 + s.m_b2 * m_x2
                - s.m_a1 * m_y1 - s.m_a2 * m_y2;
        m_x2 = m_x1;
        m_y2 = m_y1;
        m_x1 = in;
        m_y1 = out;

        return out;
    }

    double m_x2; // x[n-2]
    double m_y2; // y[n-2]
    double m_x1; // x[n-1]
    double m_y1;
    // y[n-1]

}
