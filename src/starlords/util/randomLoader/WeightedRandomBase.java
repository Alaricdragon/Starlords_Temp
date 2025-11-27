package starlords.util.randomLoader;

import java.util.Random;

public abstract class WeightedRandomBase {
    protected static final Random ran = new Random();
    public abstract int getRandomInt();
    public int getRandomInt(double target, double i, double max, double min){
        if (i == 0) return (int) ((ran.nextDouble()*(max-min))+min);
        int a = 0;
        while(a < 5) {
            double value = (ran.nextGaussian()*i)+target;
            value = Math.round(value);
            if (value <= max && value >= min) return (int) value;
            //if (true) return value;
            a++;
        }
        return (int)target;
    }
    public abstract double getRandom();
    public static double getRandom(double target, double i, double max, double min){
        if (i == 0) return (ran.nextDouble()*(max-min))+min;
        int a = 0;
        while(a < 5) {
            double value = (ran.nextGaussian()*i)+target;
            if (value <= max && value >= min) return value;
            //if (true) return value;
            a++;
        }
        return target;
    }
}
