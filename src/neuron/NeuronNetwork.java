package neuron;

import NeuronException.WrongDeepException;
import com.sun.org.apache.xml.internal.utils.WrongParserException;
import functionActivation.ActivationType;
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
import java.util.logging.Logger;

/**
 * Created by k-terra on 11.07.2019.
 */
public class NeuronNetwork implements Neurons{
    private int iNodes; //количество входных узлов
    private int hNodes; //количество скрытых узлов
    private int oNodes; //количество выходных узлов
    private int deepHiddenNodes;// количество глубоких узлов
    private int deep; //глубина - количество слоев скрытых минимум один
    private double learningRate; //коэфициент обучения
    private double[][]outputs; //выходы для подстчета
    private double[][]outputsErrors;//выходные ошибки
    private int lenght; //длина количество слоев


    private InitWeight initWeight; //инициализация вессов в начале
    private FunctionActivation activation;// функция активации
    //метрица весовых коэффицентов
//    private double[][] wih; //связей wih(между входным и скрытым слоями)
//    private double[][] who; //и who (между скрытым и выходными слоями)
    private Neuron[] neurons;


    public NeuronNetwork(int inputNodes, int hiddenNodes, int outputNodes, double learningRate, int deep,
                         InitWeight initWeight, FunctionActivation activation) {
        this.iNodes = inputNodes;
        this.hNodes = hiddenNodes;
        this.oNodes = outputNodes;
        this.deepHiddenNodes = hiddenNodes - 50;
        this.learningRate = learningRate;
        this.initWeight = initWeight;
        this.deep = deep;
        try {
            if(deep < 1)throw new WrongDeepException();
        } catch (WrongDeepException e) {
            System.out.format("wrong deep, deep = %d", deep);
            e.printStackTrace();
        }
        this.lenght = 2 + deep;
        neurons = new Neuron[lenght];
        outputs = new double[lenght][];
        outputsErrors = new double[lenght][];


        if(neurons.length == 3){
            neurons[0] = new Neuron(hNodes, iNodes, "wih", learningRate, activation); //wih inputs to hidden
            neurons[1] = new Neuron(hNodes, hNodes, "whd", learningRate, activation); //hidden to deep hidden
            neurons[2] = new Neuron(oNodes, hNodes, "wdo", learningRate, activation); //deep hidden to outputs
        }else {
            neurons[0] = new Neuron(hNodes, iNodes, "wih", learningRate, activation); //wih inputs to hidden
            for (int i = 1; i < lenght -1; i++) {
                neurons[i] = new Neuron(hNodes, hNodes, String.format("wdd[%d]", i), learningRate, activation);
            }
            //тест проверка
            neurons[neurons.length-1] = new Neuron(oNodes, hNodes, String.format("wdo[%d]", neurons.length-1 ),learningRate, activation);
        }



        //инициализация нейронных узлов
        for (int i = 0; i < lenght; i++) {
            StaticNeuronFunc.initStartWeight(neurons[i], initWeight);
        }
        this.activation = activation;
    }


    public void train(double[]inputsList, double[] targetsList){


        double[] inputs = inputsList;
        double[] targets = targetsList;

//        //вычисление для несколько слев
        outputs[0] = neurons[0].multMatrix(inputs);
        for (int i = 1; i < lenght; i++) {
            outputs[i] = neurons[i].multMatrix(outputs[i-1]);
        }

        //outputsErrors
        outputsErrors[lenght-1] = new double[oNodes];//выходной узел ошибки test
        //ошибки выходного слоя
        double[] outputErrors = new double[oNodes];
        for (int i = 0; i < oNodes; i++) {
            outputsErrors[lenght-1][i] = targets[i] - outputs[lenght-1][i];
        }

        //hidden layer error is the outputErrors, spLit by weights, recombined at hidden nodes
        //test
        System.out.format("lenght %d", lenght);
        outputsErrors[lenght-2] = getErrorCounting(outputsErrors[lenght-1], oNodes , hNodes, lenght-1);


        for (int i = lenght-2; i >=1 ; i--) {
            outputsErrors[i-1] = getErrorCounting(outputsErrors[i],hNodes, hNodes, i);
        }

        //update the weights
        for (int i = neurons.length-1; i >= 1 ; i--) {
            neurons[i].updateWeight(outputs[i- 1], outputs[i], outputsErrors[i]);
        }
        neurons[0].updateWeight(inputs,outputs[1], outputsErrors[1]);
    }

    public double[] getErrorCounting(double[] outputErrors, int oNodes, int hNodes, int i2) {
        double[] deepHiddenErrors = new double[hNodes];
        for (int i = 0; i < hNodes; i++) {
            double errors = 0.0D;
            for (int j = 0; j < oNodes; j++) {
                errors = errors + (neurons[i2].getRangeWeight(j, i) * outputErrors[j]);
            }
            deepHiddenErrors[i] = errors;
        }
        return deepHiddenErrors;
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

        outputs[0]  = neurons[0].multMatrix(inputsList);
        for (int i = 1; i < lenght; i++) {
            outputs[i] = neurons[i].multMatrix(outputs[i-1]);
        }

        return outputs[lenght-1];
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

    @Override
    public ActivationType getActivation() {
        return activation.getActivation();
    }
}
