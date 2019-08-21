package neuron;

import functionActivation.ActivationType;
import functionActivation.FunctionActivation;

import java.io.Serializable;

public interface Neurons extends Serializable {
    public void train(double[]inputsList, double[] targetsList);
    public  double[] query(double[] inputsList);
    public ActivationType getActivation();

}
