package aima.core.learning.neural2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Vector;

/**
 *
 * @author andrew
 */
public class Perceptron implements Iterable{

    ArrayList<Perceptron> inputs;
    ArrayList<Perceptron> outputs;
    HashMap<Perceptron, Double> weights;
    HashMap<Perceptron, Double> activation;
    ActivationFunction_I function;
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
    	this.inputs = new ArrayList<Perceptron>();
    	this.outputs = new ArrayList<Perceptron>();
    	this.weights = new HashMap<Perceptron, Double>();
    	this.activation = new HashMap<Perceptron, Double>();
        this.function = g;
        this.bias = 0.0d;
        this.state = Perceptron.WAITING;
    }
    
    /**
     * Constructor
     */
    public Perceptron(){
    	this.inputs = new ArrayList<Perceptron>();
    	this.outputs = new ArrayList<Perceptron>();
    	this.weights = new HashMap<Perceptron, Double>();
    	this.activation = new HashMap<Perceptron, Double>();
        this.bias = 0.0d;
        this.state = Perceptron.WAITING;
    }

    /**
     * Adds input perceptron
     * @param p
     */
    public void addInput(Perceptron p) {
        double weight = Math.random() - 0.5;
        this.weights.put(p, weight);
        this.inputs.add(p);
    }
    
    public void addInput(Perceptron p, double weight){
    	this.weights.put(p, weight);
    	this.inputs.add(p);
    }

    /**
     * Adds output perceptron
     * @param p 
     */
    public void addOutput(Perceptron p) {
        this.outputs.add(p);
    }

    /**
     * Receives inputs from the governing application;
     * implies there are no input perceptrons to this one.
     */
    public void in(double d) {
    	// change state
    	this.state = Perceptron.RECEIVING;
    	// create stub perceptron if necessary
        if( this.inputs.size() == 0 ){
        	this.addInput(new Perceptron(), 1.0);
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
        for(Perceptron p : this.inputs) {
            sum += this.weights.get(p) * this.activation.get(p);
        }
        // add bias
        sum += this.bias;
        // get activation function
        this.result = this.activation(sum);
        // send result to output perceptrons
        for (Perceptron p : this.outputs) {
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
        return this.function.activation(x);
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
     * Iterator for this perceptron; iterates over input perceptrons
     */
    private class InputIterator implements Iterator<Perceptron> {

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
        public Perceptron next(){
            if( !this.hasNext() ) throw new NoSuchElementException();
            Perceptron next = Perceptron.this.inputs.get(this.index);
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
}
