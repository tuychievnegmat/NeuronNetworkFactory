package neuron;

import functionActivation.FunctionActivation;

import java.io.Serializable;

class Neuron implements Serializable {
    private double[]inputs;
    private double[]outputs;
    private double[][]weights;
    private int rows;
    private int colls;
    private String name;
    private double learningRate = 0.3;
    private FunctionActivation activation;


    public double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public Neuron(int rows, int colls) {
        this.rows = rows;
        this.colls = colls;
        this.weights = new double[rows][colls];
        this.inputs = new double[rows];
        this.outputs = new double[rows];
        this.name = "Neuron";

    }
    public Neuron (int rows, int colls, String name){
        this(rows, colls);
        this.name = name;
    }

    public Neuron(int rows, int colls, String name, double learningRate, FunctionActivation activation){
        this(rows, colls, name);
        this.learningRate = learningRate;
        this.activation = activation;
    }

    public double[] getInputs() {
        return inputs;
    }

    public double[] getOutputs() {
        return outputs;
    }

    public double[][] getWeights() {
        return weights;
    }

    public int getRows() {
        return rows;
    }

    public int getColls() {
        return colls;
    }

    public double getRangeInput(int index){
        return inputs[index];
    }

    public double getRangeOutput(int index){
        return outputs[index];
    }

    public double getRangeWeight(int rows, int colls){
        return weights[rows][colls];
    }

    public void setRangeInput(int index, double data){
        this.inputs[index] = data;
    }

    public void setRangeOutput(int index, double data){
        this.outputs[index] = data;
    }

    public void setRangeWeight(int rows, int colls, double data){
        this.weights[rows][colls] = data;
    }
    public double[] multMatrix(double[] inputs ) {
//            System.out.format("rows %d: colls %d: name %s\n", rows, colls, name);
        double[] hiddenInputs = new double[rows];
        double[] hiddenOutputs = new double[rows];
        //рассчитать входящие сигналы для скрытого слоя
        for (int i = 0; i < this.rows; i++) {
            //calculate signals into hidden layer
            double hiBias = 0.0D;
            for (int j = 0; j < this.colls; j++) {
                hiBias += this.weights[i][j] * inputs[j];
            }
            hiddenInputs[i] = hiBias;
            //calculate the signals emerging from hidden layer
            hiddenOutputs[i] = activation.getOutput(hiddenInputs[i]);
        }
        return hiddenOutputs;
    }
    public void updateWeight(double[] hiddenOutputs, double[] finalOutputs, double[] outputErrors) {
        //update the weights for the links between the hidden and output layer
        double[]Adj = new double[rows];
        for (int i = 0; i < rows; i++) {
            Adj[i] = outputErrors[i] * finalOutputs[i] * (1.0 - finalOutputs[i]);
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < colls; j++) {
                this.weights[i][j] += learningRate * Adj[i] * hiddenOutputs[j];

            }
        }
    }


}
