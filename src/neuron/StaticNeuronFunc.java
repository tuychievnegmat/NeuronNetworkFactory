package neuron;

import initializationOfWeight.InitWeight;
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
    public static void initStartWeight(Neuron neuron, InitWeight initWeight){
        for (int i = 0; i < neuron.getRows(); i++) {
            for (int j = 0; j < neuron.getColls(); j++) {
                double initW = initWeight.getInitWeight(neuron.getRows());
                neuron.setRangeWeight(i, j, initW);
            }
        }
    }
}
