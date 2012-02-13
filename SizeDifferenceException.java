package aima.core.learning.neural2;

/**
 *
 * @author andrew
 */
public class SizeDifferenceException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1276768413294674897L;

	/**
     * Creates a new instance of <code>SizeDifferenceException</code> without detail message.
     */
    public SizeDifferenceException() {
    }

    /**
     * Constructs an instance of <code>SizeDifferenceException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public SizeDifferenceException(String msg) {
        super(msg);
    }
}
