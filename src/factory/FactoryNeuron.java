package factory;

import functionActivation.GiperbolicTangesFun;
import functionActivation.SigmoidFun;
import functionActivation.SteppedFun;
import initializationOfWeight.ClassicInitWeight;
import initializationOfWeight.HalfWeight;
import initializationOfWeight.NormalWeight;
import initializationOfWeight.RootWeight;
import neuron.NeuronNetwork;

import java.io.Serializable;

public class FactoryNeuron implements                                         Serializable {
    public static NeuronNetwork FactoryNeuron (int input, int hidden, int output, double learningRate, TypeNeuron typeNeuron){
        switch (typeNeuron){
            case CLASSIC_SIGMOID_INIT: return new NeuronNetwork(input, hidden, output, learningRate, new ClassicInitWeight(), new SigmoidFun());
            case ROOT_SIGMOID_INIT: return new NeuronNetwork(input, hidden, output, learningRate, new RootWeight(), new SigmoidFun());
            case NORMAL_SIGMOID_INIT: return new NeuronNetwork(input, hidden, output, learningRate, new NormalWeight(), new SigmoidFun());
            case HALF_SIGMOID_INIT: return new NeuronNetwork(input, hidden, output, learningRate, new HalfWeight(), new SigmoidFun());
            case CLASSIC_STEPPED_INIT: return new NeuronNetwork(input, hidden, output, learningRate, new ClassicInitWeight(), new SteppedFun());
            case NORMAL_STEPPED_INIT: return new NeuronNetwork(input, hidden, output, learningRate, new NormalWeight(), new SteppedFun());
            case NORMAL_GIPERBOLIC_INIT: return new NeuronNetwork(input, hidden, output, learningRate, new NormalWeight(), new GiperbolicTangesFun());
            default: return null;

        }
    }
}
