package neuron;

import functionActivation.FunctionActivation;
import initializationOfWeight.InitWeight;

public class TwoHemispheresNeuronNetwork implements Neurons
{
    private int iNodes; //количество входных узлов
    private int twoHemispheresNodes; //колилество скрытых узлов левого и правого полушария

    private int cCDHNodes; //corpus callosum - мозолистое тело. глубокий скрытый узел объединяющий оба полушария
    private double lLearningRate; //коэфицент обучения для левого полушария
    private double rLearningRate; //коэфициент обучения для правого полушария
    private double ccLearningRate; //коэфицент обучения для мозолистого тела
    private int oNodes; //количество выходных узлов
    private InitWeight initWeight; //инициализация весов
    private FunctionActivation activation; //функция активации
    private Neuron[] neurons = new Neuron[4];//нейронные сети


    public TwoHemispheresNeuronNetwork(int iNodes, int twoHemispheresNodes, int cCDHNodes, int oNodes,
                                       InitWeight initWeight, FunctionActivation activation) {
        this.iNodes = iNodes;
        this.twoHemispheresNodes = twoHemispheresNodes;
        this.cCDHNodes = cCDHNodes;
        this.oNodes = oNodes;
        this.lLearningRate = 0.2;
        this.rLearningRate = 0.2;
        this.ccLearningRate = 0.2;
        this.initWeight = initWeight;
        this.activation = activation;
        this.neurons[0] = new Neuron(twoHemispheresNodes, iNodes, "левое полушарие", lLearningRate, activation); //входные к левому
        this.neurons[1] = new Neuron(twoHemispheresNodes, iNodes, "правое полушарие",rLearningRate, activation); //входные к правому
        this.neurons[2] = new Neuron(cCDHNodes, twoHemispheresNodes, "промежуточное полушарие", ccLearningRate, activation);//
        this.neurons[3] = new Neuron(oNodes, cCDHNodes, "мозолистое к выходу", ccLearningRate, activation);

        StaticNeuronFunc.initStartWeight(neurons[0], initWeight); //левое полушарие
        StaticNeuronFunc.initStartWeight(neurons[1], initWeight); //правое полушарие
        StaticNeuronFunc.initStartWeight(neurons[2], initWeight); //промежуточное полушарие
        StaticNeuronFunc.initStartWeight(neurons[3], initWeight); //мозолистое к выходу



    }

    @Override
    public void train(double[] inputsList, double[] targetsList) {

        double[] inputs = inputsList;
        double[] targets = targetsList;

        double[]leftOutputs = neurons[0].multMatrix(inputs);
        double[]rightOutputs = neurons[1].multMatrix(inputs);

        double[]intermediateNode = sumMatrix(leftOutputs,rightOutputs);
        double[]intermediateOutputs = neurons[2].multMatrix(intermediateNode);



    }
    private double[]sumMatrix(double[]first, double[]seond){
        double[]outputs = new double[first.length];
        for (int i = 0; i < first.length; i++) {
            outputs[i] = first[i] + seond[i];
        }
        return outputs;
    }

    @Override
    public double[] query(double[] inputsList) {
        return new double[0];
    }
}
