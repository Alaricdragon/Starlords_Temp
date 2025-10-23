package starlords.util;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Random;

public class WeightedRandom {
    //todo: I still need to go into the settings and luna CSV and change the values.... arg...
    private double target;
    private double i;
    //private double downChance;

    private double max;
    private double min;
    //private static final double internalMulti = 1000;
    private static final Random ran = new Random();
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
    public double getRandom(){
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
    /*If I ever want to rewrite the EQ in full, all im doing is having an equation that grows expansively.
     * so basicly, something with a ^ in it. so like ran^i. or something.
     * wait, could I not just use less then 1 values of i to get the right resalt?
     * ran^0.1, would be what percentage below 10 anyways? arg...
     * I will look at this all next time.
     *
     * */


    /*so I am going to change this into a bell curve:
     * bell curves propertys:
     * 1) 'standard deviation':
     *   symbol: U (with a l on its left side)
     *   'how to mesure a bell curve from the mean out to the edge of the curve'
     *   imperical rule tells how what percentage of the data fulls within a certen ''range' of standerd deveations from the mean.
     *   example: 1 deviation from the mean (in both directions) is equal to 68% of all data. (or 34% for one direcion)
     *   example: 2 deviation form the mean (in both directions) is equal to 95% of all data. (or 47.5% for one direction) (or 13.5% between 1 and 2 in a given direction)
     *   example: 3 deviation from the mean (in both directions) is equal to 99.7% of all data. (or 49.85% for one direction) (or 2.35% between 2 and 3 in a given direction)
     *   I could use this: as a fraction of the min / max, depending on what direction I am looking.
     * 2) 'mean'. this is just the 'target value', in the current implementation of weighted random.
     *   symbol: o (- on its top right)
     *   -is also the 'mode' and the 'medium', do to the way the math works out.
     *
     * -) standardisation:
     *   normaly, when using this EQ one would set the 'standard deviation' to 1, and the 'mean' to 0. this is how people like to do it to make themselfs less frusterated.
     *   this requies:
     *       1) z-score:
     *           z = (x-U)/o (were x is some data point)
     *           tells you how far a given data point is away from the mean.
     *           this is what I am looking for.
     *           this tells you, under a 'standard' deviation how many 'U' away from 'o' x is.
     *
     *
     *
     *   conclusions:
     *       what am I even trying to do here?
     *       I am trying to turn random into a z-score.
     *       change I to be U. (as they solve the same role, in effect). (with 0 being true random)
     *           -keep in mind, 3 U is equal to 99.7% of all probability. 1 U is equal to 68% of all probability. so just keep this in mind.
     *       min and max will be the 'hard' min and man. they will not determine probability's.
     *           -this is different then my last approach. but it is useful in this way.
     *       x-score: the issue with this right now, is that it takes some point of data, and determines its probability. z is probability. (odds of item).
     *              cant I just plug in random as X, to get my zscore, then solve backwords for my true value?
     *               so if I had a o of 50, I could random 50*6, and get a true random z-score, because the data would not be distrubuted.
     *               so I could take random and set it to -5 - + 5 (for verity) but....
     *               this is also an equal distrubution. not right.
     *
     *
     *              I need to inverse this EQ. I need to take z, and solve for x.
     *                (x*U)/o = z
     *                (x*U) = o*z
     *                (x) = (o*z)/U
     *
     *                ... does this solve into math.random though?????
     *                so what z would be saying is get me the thing at X probability. a number between -3 and 3. effectivly. but like... that makes no sense. I need to distrubute this. arg.
     *
     *
     *
     * */
    /*
    @Deprecated
    public double getRandom_old(){



        //double M = 1000;
        //this simplest solution, would be to mult all the numbers by say, 1000x, then once the EQ is over, devide them by 1000x.
        //int range = upRange;
        new Random().nextGaussian();
        int multi = 1;
        double limit = max;
        if (Math.random() >= downChance){
            //range = downRange;
            multi = -1;
            limit = min;
        }
        //limit *= M;
        int a = 0;

        //double chance = (1/i);//((i-1) / i);//2 I want 50%. 3 I want 66%. 4 I want 75%.
        int c = 0;
        while(c < 5){
            while(a <= limit && Math.random() < i){//this i  is equal to 1 / inputed i
            //while(a <= limit && Math.random() < i*M){//this i  is equal to 1 / inputed i
                a++;
            }
            if(a <= limit) break;//if a is within limits, dont modifi data.
            c++;
            a=0;
        }
        if(a > limit) a = 0;
        a*=multi;
        //return ((target*M)+a)/M;
        return (target+a)/internalMulti;
    }*/
}