package aima.core.learning.neural2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

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
        this.state = NeuralNetwork.WAITING;
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
        this.state = NeuralNetwork.WAITING;
    }

    /**
     * Adds input perceptron with a random weight
     * @param p
     */
    public void addInput(Perceptron p) {
        double weight = Math.random() - 0.5;
        this.addInput(p, weight);
    }
    
    /**
     * Adds input perceptron
     * @param p
     * @param weight
     */
    public void addInput(Perceptron p, double weight){
    	this.weights.put(p, weight);
    	this.inputs.add(p);
    	// bi-directional connection
    	p.outputs.add(this);
    }

    /**
     * Adds output perceptron
     * @param p 
     */
    public void addOutput(Perceptron p) {
        this.outputs.add(p);
        // bi-directional connection
        p.weights.put(this, Math.random() - 0.5);
        p.inputs.add(this);
    }

    /**
     * Receives inputs from the governing application;
     * implies there are no input perceptrons to this one.
     */
    public void in(double d) {
    	// change state
    	this.state = NeuralNetwork.RECEIVING;
    	// debug
        if( NeuralNetwork.DEBUG ) System.out.println("Application -[" + d + "]-> " + this);
    	// create stub perceptron if necessary
        this.out(d);
    }

    /**
     * Accepts inputs from input perceptron
     * @param p
     * @param d 
     */
    public void in(Perceptron p, double d) {
        // change state
        this.state = NeuralNetwork.RECEIVING;
        // debug
        if( NeuralNetwork.DEBUG ) System.out.println(p + " -[" + d + "]-> " + this);
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
     * Send immediately, without activation function,
     * the passed value; used for sending data from
     * governing application
     * @param d
     */
    public void out (double d){
    	// change state
    	this.state = NeuralNetwork.SENDING;
    	// set result
    	this.result = d;
    	// send to outputs
    	for (Perceptron output : this.outputs){
    		output.in(this, this.result);
    	}
    	// change state
    	this.state = NeuralNetwork.WAITING;
    }
    	
    /**
     * Consumes input values and produces output signal to downstream perceptrons
     */
    public void out() {
        // change state
        this.state = NeuralNetwork.SENDING;
        // sum inputs
        double sum = 0.0;
        for(Perceptron p : this.inputs) {
            sum += this.weights.get(p) * this.activation.get(p);
        }
        // add bias
        sum += this.bias;
        // get activation function
        this.result = this.activation(sum);
        // debug
        if( NeuralNetwork.DEBUG ) System.out.println(this + " = [" + this.result + "]");
        // send result to output perceptrons
        for (Perceptron p : this.outputs) {
            p.in(this, this.result);
        }
        // reset activation map
        this.activation.clear();
        // change state
        this.state = NeuralNetwork.WAITING;
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
     * Tests whether the input queue is full
     * @return 
     */
    public boolean isFull() {
        return this.activation.size() == this.inputs.size();
    }
    
    /**
     * Tests whether this perceptron has sent out an activation value
     * @return 
     */
    public boolean isComplete() {
        return (this.state == NeuralNetwork.WAITING);
    }
    
    /**
     * Returns string representation of a perceptron
     */
    public String toString(){
    	StringBuilder s = new StringBuilder("Perceptron@");
    	s.append(Integer.toHexString(this.hashCode()));
    	return s.toString();
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
