package functionActivation;

import java.lang.annotation.Annotation;

/**
 * Created by k-terra on 10.07.2019.
 */
public class SigmoidFun implements FunctionActivation {
    @Override
    public double getOutput(double input) {
        return 1/(1+(1/(Math.pow(2.71828, input))));
    }

    @Override
    public String toString() {
        return "SigmoidFun{}";
    }
}
