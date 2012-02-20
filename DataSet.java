/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aima.core.learning.neural2;

import java.util.ArrayList;

/**
 *
 * @author andrew
 */
public class DataSet extends ArrayList<Double> {

    /**
     * Difference between doubles under which they will still be considered equal
     */
    public static double tolerance = 0.000001;
    
    /**
     * Constructor
     * @param set 
     */
    public DataSet(Double... values){
        for (Double d : values) {
            this.add(d);
        }
    }
    
    /**
     * Constructor
     * @param set 
     */
    public DataSet(double[] set) {
        for (double d : set) {
            this.add(new Double(d));
        }
    }

    /**
     * Constructor
     * @param set 
     */
    public DataSet(ArrayList<Double> set) {
        for (Double d : set) {
            this.add(d);
        }
    }

    /**
     * Creates an ArrayList for use in lower levels
     * @return 
     */
    public ArrayList<Double> toList() {
        return (ArrayList<Double>) this.clone();
    }

    /**
     * Calculate error rate between two datasets
     * @param a
     * @param b
     * @return
     * @throws SizeDifferenceException 
     */
    public static double getErrorRate(DataSet a, DataSet b) throws SizeDifferenceException {
        if (a.size() != b.size()) {
            throw new SizeDifferenceException("To compare datasets, they must be the same size");
        }
        // see page 708: "define the error rate of a hypothesis as the proportion of mistakes it makes"
        int errors = 0;
        double delta_tolerance = 0.000001; // change this as needed
        for (int i = 0; i < a.size(); i++) {
            if( Math.abs(a.get(i) - b.get(i)) < DataSet.tolerance ) errors++;
        }
        // return
        return errors / a.size();
    }
    
//    public static double getErrorRate(DataSet a, DataSet b) throws SizeDifferenceException{
//        if( a.size() != b.size() ) throw new SizeDifferenceException("To compare datasets, they must be the same size");
//        // E = 0.5 * sum of (a_i - b_i)^2 
//        double sum = 0.0;
//        for(int i = 0; i < a.size(); i++){
//            double e = a.get(i) - b.get(i);
//            sum += Math.pow(e, 2);
//        }
//        // return
//        return 0.5 * sum;       
//    }
    
}
