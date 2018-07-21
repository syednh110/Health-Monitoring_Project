package com.respireplus.respire.Math;

/**
 * Created by peekay on 23/2/18.
 */
/**
 *
 * Implementation of a Direct Form II filter with its states. The coefficients
 * are supplied from the outside.
 *
 */
public class DirectFormII extends DirectFormAbstract{

    public DirectFormII() {
        reset();
    }

    public void reset() {
        m_v1 = 0;
        m_v2 = 0;
    }

    public double process1(double in,
                           Biquad s) {
        if (s != null) {
            double w = in - s.m_a1 * m_v1 - s.m_a2 * m_v2;
            double out = s.m_b0 * w + s.m_b1 * m_v1 + s.m_b2 * m_v2;

            m_v2 = m_v1;
            m_v1 = w;

            return out;
        } else {
            return in;
        }
    }

    double m_v1; // v[-1]
    double m_v2; // v[-2]

}
