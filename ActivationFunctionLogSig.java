package aima.core.learning.neural2;

/**
 * @author Ravi Mohan
 * 
 */
public class ActivationFunctionLogSig implements ActivationFunction_I {

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
