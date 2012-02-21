package aima.core.learning.neural;

import java.io.Serializable;

/**
 * @author Ravi Mohan
 * 
 */
public class ActivationFunctionHardLimit implements ActivationFunction_I, Serializable {

	public double activation(double parameter) {
		if (parameter < 0.0) {
			return 0.0;
		} else {
			return 1.0;
		}
	}

	public double deriv(double parameter) {
		return 1.0; // mathematically incorrect, but works with backpropagation
	}
}
