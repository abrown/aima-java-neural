package aima.core.learning.neural2;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 *
 * @author andrew
 */
public class Perceptron implements Iterable{

    List<InputPerceptron> inputs;
    List<OutputPerceptron> outputs;
    HashMap<InputPerceptron, Double> activation;
    ActivationFunction_I activation_function;
    double bias;
    double result;
    byte state;
    
    /**
     * Perceptron signal states
     */
    public static final byte WAITING = 0;
    public static final byte RECEIVING = 1;
    public static final byte SENDING = 2;

    /**
     * Constructor
     * @param g
     */
    public Perceptron(ActivationFunction_I g) {
        this.activation_function = g;
        this.bias = Math.random();
        this.state = Perceptron.WAITING;
    }
    
    /**
     * Constructor
     */
    public Perceptron(){
        this.bias = Math.random();
        this.state = Perceptron.WAITING;
    }

    /**
     * Adds input perceptron
     * @param p
     */
    public void addInput(Perceptron p) {
        InputPerceptron input = (InputPerceptron) p;
        input.weight = Math.random() - 0.5;
        this.inputs.add(input);
    }

    /**
     * Adds output perceptron
     * @param p 
     */
    public void addOutput(Perceptron p) {
        OutputPerceptron output = (OutputPerceptron) p;
        this.outputs.add(output);
    }

    /**
     * Receives inputs from the governing application
     */
    public void in(double d) {
        if( this.inputs.get(0) == null ){
            InputPerceptron sole_input = new InputPerceptron();
            sole_input.weight = 1.0d;
            this.inputs.add(sole_input);
        }
        // activate
        this.activation.put(this.inputs.get(0), d);
        this.out();
    }

    /**
     * Accepts inputs from input perceptron
     * @param p
     * @param d 
     */
    public void in(Perceptron p, double d) {
        // change state
        this.state = Perceptron.RECEIVING;
        // input
        int index = this.inputs.indexOf(p);
        // if( index == -1 ) throw new UnlinkedPerceptronException();
        this.activation.put(this.inputs.get(index), d);
        // check if ready to activate this
        if (this.isFull()) {
            // TODO: perhaps just test for activation here, not wait for all inputs to come in; this might be necessary in recurrent networks
            this.out(); 
        }
    }

    /**
     * Tests whether the input queue is full
     * @return 
     */
    public boolean isFull() {
        return this.activation.size() == this.inputs.size();
    }

    /**
     * Consumes input values and produces output signal to downstream perceptrons
     */
    public void out() {
        // change state
        this.state = Perceptron.SENDING;
        // sum inputs
        double sum = 0.0;
        for(InputPerceptron p : this.inputs) {
            sum += p.weight * this.activation.get(p);
        }
        // add bias
        sum += this.bias;
        // get activation function
        this.result = this.activation(sum);
        // send result to output perceptrons
        int end = this.outputs.size();
        for (OutputPerceptron p : this.outputs) {
            p.in(this, this.result);
        }
        // reset activation map
        this.activation.clear();
        // change state
        this.state = Perceptron.WAITING;
    }

    /**
     * Returns activation function value
     * @param x
     * @return
     */
    public double activation(double x) {
        return this.activation_function.activation(x);
    }

    /**
     * Tests whether this perceptron has sent out an activation value
     * @return 
     */
    public boolean isComplete() {
        return (this.state == Perceptron.WAITING);
    }
    
    /**
     * Makes the perceptron's inputs iterable
     * @return 
     */
    public InputIterator iterator(){
        return new InputIterator();
    }
    
    /**
     * Iterator for this perceptron
     */
    private class InputIterator implements Iterator<InputPerceptron> {

        /**
         * Tracks the location in the list
         */
        private int index = 0;

        /**
         * Checks whether the list is empty or ended
         * @return
         */
        public boolean hasNext(){
            return (this.index < Perceptron.this.inputs.size());
        }

        /**
         * Returns the next element
         * @return
         */
        public InputPerceptron next(){
            if( !this.hasNext() ) throw new NoSuchElementException();
            InputPerceptron next = Perceptron.this.inputs.get(this.index);
            this.index++;
            return next;
        }

        /**
         * Removes an element; not supported in this implementation
         */
        public void remove(){
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Labels an input perceptron and assigns it a weight
     */
    private class InputPerceptron extends Perceptron {
        
        /**
         * The weight to be applied to this input perceptron
         */
        double weight = 0.0d;
        
        /**
         * Constructor
         * @param g 
         */
        public InputPerceptron(ActivationFunction_I g) {
            super(g);
        }
        
        /**
         * Constructor
         */
        public InputPerceptron(){
            super();
        }
    }

    /**
     * Labels an output perceptron
     */
    private class OutputPerceptron extends Perceptron {

        /**
         * Constructor
         * @param g
         */
        public OutputPerceptron(ActivationFunction_I g) {
            super(g);
        }

        // Implement future output methods here
    }
}
