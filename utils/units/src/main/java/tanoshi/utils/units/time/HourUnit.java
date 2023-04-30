package tanoshi.utils.units.time;

public class HourUnit extends TimeUnit {
    public static final HourUnit instance = new HourUnit();

    @Override
    public double toNano(double value) {
        return value * nanoToHour;
    }

    @Override
    public String shortName() {
        return "hour";
    }

    @Override
    protected TimeUnit getNextUpperUnit() {
        return DayUnit.instance;
    }

    @Override
    protected TimeUnit getNextLowerUnit() {
        return MinuteUnit.instance;
    }

    @Override
    protected double upperUnitCap() {
        return 24;
    }

    @Override
    protected double lowerUnitCap() {
        return 1;
    }

    @Override
    protected double fromNano(double nanoValue) {
        return NanosecondUnit.instance.toHour(nanoValue);
    }

    private HourUnit() {
    }
}
