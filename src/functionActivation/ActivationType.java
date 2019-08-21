package functionActivation;

/**
 * Created by k-terra on 10.07.2019.
 */
public enum ActivationType {
    STEPPED_FUN(0.5, 0),
    SIGMOID_FUN(0.99, 0.01),
    GIPERBOLIC_TANGES_FUN(0.73, -0.01),
    SIGMOID_NOT_NULL(0.99, 0.01);


    private final double CONST_TARGET;
    private final double CONST_WRONG;

    ActivationType() {
        //стандарт для сигмоида
        CONST_TARGET = 0.01;
        CONST_WRONG = 0.99;
    }

    ActivationType(double CONST_TARGET, double CONST_WRONG) {
        this.CONST_TARGET = CONST_TARGET;
        this.CONST_WRONG = CONST_WRONG;
    }

    public double getCONST_TARGET() {
        return CONST_TARGET;
    }

    public double getCONST_WRONG() {
        return CONST_WRONG;
    }
}
