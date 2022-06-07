package statistics;

import statistics.*;

public class test {

    public static void main(String[] args) {
        LeftTruncatedNormalDistribution normal = new LeftTruncatedNormalDistribution(246, 66, 1);
        System.out.println(normal.sample());

        PoissonDistribution poisson = new PoissonDistribution(300);
        System.out.println(poisson.sample());
    }
}

