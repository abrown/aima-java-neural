/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aima.core.learning.neural2;

import java.util.Iterator;

/**
 *
 * @author andrew
 */
public class DataSet{
    double[] values;
    
    /**
     * Constructor
     * @param number_of_values 
     */
    public DataSet(int number_of_values){
        this.values = new double[number_of_values];
    }
    
    /**
     * Returns the size of the dataset
     * @return 
     */
    public int size(){
        return values.length;
    }
    
    /**
     * Returns data values
     * @return 
     */
    public double[] getValues(){
        return this.values;
    }
    
    /**
     * Returns the specified value
     * @param index
     * @return 
     */
    public double get(int index){
        return this.values[index];
    }
    
    /**
     * Sets the specified value
     * @param index
     * @param value 
     */
    public void set(int index, double value){
        this.values[index] = value;
    }
}
