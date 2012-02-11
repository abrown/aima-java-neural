package aima.core.learning.neural2;

import aima.core.learning.neural.*;

/**
 * @author Ravi Mohan
 * 
 */
public class ActivationFunctionPureLinear implements ActivationFunction_I {

	public double activation(double parameter) {
		return parameter;
	}

	public double deriv(double parameter) {

		return 1;
	}
}
