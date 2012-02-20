package aima.core.learning.neural2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author andrew
 */
public class NeuralNetwork implements Serializable {

    /**
     * Constants
     */
    public static final byte WAITING = 0;
    public static final byte RECEIVING = 1;
    public static final byte SENDING = 2;
    public static final byte COMPLETE = 3;
    /**
     * Turns on debugging
     */
    public static final boolean DEBUG = true;
    /**
     * List of interconnected layers
     */
    public ArrayList<Layer> layers;

    /**
     * Constructor
     * @param number_of_layers
     * @param perceptrons_per_layer
     * @param function
     * @param sensitivity
     */
    public NeuralNetwork(int number_of_layers, int perceptrons_per_layer, ActivationFunction_I function, double sensitivity) {
        // setup
        this.layers = new ArrayList<Layer>();
        // create layers
        for (int i = 0; i < number_of_layers; i++) {
            this.layers.add(new Layer(perceptrons_per_layer, function, sensitivity));
        }
        // connect layers
        for (int j = 0; j < this.layers.size() - 1; j++) {
            this.layers.get(j).connectTo(this.layers.get(j + 1));
        }
    }

    /**
     * Trains a network against a given dataset
     * @param input
     * @param expected_output
     * @return
     * @throws SizeDifferenceException 
     */
    public int train(DataSet[] inputs, DataSet[] outputs, double target_error_rate) throws SizeDifferenceException, WrongSizeException {
        if (inputs.length != outputs.length) {
            throw new SizeDifferenceException("Must have the same number of training inputs and outputs");
        }
        // train until average error rate is below target error rate
        int iterations = 0;
        double average_error_rate = 1.0;
        do {
            iterations++;
            double error_sum = 0.0;
            for (int i = 0; i < inputs.length; i++) {
                ArrayList<Double> output = new DataSet(this.use(inputs[i].toList()));
                error_sum += DataSet.getErrorRate(new DataSet(output), outputs[i]);
                // train
                this.last().backpropagate(outputs[i]);
            }
            // calculate error rate
            average_error_rate = error_sum / inputs.length;
        } while (average_error_rate > target_error_rate);
        // return 
        return iterations;
    }

    /**
     * Tests multiple datasets, returning the average error
     * @param inputs
     * @param outputs
     * @return a proportion of correct results within the dataset
     * @throws SizeDifferenceException 
     */
    public double test(DataSet[] inputs, DataSet[] outputs) throws SizeDifferenceException, WrongSizeException {
        if (inputs.length != outputs.length) {
            throw new SizeDifferenceException("Must have the same number of training inputs and outputs");
        }
        // find average error rate
        double error_sum = 0.0;
        for (int i = 0; i < inputs.length; i++) {
            ArrayList<Double> output = new DataSet(this.use(inputs[i].toList()));
            error_sum += DataSet.getErrorRate(new DataSet(output), outputs[i]);
            // train
            this.last().backpropagate(outputs[i]);
        }
        // calculate error rate
        return error_sum / inputs.length;
   }
    
    /**
     * Test a single dataset for correctness
     * @param input
     * @param expected_output
     * @return
     * @throws SizeDifferenceException
     * @throws WrongSizeException 
     */
    public double test(DataSet input, DataSet expected_output)throws SizeDifferenceException, WrongSizeException{
        DataSet out = new DataSet( this.use(input.toList()) );
        return DataSet.getErrorRate(out, expected_output);
    }

    /**
     * Uses the neural network to determine the output for the given input
     * @param input
     * @return
     * @throws SizeDifferenceException 
     */
    public ArrayList<Double> use(ArrayList<Double> input) throws SizeDifferenceException, WrongSizeException {
        // send input
        this.first().in(input);
        // receive output
        ArrayList<Double> output = this.last().out();
        // return
        return output;
    }

    /**
     * Returns the first layer in this network
     * @return 
     */
    public Layer first() {
        return this.layers.get(0);
    }

    /**
     * Returns the last layer in this network
     * @return 
     */
    public Layer last() {
        int end = this.layers.size() - 1;
        return this.layers.get(end);
    }

    /**
     * Saves a given network to file
     * @param file
     * @param net
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException 
     */
    public static void save(File file, NeuralNetwork net) throws java.io.IOException, java.io.FileNotFoundException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
        out.writeObject(net);
        out.flush();
        out.close();
    }

    /**
     * Loads a network from file
     * @param file
     * @return
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     * @throws ClassNotFoundException 
     */
    public static NeuralNetwork load(File file) throws java.io.IOException, java.io.FileNotFoundException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
        return (NeuralNetwork) in.readObject();
    }
}
