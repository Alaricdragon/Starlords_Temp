package starlords.util.memoryUtils.Stats.types;

import java.util.Random;

public interface RandomDataReader <a>{
    boolean isCompressed();//none compressed data is not held as compressed data.
    a getValue(Random ran, int seed, Object linkedObject);
}
