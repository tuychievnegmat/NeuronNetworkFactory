package initializationOfWeight;

/**
 * Created by k-terra on 11.07.2019.
 * начальный вес не должен превышать 1/корень квадраный входного узла
 */
public class RootWeight implements InitWeight{
    @Override
    public double getInitWeight(int first) {
       return Math.random() * (1/ Math.sqrt(first));
    }

    @Override
    public String toString() {
        return "RootWeight{}";
    }
}
