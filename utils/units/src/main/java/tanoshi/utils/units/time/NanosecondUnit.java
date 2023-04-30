package tanoshi.utils.units.time;

public class NanosecondUnit extends TimeUnit {
    public static final NanosecondUnit instance = new NanosecondUnit();
    @Override
    public double toNano(double value) { return value; }

    @Override
    public String shortName() {
        return "ns";
    }

    @Override
    protected TimeUnit getNextUpperUnit() {
        return MicrosecondUnit.instance;
    }

    @Override
    protected TimeUnit getNextLowerUnit() {
        return null;
    }

    @Override
    protected double upperUnitCap() {
        return 1000;
    }

    @Override
    protected double lowerUnitCap() {
        return -1;
    }

    @Override
    protected double fromNano(double nanoValue) { return nanoValue; }

    private NanosecondUnit(){}
}

