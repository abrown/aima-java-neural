package aima.core.learning.neural2;

import java.io.Serializable;

/**
 * @author Ravi Mohan
 * 
 */
public class ActivationFunctionPureLinear implements ActivationFunction_I, Serializable {

	public double activation(double parameter) {
		return parameter;
	}

	public double deriv(double parameter) {

		return 1;
	}
}
