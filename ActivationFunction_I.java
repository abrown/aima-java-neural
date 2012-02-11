package aima.core.learning.neural2;
import aima.core.learning.neural.*;

/**
 * @author Ravi Mohan
 * 
 */
public interface ActivationFunction_I {
	double activation(double parameter);
	double deriv(double parameter);
}