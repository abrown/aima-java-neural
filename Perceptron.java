package aima.core.learning.neural2;

import java.util.List;


/**
 *
 * @author andrew
 */
public class Perceptron {
    
    List<Perceptron> inputs;
    List<Perceptron> outputs;
    Double bias;
    List<Double> weights;
    List<Double> input_queue;
    ActivationFunction activation_function;
    Double activation_result;
    
    /**
     * Constructor
     * @param g 
     */
    public Perceptron(ActivationFunction g){
        this.activation_function = g;
        this.bias = Math.random();
    }
    
    /**
     * Adds input perceptron
     * @param p 
     */
    public void addInput(Perceptron p){
        this.inputs.add(p);
        int i = this.inputs.indexOf(p);
        this.weights.set(i, Math.random() - 0.5);
    }
    
    /**
     * Adds output perceptron
     * @param p 
     */
    public void addOutput(Perceptron p){
        this.outputs.add(p);
    }
    
    /**
     * 
     */
    public void in(double d){
        this.input_queue.set(0, d);
        this.out();
    }
    
    /**
     * Accepts inputs from input perceptron
     * @param p
     * @param d 
     */
    public void in(Perceptron p, double d){
        int i = this.inputs.indexOf(p);
        // if( i == -1 ) throw new Exception("Perceptron is not linked.");
        this.input_queue.set(i, d);
        if( this.isFull() ) this.out(); // TODO: perhaps just test for activation here, not wait for all inputs to come in; this might be necessary in recurrent networks
    }
    
    /**
     * Tests whether the input queue is full
     * @return 
     */
    public boolean isFull(){
        return this.input_queue.size() == this.inputs.size();
    }  
    
    /**
     * Consumes input values and produces output signal to downstream perceptrons
     */
    public void out(){
        double sum = 0.0;
        // case: this perceptron has no inputs, values sent by application
        if( this.inputs.size() == 0 ){
            int end = this.input_queue.size();
            for( int i = 0; i < end; i++ ){
                sum += this.input_queue.get(i);
            }
        }
        // case: sum all input values from input perceptrons
        else{
            int end = this.inputs.size();
            for( int i = 0; i < end; i++ ){
                sum += this.weights.get(i) * this.input_queue.get(i);
            }
            // add in the bias
            sum += this.bias; // TODO: perhaps this applies to perceptrons with no inputs as well?
        }
        // get activation function
        this.activation_result = this.activation(sum);
        // send result to output perceptrons
        int end = this.outputs.size();
        for( int j = 0; j < end; j++ ){
            this.outputs.get(j).in(this, activation_result);
        }
    }
    
    /**
     * Returns activation function value
     * @param x
     * @return 
     */
    public double activation(double x){
        return this.activation_function.activation(x);
    }
    
    /**
     * Tests whether this perceptron has sent out an activation value
     * @return 
     */
    public boolean isComplete(){
        return (this.activation_result == null);
    }
}
