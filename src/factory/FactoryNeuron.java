package factory;

import functionActivation.GiperbolicTangesFun;
import functionActivation.SigmoidFun;
import functionActivation.SigmoidNotNullFun;
import functionActivation.SteppedFun;
import initializationOfWeight.ClassicInitWeight;
import initializationOfWeight.HalfWeight;
import initializationOfWeight.NormalWeight;
import initializationOfWeight.RootWeight;
import neuron.NeuronNetwork;

import java.io.Serializable;

public class FactoryNeuron implements                                         Serializable {
    public static NeuronNetwork FactoryNeuron (int input, int hidden, int output, double learningRate, TypeNeuron typeNeuron){
       int deep = 1;
       return init(input, hidden, output, learningRate, typeNeuron, deep);
    }
    public static NeuronNetwork FactoryNeuron (int input, int hidden, int output, double learningRate, TypeNeuron typeNeuron, int deep){
        return init(input, hidden, output, learningRate, typeNeuron, deep);
    }

    private static NeuronNetwork init(int input, int hidden, int output, double learningRate, TypeNeuron typeNeuron, int deep){
        switch (typeNeuron){
            case CLASSIC_SIGMOID_INIT: return new NeuronNetwork(input, hidden, output, learningRate,deep, new ClassicInitWeight(), new SigmoidFun());
            case ROOT_SIGMOID_INIT: return new NeuronNetwork(input, hidden, output, learningRate,deep, new RootWeight(), new SigmoidFun());
            case NORMAL_SIGMOID_INIT: return new NeuronNetwork(input, hidden, output, learningRate,deep, new NormalWeight(), new SigmoidFun());
            case HALF_SIGMOID_INIT: return new NeuronNetwork(input, hidden, output, learningRate,deep, new HalfWeight(), new SigmoidFun());
            case CLASSIC_STEPPED_INIT: return new NeuronNetwork(input, hidden, output, learningRate, deep, new ClassicInitWeight(), new SteppedFun());
            case NORMAL_STEPPED_INIT: return new NeuronNetwork(input, hidden, output, learningRate,deep, new NormalWeight(), new SteppedFun());
            case NORMAL_GIPERBOLIC_INIT: return new NeuronNetwork(input, hidden, output, learningRate,deep, new NormalWeight(), new GiperbolicTangesFun());

            //test
            case NORMAL_TEST_NOT_NULL_INIT: return new NeuronNetwork(input, hidden, output, learningRate,deep, new NormalWeight(), new SigmoidNotNullFun());
            default: return null;

        }
    }
}
