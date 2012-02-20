/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aima.core.learning.neural2;

/**
 *
 * @author andrew
 */
public class WrongSizeException extends Exception {

    /**
     * Creates a new instance of <code>WrongSizeException</code> without detail message.
     */
    public WrongSizeException() {
    }

    /**
     * Constructs an instance of <code>WrongSizeException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public WrongSizeException(String msg) {
        super(msg);
    }
}
