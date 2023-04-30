package tanoshi.utils.units.time;

public class DayUnit extends TimeUnit {
    public static final DayUnit instance = new DayUnit();

    @Override
    public double toNano(double value) {
        return value * nanoToDay;
    }

    @Override
    public String shortName() {
        return "day";
    }

    @Override
    protected TimeUnit getNextUpperUnit() {
        return null;
    }

    @Override
    protected TimeUnit getNextLowerUnit() {
        return HourUnit.instance;
    }

    @Override
    protected double upperUnitCap() {
        return - 1;
    }

    @Override
    protected double lowerUnitCap() {
        return 1;
    }

    @Override
    protected double fromNano(double nanoValue) {
        return NanosecondUnit.instance.toDay(nanoValue);
    }

    private DayUnit() {
    }
}
