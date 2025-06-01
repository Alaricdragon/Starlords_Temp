package starlords.util.weights;


import org.apache.log4j.Logger;

public class IncomeWeights {
    public double lordFiefIncomeMulti=1;
    public double lordCombatIncomeMulti=1;
    public double lordTradeIncomeMulti=1;
    public double lordCommissionedIncomeMulti=1;
    public void log(Logger log){
        log.info("  fiefIncomeMulti: "+this.lordFiefIncomeMulti);
        log.info("  combatIncomeMulti: "+this.lordCombatIncomeMulti);
        log.info("  tradeIncomeMulti: "+this.lordTradeIncomeMulti);
        log.info("  commissionIncomeMulti: "+this.lordCommissionedIncomeMulti);
    }
}
