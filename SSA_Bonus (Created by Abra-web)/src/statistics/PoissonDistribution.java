package statistics;

import statistics.Distribution;

import java.util.Random;

public class PoissonDistribution implements Distribution<Integer> {

    protected double mean;

    protected Random rng = new Random();

    public PoissonDistribution(double mean) {
        this.mean = mean;
    }

    public Integer sample() {
        double lambda = Math.exp(-this.mean);
        double lambda2 = 1;
        int n = 0;

        for (double random = 0; (double) n < 1000 * this.mean; ++n) {
            random = this.rng.nextDouble();
            lambda2 *= random;
            if (lambda2 < lambda) {
                return n;
            }
        }

        return n;
    }

    public Integer[] sample(int size) {
        Integer[] numbers = new Integer[size];

        for (int i = 0; i < size; i++) {
            numbers[i] = this.sample();
        }
        return numbers;
    }
}
