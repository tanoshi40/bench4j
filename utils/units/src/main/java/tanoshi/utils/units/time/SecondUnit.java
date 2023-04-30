package tanoshi.utils.units.time;

public class SecondUnit extends TimeUnit {
    public static final SecondUnit instance = new SecondUnit();

    @Override
    public double toNano(double value) {
        return value * nanoToSec;
    }

    @Override
    public String shortName() {
        return "sec";
    }

    @Override
    protected TimeUnit getNextUpperUnit() {
        return MinuteUnit.instance;
    }

    @Override
    protected TimeUnit getNextLowerUnit() {
        return MillisecondUnit.instance;
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
        return NanosecondUnit.instance.toSec(nanoValue);
    }

    private SecondUnit() {
    }
}
