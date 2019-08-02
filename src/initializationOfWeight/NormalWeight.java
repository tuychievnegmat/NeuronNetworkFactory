package initializationOfWeight;

import java.util.Random;

/**
 * Created by k-terra on 17.07.2019.
 * Обратите внимание, что если вам нужно несколько случайных значений, вы можете использовать rнесколько раз. Вы также можете предоставить конструктору конкретное начальное число Random r = new Random(1234)
 *
 * нормальное распределение. Ее параметрами являются центр распределения, стандартное отклонение и размер массива.
 * аналог numpy.random.normal(mean, variance)
 * в данном случае вместо mean 0.0
 * вместо variance = first
 *
 */
public class NormalWeight implements InitWeight{
    private static Random random = new Random();
    @Override
    public double getInitWeight(int first) {
        return 0.0 + random.nextGaussian() * (Math.pow(first, - 0.5));
    }
}
