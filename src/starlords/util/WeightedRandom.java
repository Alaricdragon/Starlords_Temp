package starlords.util;

public class WeightedRandom {
    /*TODO: this was suppose to create a controlable bell curve with an equadtion. but I gave up, becasue I only know basic math. it now brute forces the eq with while loops. its not terrible, so long as its not, lets say, runing every tick*/
    private double target;
    private double i;
    private double downChance;

    private double max;
    private double min;
    public WeightedRandom(double max, double min, double i, double target){
        this.i = 1/i;
        min = Math.min(min,max);
        max = Math.max(min,max);
        target = Math.min(target,max);
        target = Math.max(target,min);
        this.target = target;
        this.max = max-target;
        this.min = target-min;
        downChance = 0.5;
        if(max == target) downChance = 1;
        if(min == target) downChance = 0;
    }
    public double getRandom(){
        /*If I ever want to rewrite the EQ in full, all im doing is having an equation that grows expansively.
        * so basicly, something with a ^ in it. so like ran^i. or something.
        * wait, could I not just use less then 1 values of i to get the right resalt?
        * ran^0.1, would be what percentage below 10 anyways? arg...
        * I will look at this all next time.
        *
        * */


        double M = 1000;
        //this simplest solution, would be to mult all the numbers by say, 1000x, then once the EQ is over, devide them by 1000x.
        //int range = upRange;
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
            //while(a <= limit && Math.random() < i){//this i  is equal to 1 / inputed i
            while(a <= limit && Math.random() < i*M){//this i  is equal to 1 / inputed i
                a++;
            }
            if(a <= limit) break;//if a is within limits, dont modifi data.
            c++;
            a=0;
        }
        if(a > limit) a = 0;
        a*=multi;
        //return ((target*M)+a)/M;
        return target+a;
    }
}