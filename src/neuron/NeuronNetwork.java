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
public class NeuronNetwork implements Neurons{
    private int iNodes; //количество входных узлов
    private int hNodes; //количество скрытых узлов
    private int oNodes; //количество выходных узлов
    private int deepHiddenNodes;// количество глубоких узлов
    private double learningRate; //коэфициент обучения

    private InitWeight initWeight; //инициализация вессов в начале
    private FunctionActivation activation;// функция активации
    //метрица весовых коэффицентов
//    private double[][] wih; //связей wih(между входным и скрытым слоями)
//    private double[][] who; //и who (между скрытым и выходными слоями)
    private Neuron[] neurons = new Neuron[3];


    public NeuronNetwork(int inputNodes, int hiddenNodes, int outputNodes, double learningRate,
                         InitWeight initWeight, FunctionActivation activation) {
        this.iNodes = inputNodes;
        this.hNodes = hiddenNodes;
        this.oNodes = outputNodes;
        this.deepHiddenNodes = hiddenNodes - 50;
        this.learningRate = learningRate;
        this.initWeight = initWeight;

//        wih = new double[hNodes][iNodes];
//        who = new double[oNodes][hNodes];
        neurons[0] = new Neuron(hNodes, iNodes, "wih", learningRate, activation); //wih inputs to hidden
        neurons[2] = new Neuron(deepHiddenNodes, hNodes, "whd", learningRate, activation); //hidden to deep hidden
        neurons[1] = new Neuron(oNodes, deepHiddenNodes, "wdo", learningRate, activation); //deep hidden to outputs
        initStartWeight(neurons[0]);
        initStartWeight(neurons[1]);
        initStartWeight(neurons[2]);
        this.activation = activation;
    }
    private void initStartWeight(Neuron neuron){
        for (int i = 0; i < neuron.getRows(); i++) {
            for (int j = 0; j < neuron.getColls(); j++) {
                double initW = initWeight.getInitWeight(neuron.getRows());
                neuron.setRangeWeight(i, j, initW);
            }
        }
    }

    public void train(double[]inputsList, double[] targetsList){


        double[] inputs = inputsList;
        double[] targets = targetsList;


        double[] hiddenOutputs = neurons[0].multMatrix(inputs);

        double[]deepHiddenOuputs = neurons[2].multMatrix(hiddenOutputs);

        double[] finalOutputs = neurons[1].multMatrix(deepHiddenOuputs);

        //ошибки выходного слоя
        double[] outputErrors = new double[oNodes];
        for (int i = 0; i < oNodes; i++) {
            outputErrors[i] = targets[i] - finalOutputs[i];
        }


        //hidden layer error is the outputErrors, spLit by weights, recombined at hidden nodes
        double[]deepHiddenErrors = new double[deepHiddenNodes];
        for (int i = 0; i < deepHiddenNodes; i++) {
            double errors = 0.0D;
            for (int j = 0; j < oNodes; j++) {
                errors = errors + (neurons[1].getRangeWeight(j, i) * outputErrors[j]);
            }
            deepHiddenErrors[i] = errors;
        }


        double[] hiddenErrors = new double[hNodes];
        for (int i = 0; i < hNodes; i++) {
            double errors = 0.0D;
            for (int j = 0; j < deepHiddenNodes; j++) {
                errors = errors + (neurons[2].getRangeWeight(j, i) * deepHiddenErrors[j]);
            }
            hiddenErrors[i] = errors;
        }
        //update the weights
        neurons[1].updateWeight(deepHiddenOuputs, finalOutputs, outputErrors);
        neurons[2].updateWeight(hiddenOutputs, deepHiddenOuputs, deepHiddenErrors);
        neurons[0].updateWeight(inputs, hiddenOutputs, hiddenErrors);
        //update the weights for the links between the input and hidden layers

    }




    ; //тренировка нейронной сети

    private SimpleMatrix multiplyNumberMatrix(double multiply, SimpleMatrix matrix){
        SimpleMatrix rate = new SimpleMatrix();
        for (int i = 0; i < matrix.numRows(); i++) {
            for (int j = 0; j < matrix.numCols(); j++) {
                rate.set(i, j, matrix.get(i, j) * multiply);
            }
        }
        return rate;
    }
    //матричное умножени
    //опрос нейроной сети
    //query the nneural network
    public  double[] query(double[] inputsList){
        double[] inputs = inputsList;


        double[] hiddenOutputs = neurons[0].multMatrix(inputs);

        double[]deepHiddenOuputs = neurons[2].multMatrix(hiddenOutputs);

        double[] finalOutputs = neurons[1].multMatrix(deepHiddenOuputs);


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
        return String.format("name NeuronNetwork, funActivation name %s, initOfWeight %s", activation, initWeight);
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
