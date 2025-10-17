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
        //int range = upRange;
        int multi = 1;
        double limit = max;
        if (Math.random() >= downChance){
            //range = downRange;
            multi = -1;
            limit = min;
        }
        int a = 0;
        //double chance = (1/i);//((i-1) / i);//2 I want 50%. 3 I want 66%. 4 I want 75%.
        int c = 0;
        while(c < 5){
            while(a <= limit && Math.random() < i){//this i  is equal to 1 / inputed i
                a++;
            }
            if(a <= limit) break;
            c++;
            a=0;
        }
        if(a > limit) a = 0;
        a*=multi;
        return target+a;
    }
}