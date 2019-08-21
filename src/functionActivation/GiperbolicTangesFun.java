package functionActivation;

public class GiperbolicTangesFun implements FunctionActivation{
    @Override
    public double getOutput(double input) {
        return (Math.pow(2.71828,(2 * input)) - 1)/(Math.pow(2.71828, (2 * input)) + 1);
    }

    @Override
    public String toString() {
        return "GiperbolicTangesFun{}";
    }

    @Override
    public ActivationType getActivation() {
        return ActivationType.GIPERBOLIC_TANGES_FUN;
    }
}
