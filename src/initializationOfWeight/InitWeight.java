package initializationOfWeight;

import java.io.Serializable;

/**
 * Created by k-terra on 11.07.2019.
 */
@FunctionalInterface
public interface InitWeight extends Serializable {
    double getInitWeight(int first);
}
