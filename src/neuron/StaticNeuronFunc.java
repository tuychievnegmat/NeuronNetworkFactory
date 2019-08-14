package neuron;

import org.ejml.simple.SimpleMatrix;

public class StaticNeuronFunc {
    public static SimpleMatrix listToTwoDimensionalArray(double[]list, int rows){
        int cells = (int) Math.ceil((double) list.length / (double) rows);
        SimpleMatrix matrix = new SimpleMatrix(rows, cells);
        int listIndex = 0;
        for (int rowIndex = 0;  rowIndex< rows; rowIndex++) {
            for (int cellIndex = 0; cellIndex < cells; cellIndex++) {
                if(listIndex < list.length){
                    matrix.set(rowIndex, cellIndex, list[listIndex++]);
                }
                else {
                    matrix.set(rowIndex, cellIndex, 0);
                }
            }
        }
        return matrix;
    }
}
