package statistics;

import statistics.Distribution;

import java.util.Random;

public class NormalDistribution implements Distribution<Double> {

    protected double mean;
    protected double std;

    protected Random rng = new Random();

    public NormalDistribution() {
        this.mean = 0;
        this.std = 1;
    }

    public NormalDistribution(double mean, double std) {
        this.mean = mean;
        this.std = std;
    }

    public Double sample() {
        return this.mean + this.rng.nextGaussian() * this.std;
    }

    public Double[] sample(int size) {
        Double[] samples = new Double[size];
        for (int i = 0; i < size; i++) {
            samples[i] = this.sample();
        }
        return samples;
    }

    public double density(double x) {
        return Math.exp(-((Math.pow((x - this.mean) / this.std, 2)) / 2) - Math.log(this.std) + (Math.log(Math.PI) / 2));
    }

}
