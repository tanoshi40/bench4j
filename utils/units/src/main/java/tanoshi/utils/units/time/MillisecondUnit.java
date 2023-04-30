package tanoshi.utils.units.time;

public class MillisecondUnit extends TimeUnit {
    public static final MillisecondUnit instance = new MillisecondUnit();

    @Override
    public double toNano(double value) {
        return value * nanoToMilli;
    }

    @Override
    public String shortName() {
        return "ms";
    }

    @Override
    protected TimeUnit getNextUpperUnit() {
        return SecondUnit.instance;
    }

    @Override
    protected TimeUnit getNextLowerUnit() {
        return MicrosecondUnit.instance;
    }

    @Override
    protected double upperUnitCap() {
        return 1000;
    }

    @Override
    protected double lowerUnitCap() {
        return 1;
    }

    @Override
    protected double fromNano(double nanoValue) {
        return NanosecondUnit.instance.toMilli(nanoValue);
    }

    private MillisecondUnit() {
    }
}
