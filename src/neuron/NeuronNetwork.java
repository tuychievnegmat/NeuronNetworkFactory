package neuron;

import functionActivation.FunctionActivation;
import functionActivation.SigmoidFun;
import initializationOfWeight.InitWeight;
import org.ejml.simple.SimpleMatrix;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by k-terra on 11.07.2019.
 */
public class NeuronNetwork implements Serializable{
    private int iNodes; //количество входных узлов
    private int hNodes; //количество скрытых узлов
    private int oNodes; //количество выходных узлов
    private double learningRate; //коэфициент обучения

    private InitWeight initWeight; //инициализация вессов в начале
    private FunctionActivation activation;// функция активации
    //метрица весовых коэффицентов
    private double[][] wih; //связей wih(между входным и скрытым слоями)
    private double[][] who; //и who (между скрытым и выходными слоями)


    public NeuronNetwork(int inputNodes, int hiddenNodes, int outputNodes, double learningRate,
                         InitWeight initWeight, FunctionActivation activation) {
        this.iNodes = inputNodes;
        this.hNodes = hiddenNodes;
        this.oNodes = outputNodes;
        this.learningRate = learningRate;
        this.initWeight = initWeight;

//        wih = new SimpleMatrix(hNodes, iNodes);
//        who = new SimpleMatrix(oNodes, hNodes);
        wih = new double[hNodes][iNodes];
        who = new double[oNodes][hNodes];
        initStartWeight(wih);
        initStartWeight(who);
        this.activation = activation;
    }
    private void initStartWeight(double[][] arr){
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                arr[i][j] = initWeight.getInitWeight(arr.length);
            }
        }
    }

    public void train(double[]inputsList, double[] targetsList){

        double[] inputs = inputsList;
        double[] targets = targetsList;



        double[]hiddenInputs = new double[hNodes];
        double[]hiddenOutputs = new double[hNodes];
        //рассчитать входящие сигналы для скрытого слоя
        for (int i = 0; i < hNodes; i++) {
            //calculate signals into hidden layer
            double hiBias = 0.0D;
            for (int j = 0; j < iNodes; j++) {
                hiBias += wih[i][j] * inputs[j];
            }
            hiddenInputs[i] = hiBias;
            //calculate the signals emerging from hidden layer
            hiddenOutputs[i] = activation.getOutput(hiddenInputs[i]);
        }


        double[] finalInputs = new double[oNodes];
        double[] finalOutputs = new double[oNodes];

        for (int i = 0; i < oNodes; i++) {
            //calculate signals into final output layer
            double fiBias = 0.0D;
            for (int j = 0; j < hNodes; j++) {
                fiBias += (who[i][j] * hiddenOutputs[j]);
            }
            finalInputs[i] = fiBias;
            //calculate the signals emerging from final output layer
            finalOutputs[i] = activation.getOutput(finalInputs[i]);
        }
        //output layer error is the (target - actual)
        double[] outputErrors = new double[oNodes];
        for (int i = 0; i < oNodes; i++) {
            outputErrors[i] = targets[i] - finalOutputs[i];
        }

        //hidden layer error is the outputErrors, spLit by weights, recombined at hidden nodes
        double[]hiddenErrors = new double[hNodes];
        for (int i = 0; i < hNodes; i++) {
            double errors = 0.0D;
            for (int j = 0; j < oNodes; j++) {
                errors = errors + (who[j][i] * outputErrors[j]);
            }
            hiddenErrors[i] = errors;
        }

        //update the weights for the links between the hidden and output layer
        double[]whoAdj = new double[oNodes];
        for (int i = 0; i < oNodes; i++) {
            whoAdj[i] = outputErrors[i] * finalOutputs[i] * (1.0 - finalOutputs[i]);
        }
        for (int i = 0; i < oNodes; i++) {
            for (int j = 0; j < hNodes; j++) {
                who[i][j] += learningRate * whoAdj[i] * hiddenOutputs[j];
            }
        }

        //update the weights for the links between the input and hidden layers
        double[]wihAdj = new double[hNodes];
        for (int i = 0; i < hNodes; i++) {
            wihAdj[i] = hiddenErrors[i] * hiddenOutputs[i] * (1.0 - hiddenOutputs[i]);
        }
        for (int i = 0; i < hNodes; i++) {
            for (int j = 0; j < iNodes; j++) {
                wih[i][j] += learningRate * wihAdj[i] * inputs[j];
            }
        }
    }; //тренировка нейронной сети

    private SimpleMatrix multiplyNumberMatrix(double multiply, SimpleMatrix matrix){
        SimpleMatrix rate = new SimpleMatrix();
        for (int i = 0; i < matrix.numRows(); i++) {
            for (int j = 0; j < matrix.numCols(); j++) {
                rate.set(i, j, matrix.get(i, j) * multiply);
            }
        }
        return rate;
    }
    //матричное умножение



    //опрос нейроной сети

    //query the nneural network
    public  double[] query(double[] inputsList){
        double[] inputs = inputsList;

        double[] hiddenInputs = new double[hNodes];
        double[] hiddenOutputs = new double[hNodes];

        for (int i = 0; i < hNodes; i++) {
            //calculate signals into hidden layer
            double hiBias = 0.0D;
            for (int j = 0; j < iNodes; j++) {
                hiBias += wih[i][j] * inputs[j];
            }
            hiddenInputs[i] = hiBias;
            //calculate the signals emergng from hidden layer
            hiddenOutputs[i] = activation.getOutput(hiddenInputs[i]);
        }

        double[]finalInputs = new double[oNodes];
        double[]finalOutputs = new double[oNodes];
        for (int i = 0; i < oNodes; i++) {
            //calculate signals into final output layer
            double fiBias = 0.0D;
            for (int j = 0; j < hNodes; j++) {
                fiBias += who[i][j] * hiddenOutputs[j];
            }
            finalInputs[i] = fiBias;
            //calculate the signals emergikng from final output layer
            finalOutputs[i] = activation.getOutput(finalInputs[i]);
        }

        return finalOutputs;
    }; //опрос нейронной сети

    //преобразовать лист в двумерный массив
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

    //инициализация спомощью функции активации
    private SimpleMatrix funcActivation(SimpleMatrix matrix ){

        SimpleMatrix funMatrix = new SimpleMatrix(matrix.numRows(), matrix.numCols());
        for (int i = 0; i < matrix.numRows(); i++) {
            for (int j = 0; j < matrix.numCols(); j++) {
                funMatrix.set(i, j, activation.getOutput(matrix.get(i, j)));
            }
        }

        return funMatrix;
    }


    @Override
    public String toString() {
        return "NeuronNetwork{" +
                "matrixHiddenInput=" + wih +
                ", matrixOutputHidden=" + who +
                '}' ;
    }



    //Инициализация целевого массива
    public static double[] InitTargetArray(double[] arrayTarget, double initAllArray, double target, int indexTarget){

        double[] arr = new double[arrayTarget.length];
        System.out.println("target size: " + arrayTarget.length);
        System.out.println("InitTargetArray indexTarget: " + indexTarget);
        for (int i = 0; i < arrayTarget.length; i++) {

            System.out.println("i InitTargetArray: " + i);
            if(i == indexTarget)
               arr[i] = target;
            else
                arr[i] = initAllArray;
        }



        return arr;
    }
}
