package functionActivation;

/**
 * Created by k-terra on 10.07.2019.
 */
public class SteppedFun implements FunctionActivation {
    @Override
    public double getOutput(double input) {
        return input > 5? 1: 0;
    }
}
