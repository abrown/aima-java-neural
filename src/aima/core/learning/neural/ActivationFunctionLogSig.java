package aima.core.learning.neural;

import java.io.Serializable;

/**
 * @author Ravi Mohan
 * 
 */
public class ActivationFunctionLogSig implements ActivationFunction_I, Serializable {

	public double activation(double parameter) {

		return 1.0 / (1.0 + Math.pow(Math.E, (-1.0 * parameter)));
	}

	public double deriv(double parameter) {
		// parameter = induced field
		// e == activation
		double e = 1.0 / (1.0 + Math.pow(Math.E, (-1.0 * parameter)));
		return e * (1.0 - e);
	}
}
