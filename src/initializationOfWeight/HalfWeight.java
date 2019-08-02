package initializationOfWeight;

/**
 * Created by k-terra on 11.07.2019.
 * от -0.5 до 0.5
 */

public class HalfWeight implements InitWeight {
    @Override
    public double getInitWeight(int first) {
        return Math.random() - 0.5;
    }
}
