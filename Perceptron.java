package aima.core.learning.neural2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 *
 * @author andrew
 */
public class Perceptron implements Iterable{

    List<InputNode> inputs;
    List<OutputNode> outputs;
    HashMap<InputNode, Double> activation;
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
    	this.inputs = new ArrayList<InputNode>();
    	this.outputs = new ArrayList<OutputNode>();
    	this.activation = new HashMap<InputNode, Double>();
        this.activation_function = g;
        this.bias = 0.0d;
        this.state = Perceptron.WAITING;
    }
    
    /**
     * Constructor
     */
    public Perceptron(){
    	this.inputs = new ArrayList<InputNode>();
    	this.outputs = new ArrayList<OutputNode>();
    	this.activation = new HashMap<InputNode, Double>();
        this.bias = 0.0d;
        this.state = Perceptron.WAITING;
    }

    /**
     * Adds input perceptron
     * @param p
     */
    public void addInput(Perceptron p) {
        double weight = Math.random() - 0.5;
        this.inputs.add(new InputNode(p, weight));
    }

    /**
     * Adds output perceptron
     * @param p 
     */
    public void addOutput(Perceptron p) {
        this.outputs.add(new OutputNode(p));
    }

    /**
     * Receives inputs from the governing application
     */
    public void in(double d) {
        if( this.inputs.size() == 0 ){
            InputNode sole_input = new InputNode(1.0);
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
        for(InputNode p : this.inputs) {
            sum += p.weight * this.activation.get(p);
        }
        // add bias
        sum += this.bias;
        // get activation function
        this.result = this.activation(sum);
        // send result to output perceptrons
        for (OutputNode p : this.outputs) {
            p.send(this.result);
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
    private class InputIterator implements Iterator<InputNode> {

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
        public InputNode next(){
            if( !this.hasNext() ) throw new NoSuchElementException();
            InputNode next = Perceptron.this.inputs.get(this.index);
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
    private class InputNode{
        
        /**
         * The weight to be applied to this input perceptron
         */
        double weight = 0.0d;
        
        /**
         * The perceptron link
         */
        Perceptron perceptron;
           
        /**
         * Constructor
         * @param node
         * @param weight
         */
        public InputNode(Perceptron node, double weight){
            this.perceptron = node;
            this.weight = weight;
        }
        
        /**
         * Constructor, for application input
         * @param weight
         */
        public InputNode(double weight){
        	this.weight = weight;
        }
    }

    /**
     * Labels an output perceptron
     */
    private class OutputNode {

    	Perceptron perceptron;
    	
        /**
         * Constructor
         * @param node
         */
        public OutputNode(Perceptron node) {
            this.perceptron = node;
        }

        /**
         * Passes the resulting value on to the output perceptron
         * @param value
         */
        public void send(double value) {
        	this.perceptron.in(Perceptron.this, value);
        }
    }
}
