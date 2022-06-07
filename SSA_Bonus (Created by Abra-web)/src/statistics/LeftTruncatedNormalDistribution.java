package statistics;

public class LeftTruncatedNormalDistribution extends NormalDistribution {

    protected int leftTruncatedValue;

    public LeftTruncatedNormalDistribution(double mean, double sd, int leftTruncatedValue) {
        super(mean, sd);
        this.leftTruncatedValue = leftTruncatedValue;
    }

    @Override
    public Double sample() {
        double sample = super.sample();
        return sample < this.leftTruncatedValue ? this.sample() : sample;
    }

    @Override
    public double density(double x) {
        return x < this.leftTruncatedValue ? 0 : super.density(x);
    }
}
