package functionActivation;

import java.io.Serializable;

/**
 * Created by k-terra on 10.07.2019.
 */

public interface FunctionActivation extends Serializable{
    double getOutput(double input);
    ActivationType getActivation();

}
