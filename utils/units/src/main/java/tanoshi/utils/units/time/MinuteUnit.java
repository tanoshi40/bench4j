package tanoshi.utils.units.time;

public class MinuteUnit extends TimeUnit {
    public static final MinuteUnit instance = new MinuteUnit();

    @Override
    public double toNano(double value) {
        return value * nanoToMin;
    }

    @Override
    public String shortName() {
        return "min";
    }

    @Override
    protected TimeUnit getNextUpperUnit() {
        return HourUnit.instance;
    }

    @Override
    protected TimeUnit getNextLowerUnit() {
        return SecondUnit.instance;
    }

    @Override
    protected double upperUnitCap() {
        return 60;
    }

    @Override
    protected double lowerUnitCap() {
        return 1;
    }

    @Override
    protected double fromNano(double nanoValue) {
        return NanosecondUnit.instance.toMin(nanoValue);
    }

    private MinuteUnit() {
    }
}
