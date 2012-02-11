package aima.core.learning.neural2;

/**
 * @author Ravi Mohan
 * 
 */
public interface ActivationFunction_I {
	double activation(double parameter);
	double deriv(double parameter);
}
