package initializationOfWeight;

/**
 * Created by k-terra on 11.07.2019.
 */
public class ClassicInitWeight implements InitWeight {
    @Override
    public double getInitWeight(int first) {
        return Math.random();
    }
}
