package starlords.util.randomLoader;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Random;

public class WeightedRandom extends WeightedRandomBase{
    //todo: I still need to go into the settings and luna CSV and change the values.... arg...
    private double target;
    private double i;
    //private double downChance;

    private double max;
    private double min;
    //private static final double internalMulti = 1000;
    public WeightedRandom(){

    }
    public WeightedRandom(double max, double min, double i, double target){
        //this.i = 1/(i*internalMulti);
        min = Math.min(min,max);
        max = Math.max(min,max);
        target = Math.min(target,max);
        target = Math.max(target,min);
        this.target = target;
        this.max = max;
        this.min = min;
        this.i = i;
        //this.max = max-target;
        //this.min = target-min;
        //downChance = 0.5;
        //if(max == target) downChance = 1;
        //if(min == target) downChance = 0;
        //this.target*=internalMulti;
        //this.max*=internalMulti;
        //this.min*=internalMulti;
    }
    public static void attemptLog(){
        //this is for making sure this works. it just outputs an array to log.
        WeightedRandom a = new WeightedRandom(350,50,1,300);
        ArrayList<Integer> out = new ArrayList<>();
        for (int b = 0;b < 1000; b++){
            out.add((int) a.getRandomInt());
        }
        String o = "{";
        for (int b : out){
            o=o+b+",";
        }
        o=o+"}";

        Logger log = Logger.getLogger(WeightedRandom.class);
        log.info("HERE. IT IS HERE. GET IT HERE");
        log.info(o);
    }
    public int getRandomInt(){
        return getRandomInt(target,i,max,min);
    }
    public double getRandom(){
        return getRandom(target,i,max,min);
    }
}