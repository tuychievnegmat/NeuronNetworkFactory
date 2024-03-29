package functionActivation;

/**
 * Created by k-terra on 10.07.2019.
 */
public class SteppedFun implements FunctionActivation {
    @Override
    public double getOutput(double input) {
        return input > 0.5? 1: 0;
    }

    @Override
    public String toString() {
        return "SteppedFun{}";
    }

    @Override
    public ActivationType getActivation() {
        return ActivationType.STEPPED_FUN;
    }
}
