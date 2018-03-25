package com.dpingin.home.automation.audio.impl.window.function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: DanoSaur
 * Date: 17.07.14
 * Time: 02:11
 * To change this template use File | Settings | File Templates.
 */
public class WindowFunction
{
	private final Logger log = LoggerFactory.getLogger(WindowFunction.class);

	public static final int RECTANGULAR = 0;

	public static final int BARTLETT = 1;

	public static final int HANNING = 2;

	public static final int HAMMING = 3;

	public static final int BLACKMAN = 4;

	int windowType = 0; // defaults to rectangular window

	public WindowFunction()
	{
	}

	public void setWindowType(int wt)
	{
		windowType = wt;
	}

	public WindowFunction windowType(final int windowType)
	{
		this.windowType = windowType;
		return this;
	}

	public int getWindowType()
	{
		return windowType;
	}

	public void setWindowType(String w)
	{
		if (w.toUpperCase().equals("RECTANGULAR"))
			windowType = RECTANGULAR;
		if (w.toUpperCase().equals("BARTLETT"))
			windowType = BARTLETT;
		if (w.toUpperCase().equals("HANNING"))
			windowType = HANNING;
		if (w.toUpperCase().equals("HAMMING"))
			windowType = HAMMING;
		if (w.toUpperCase().equals("BLACKMAN"))
			windowType = BLACKMAN;
	}

	public double[] generate(int nSamples)
	{
		// generate nSamples window function values
		// for index values 0 .. nSamples - 1
		int m = nSamples / 2;
		double r;
		double pi = Math.PI;
		double[] w = new double[nSamples];
		switch (windowType)
		{
			case BARTLETT: // Bartlett (triangular) window
				for (int n = 0; n < nSamples; n++)
					w[n] = 1.0f - Math.abs(n - m) / m;
				break;
			case HANNING: // Hanning window
				r = pi / (m + 1);
				for (int n = -m; n < m; n++)
					w[m + n] = 0.5f + 0.5f * Math.cos(n * r);
				break;
			case HAMMING: // Hamming window
				r = pi / m;
				for (int n = -m; n < m; n++)
					w[m + n] = 0.54f + 0.46f * Math.cos(n * r);
				break;
			case BLACKMAN: // Blackman window
				r = pi / m;
				for (int n = -m; n < m; n++)
					w[m + n] = 0.42f + 0.5f * Math.cos(n * r) + 0.08f * Math.cos(2 * n * r);
				break;
			default: // Rectangular window function
				for (int n = 0; n < nSamples; n++)
					w[n] = 1.0f;
		}

		for (int i = 0; i < 5; i++)
		{
			setWindowType(i);
			double[] testWindow = generate(nSamples);
			log.trace("Generated window[{}] {}", i, testWindow);
		}

		return w;
	}
}