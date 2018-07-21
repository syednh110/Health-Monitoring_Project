package com.respireplus.respire.Math;

/**
 * Created by peekay on 23/2/18.
 */
import org.apache.commons.math3.analysis.solvers.LaguerreSolver;
import org.apache.commons.math3.complex.Complex;

/**
 * User facing class which contains all the methods the user uses to create
 * Bessel filters. This done in this way: Bessel bessel = new Bessel(); Then
 * call one of the methods below to create low-,high-,band-, or stopband
 * filters. For example: bessel.bandPass(2,250,50,5);
 */

public class Bessel extends Cascade{
    // returns fact(n) = n!
    private double fact(int n) {
        if (n == 0)
            return 1;

        double y = n;
        for (double m = n-1; m > 0; m--)
            y = y * m;

        return y;
    }

    class AnalogLowPass extends LayoutBase {

        int degree;

        double[] m_a;
        Complex[] m_root;

        // returns the k-th zero based coefficient of the reverse bessel
        // polynomial of degree n
        private double reversebessel(int k, int n) {
            double result = fact(2 * n - k)
                    / ((fact(n - k) * fact(k)) * Math.pow(2., n - k));
            return result;
        }

        // ------------------------------------------------------------------------------

        public AnalogLowPass(int _degree) {
            super(_degree);
            degree = _degree;
            m_a   = new double[degree + 1]; // input coefficients (degree+1 elements)
            m_root = new Complex[degree]; // array of roots (degree elements)
            setNormal(0, 1);
        }

        public void design() {
            reset();

            for (int i = 0; i < degree + 1; ++i) {
                m_a[i] = reversebessel(i, degree);
            }

            LaguerreSolver laguerreSolver = new LaguerreSolver();

            m_root = laguerreSolver.solveAllComplex(m_a,0.0);

            Complex inf = Complex.INF;
            int pairs = degree / 2;
            for (int i = 0; i < pairs; ++i) {
                Complex c = m_root[i];
                addPoleZeroConjugatePairs(c, inf);
            }

            if ((degree & 1) == 1)
                add(new Complex(m_root[pairs].getReal()), inf);
        }

    }

    private void setupLowPass(int order, double sampleRate,
                              double cutoffFrequency, int directFormType) {

        AnalogLowPass m_analogProto = new AnalogLowPass(order);

        m_analogProto.design();

        LayoutBase m_digitalProto = new LayoutBase(order);

        new LowPassTransform(cutoffFrequency / sampleRate, m_digitalProto,
                m_analogProto);

        setLayout(m_digitalProto, directFormType);
    }

    /**
     * Bessel Lowpass filter with default topology
     *
     * @param order
     *            The order of the filter
     * @param sampleRate
     *            The sampling rate of the system
     * @param cutoffFrequency
     *            the cutoff frequency
     */
    public void lowPass(int order, double sampleRate, double cutoffFrequency) {
        setupLowPass(order, sampleRate, cutoffFrequency,
                DirectFormAbstract.DIRECT_FORM_II);
    }

    /**
     * Bessel Lowpass filter with custom topology
     *
     * @param order
     *            The order of the filter
     * @param sampleRate
     *            The sampling rate of the system
     * @param cutoffFrequency
     *            The cutoff frequency
     * @param directFormType
     *            The filter topology. This is either
     *            DirectFormAbstract.DIRECT_FORM_I or DIRECT_FORM_II
     */
    public void lowPass(int order, double sampleRate, double cutoffFrequency,
                        int directFormType) {
        setupLowPass(order, sampleRate, cutoffFrequency, directFormType);
    }

    private void setupHighPass(int order, double sampleRate,
                               double cutoffFrequency, int directFormType) {

        AnalogLowPass m_analogProto = new AnalogLowPass(order);
        m_analogProto.design();

        LayoutBase m_digitalProto = new LayoutBase(order);

        new HighPassTransform(cutoffFrequency / sampleRate, m_digitalProto,
                m_analogProto);

        setLayout(m_digitalProto, directFormType);
    }

    /**
     * Highpass filter with custom topology
     *
     * @param order
     *            Filter order (ideally only even orders)
     * @param sampleRate
     *            Sampling rate of the system
     * @param cutoffFrequency
     *            Cutoff of the system
     * @param directFormType
     *            The filter topology. See DirectFormAbstract.
     */
    public void highPass(int order, double sampleRate, double cutoffFrequency,
                         int directFormType) {
        setupHighPass(order, sampleRate, cutoffFrequency, directFormType);
    }

    /**
     * Highpass filter with default filter topology
     *
     * @param order
     *            Filter order (ideally only even orders)
     * @param sampleRate
     *            Sampling rate of the system
     * @param cutoffFrequency
     *            Cutoff of the system
     */
    public void highPass(int order, double sampleRate, double cutoffFrequency) {
        setupHighPass(order, sampleRate, cutoffFrequency,
                DirectFormAbstract.DIRECT_FORM_II);
    }

    private void setupBandStop(int order, double sampleRate,
                               double centerFrequency, double widthFrequency, int directFormType) {

        AnalogLowPass m_analogProto = new AnalogLowPass(order);
        m_analogProto.design();

        LayoutBase m_digitalProto = new LayoutBase(order * 2);

        new BandStopTransform(centerFrequency / sampleRate, widthFrequency
                / sampleRate, m_digitalProto, m_analogProto);

        setLayout(m_digitalProto, directFormType);
    }

    /**
     * Bandstop filter with default topology
     *
     * @param order
     *            Filter order (actual order is twice)
     * @param sampleRate
     *            Samping rate of the system
     * @param centerFrequency
     *            Center frequency
     * @param widthFrequency
     *            Width of the notch
     */
    public void bandStop(int order, double sampleRate, double centerFrequency,
                         double widthFrequency) {
        setupBandStop(order, sampleRate, centerFrequency, widthFrequency,
                DirectFormAbstract.DIRECT_FORM_II);
    }

    /**
     * Bandstop filter with custom topology
     *
     * @param order
     *            Filter order (actual order is twice)
     * @param sampleRate
     *            Samping rate of the system
     * @param centerFrequency
     *            Center frequency
     * @param widthFrequency
     *            Width of the notch
     * @param directFormType
     *            The filter topology
     */
    public void bandStop(int order, double sampleRate, double centerFrequency,
                         double widthFrequency, int directFormType) {
        setupBandStop(order, sampleRate, centerFrequency, widthFrequency,
                directFormType);
    }

    private void setupBandPass(int order, double sampleRate,
                               double centerFrequency, double widthFrequency, int directFormType) {

        AnalogLowPass m_analogProto = new AnalogLowPass(order);
        m_analogProto.design();

        LayoutBase m_digitalProto = new LayoutBase(order * 2);

        new BandPassTransform(centerFrequency / sampleRate, widthFrequency
                / sampleRate, m_digitalProto, m_analogProto);

        setLayout(m_digitalProto, directFormType);

    }

    /**
     * Bandpass filter with default topology
     *
     * @param order
     *            Filter order
     * @param sampleRate
     *            Sampling rate
     * @param centerFrequency
     *            Center frequency
     * @param widthFrequency
     *            Width of the notch
     */
    public void bandPass(int order, double sampleRate, double centerFrequency,
                         double widthFrequency) {
        setupBandPass(order, sampleRate, centerFrequency, widthFrequency,
                DirectFormAbstract.DIRECT_FORM_II);
    }

    /**
     * Bandpass filter with custom topology
     *
     * @param order
     *            Filter order
     * @param sampleRate
     *            Sampling rate
     * @param centerFrequency
     *            Center frequency
     * @param widthFrequency
     *            Width of the notch
     * @param directFormType
     *            The filter topology (see DirectFormAbstract)
     */
    public void bandPass(int order, double sampleRate, double centerFrequency,
                         double widthFrequency, int directFormType) {
        setupBandPass(order, sampleRate, centerFrequency, widthFrequency,
                directFormType);
    }

}

