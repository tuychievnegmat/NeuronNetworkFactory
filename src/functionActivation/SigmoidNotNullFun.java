package functionActivation;

public class SigmoidNotNullFun implements FunctionActivation {
    @Override
    public double getOutput(double input) {
        if(input < 0) return 0;
        else return 1/(1+(1/(Math.pow(2.71828, input))));
    }

    @Override
    public String toString() {
        return "SigmoidNotNullFun{}";
    }

    @Override
    public ActivationType getActivation() {
        return ActivationType.SIGMOID_NOT_NULL;
    }
}
