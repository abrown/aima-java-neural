package aima.core.learning.neural2;

import java.util.ArrayList;

/**
 *
 * @author andrew
 */
public class NeuralNetwork {
	
	/**
	 * Constants
	 */
    public static final byte WAITING = 0;
    public static final byte RECEIVING = 1;
    public static final byte SENDING = 2;
	public static final boolean DEBUG = true;
    
	/**
	 * List of interconnected layers
	 */
    public ArrayList<Layer> layers;
    
    /**
     * Constructor
     * @param number_of_layers
     * @param perceptrons_per_layer
     * @param g 
     */
    public NeuralNetwork(int number_of_layers, int perceptrons_per_layer, ActivationFunction_I g){
        // setup
        this.layers = new ArrayList<Layer>();
        // create layers
        for(int i=0; i<number_of_layers; i++){
            this.layers.add(new Layer(perceptrons_per_layer, g));
        }
        // connect layers
        for(int j=0; j < this.layers.size() - 1; j++){
            this.layers.get(j).connectTo( this.layers.get(j+1) );
        }
    }
    
    /**
     * Trains a network against a given dataset
     * @param input
     * @param expected_output
     * @return
     * @throws SizeDifferenceException 
     */
    public int train(ArrayList<Double> input, ArrayList<Double> expected_output) throws SizeDifferenceException{
        // iterate until complete
        int iterations = 0;
        ArrayList<Double> output = null;
        do{
            iterations++;
            output = this.use(input);
            // TODO: train by backpropagation
        }
        while( !output.equals(expected_output) );
        // return 
        return iterations;    
    }
    
    /**
     * Tests the given input for correctness
     * @param input
     * @param expected_output
     * @return a proportion of correct results within the dataset
     * @throws SizeDifferenceException 
     */
    public float test(ArrayList<Double> input, ArrayList<Double> expected_output) throws SizeDifferenceException{
        ArrayList<Double> output = this.use(input);
        if( output.size() != expected_output.size() ) 
            throw new SizeDifferenceException("DataSet sizes (returned, "+output.size()+"; expected, "+expected_output.size()+") do not match");
        // create proportion
        int total = output.size();
        int correct = 0;
        for(int i=0; i<total; i++){
            if( output.get(i) == expected_output.get(i) ) correct++;
        }
        // return
        return correct/total;
    }
    
    /**
     * Uses the neural network to determine the output for the given input
     * @param input
     * @return
     * @throws SizeDifferenceException 
     */
    public ArrayList<Double> use(ArrayList<Double> input) throws SizeDifferenceException{
        // send input
        this.layers.get(0).in( input );
        // receive output
        int last = this.layers.size() - 1;
        ArrayList<Double> output = this.layers.get(last).out();
        // return
        return output;
    }
}
