package test;


import factory.FactoryNeuron;
import factory.TypeNeuron;
import functionActivation.ActivationType;
import functionActivation.FunctionActivation;
import functionActivation.GiperbolicTangesFun;
import functionActivation.SigmoidFun;
import initializationOfWeight.NormalWeight;
import neuron.NeuronNetwork;
import neuron.Neurons;
import neuron.StaticNeuronFunc;
import org.ejml.simple.SimpleMatrix;
import view.Model;
import view.View;

import javax.swing.*;
import java.io.*;
import java.util.*;


/**
 * Created by k-terra on 11.07.2019.
 */
public class Main {
    public static void main(String[] args) {
        //test fun activation

        //количество входных, скрытых и выходных узлов
        int inputNodes = 784;
        int hiddenNodes = 250;
        int outputNodes = 10;
        boolean dounwload = false;

        //кооэфицент обучения равен 0.3
        double learningRate = 0.2;
        int deep = 1;

        TypeNeuron typeNeuron = TypeNeuron.NORMAL_SIGMOID_INIT;


        //создать экземпляр нейронной сети
        Neurons neuronNetwork = FactoryNeuron.FactoryNeuron(inputNodes, hiddenNodes, outputNodes, learningRate, typeNeuron, deep);
        //функция активации
        ActivationType functionActivation = neuronNetwork.getActivation();
        System.out.println(neuronNetwork.toString());

        //загрузить в список тестовый наборданных CSV - файла набора MNIST
        File fileTraning = new File("D:\\javarush\\train data for neuron network\\mnist_train_100.csv");
        double[][] arrayTrainList = readFileToList(fileTraning,false);



        File fileTest = new File("D:\\javarush\\train data for neuron network\\mnist_test_10.csv");
        double[][] arrayTestList = readFileToList(fileTest, false);




            //train the neural network
            //epochs is the number of times the trining data set is used for trainin
        Date startTime, finishTime;
        startTime = new Date();
            int epochs = 5;
            int mnistLabel = 0;
            double[]inputs = new double[inputNodes];
            double[]targets = new double[outputNodes];
            if(dounwload){
                try {
                    ObjectInputStream read = new ObjectInputStream(new FileInputStream("save neuron network.txt"));
                    neuronNetwork = (NeuronNetwork) read.readObject();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e){

                }

            }
            else {
                //начало времени измерения алгоритма

                for (int p = 0; p < epochs; p++) {
                    for (int i = 0; i < arrayTrainList.length; i++) {
                        //go through all records in the training data set
                        for (int j = 0; j < inputNodes + 1; j++) {
                            if (j == 0) {
                                mnistLabel = (int) arrayTrainList[i][j];
                            } else {
                                double input = arrayTrainList[i][j];
                                //scale and shift the inputs
                                input = (input / 255.0 * functionActivation.getCONST_TARGET()) + 0.01;
                                inputs[j - 1] = input;
                            }
                        }
                        //create the target output values(all 0.01, except the desired label which is 0.99)
                        for (int j = 0; j < outputNodes; j++) {
                            targets[j] = functionActivation.getCONST_WRONG();
                        }


                        targets[mnistLabel] = functionActivation.getCONST_TARGET();
                        neuronNetwork.train(inputs, targets);
                    }

                }
                try {
                    ObjectOutputStream write = new ObjectOutputStream(new FileOutputStream("save neuron network.txt"));
                    write.writeObject(neuronNetwork);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        finishTime = new Date();

        System.out.format("затраченно времени\n start %s\n finish %s\n", startTime.toString(), finishTime.toString());
        int min = finishTime.getMinutes() - startTime.getMinutes();
        int sec = finishTime.getSeconds() - startTime.getSeconds();
        System.out.format("min:%d sec:%d", min, sec);

        //test the neural network
        //scorecard for how well the network performs, initially empty
        double[]scoreCard = new double[arrayTestList.length];

        //go through all the records in the test data set

        double correctLabel = 0.0;
        for (int read = 0; read < arrayTestList.length; read++) {
            for (int j = 0; j < inputNodes; j++) {
                if(j == 0){
                    //correct answer is first value
                    correctLabel = arrayTestList[read][j];

                }else {
                    double input = arrayTestList[read][j];
                    //scale and shift the inputs
                    input = (input / 255.0 * functionActivation.getCONST_TARGET()) + 0.01;
                    inputs[j -1 ] = input;
                }
            }
            //query the network
            double[]outputs = neuronNetwork.query(inputs);

//            for (int i = 0; i < outputs.length; i++) {
//                System.out.format("[%d][%f]: ", i, outputs[i]);
//            }
//            System.out.println("finish outputs: ");

            //the index of the highest value corresponds to the label
            double label = 0;
            double maxOut = 0.0;
            for (int k = 0; k < outputs.length; k++) {
                if (maxOut < outputs[k]){
                    maxOut = outputs[k];
                    label = k;
            }
            }
//            System.out.format("correctLabel %f, label %f ###", correctLabel, label);
            //append correct or incorrect to list
            if(label == correctLabel){
                //network's answer matches ccrrect answer, add 1 to scorecard
                scoreCard[read] = 1;
            }else {
                //network's answer doesn't match correct answer, add 0 to scoreCard
                scoreCard[read] = 0;
            }

            for (int i = 0; i < arrayTestList.length; i++) {
                double[] arr = arrayTestList[i];
                SimpleMatrix matrix = NeuronNetwork.listToTwoDimensionalArray(Arrays.copyOfRange(arr, 1, arr.length), 28);

            }

        }

        //calculate the performance score, the fraction of correct answers
        double perfomance = 0.0;
        double scoreSum = 0.0;
        for (int k = 0; k < scoreCard.length; k++) {
            scoreSum = scoreSum + scoreCard[k];
        }

        perfomance = scoreSum / scoreCard.length;
        System.out.println("perfomance = " + perfomance);

        forTrainAndShow(arrayTestList, neuronNetwork, 28,scoreCard);
        }



        //показать матрицу
    public static void showMatrix(SimpleMatrix matrix){
        JFrame game = new JFrame();
        Model model = new Model(matrix);
        show(model, game, "255", "test");
    }

        //тест проверки входных данных после подготовки
    public static void forTrainAndShow(double[][]arrayList, Neurons neuronNetwork, int numRows, double[]scoreCard){
        List<Double> testlist;
        Model model;
        JFrame game = new JFrame();
        int i = 0;
        SimpleMatrix matrix = new SimpleMatrix();
        for (double[] lists : arrayList) {
            String ch = Double.toString(lists[0]);
            //testlist = neuronNetwork.inputForTrain(lists);


            double[]listToDimensional = Arrays.copyOfRange(lists, 1, lists.length);
            matrix = StaticNeuronFunc.listToTwoDimensionalArray(listToDimensional, numRows);


            model = new Model(matrix);
            String answer = "";
            System.out.format("score card: %f, %d", scoreCard[i], i);
            System.out.println();
            int index =(int) Double.parseDouble(ch);
            if(scoreCard[index] == 1.0)answer = Integer.toString(index);
            else answer ="wrong";
            show(model, game, ch, answer);
            i++;


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }




    private static void show(Model model, JFrame game, String ch, String queryResult){

        View view = new View(model.getGameTiles(), ch, queryResult);
//        JFrame game = new JFrame();


        game.setTitle("Neuron Network");
        game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game.setSize(1024, 1024);
        game.setResizable(false);
//        game.add(view);
        game.setContentPane(view);
        game.setLocationRelativeTo(null);
        game.setVisible(true);



    }

    //чтение файла и превращение его в список true подготовленные данные, false без подготовки
    // (value/255.0 * 0.99) + 0.01
    public static double[][] readFileToList(File file, boolean preparedData){
        List<List<Double>> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
            StringTokenizer tokenizer;

            System.out.println("file name: " + file.getName());
            while (reader.ready()) {
                tokenizer = new StringTokenizer(reader.readLine(), ", ");
                List<Double>testlist = new ArrayList<Double>();

                while (tokenizer.hasMoreTokens()) {
                    testlist.add(Double.parseDouble(tokenizer.nextToken()));
                }
                list.add(testlist);

            }
        }catch (FileNotFoundException e){

        }catch (IOException e){

        }

        double[][] doubles = new double[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            doubles[i] = new double[list.get(i).size()];
            for (int j = 0; j < list.get(i).size(); j++) {
                doubles[i][j] = list.get(i).get(j);
            }
        }

        return doubles;
    }


    private static void update(Model model){

    }

}