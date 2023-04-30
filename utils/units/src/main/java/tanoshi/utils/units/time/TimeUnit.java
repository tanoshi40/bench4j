package tanoshi.utils.units.time;

public abstract class TimeUnit {

    public static final double nanoToMicro = 1_000;
    public static final double nanoToMilli = nanoToMicro * 1_000;
    public static final double nanoToSec = nanoToMilli * 1_000;
    public static final double nanoToMin = nanoToSec * 60;
    public static final double nanoToHour = nanoToMin * 60;
    public static final double nanoToDay = nanoToHour * 24;

    public abstract String shortName();

    public abstract double toNano(double value);
    protected abstract TimeUnit getNextUpperUnit();
    protected abstract TimeUnit getNextLowerUnit();
    protected abstract double upperUnitCap();
    protected abstract double lowerUnitCap();
    protected abstract double fromNano(double nanoValue);

    public double toMicro(double value) {
        return toNano(value) / nanoToMicro;
    }

    public double toMilli(double value) {
        return toNano(value) / nanoToMilli;
    }

    public double toSec(double value) {
        return toNano(value) / nanoToSec;
    }

    public double toMin(double value) {
        return toNano(value) / nanoToMin;
    }

    public double toHour(double value) {
        return toNano(value) / nanoToHour;
    }

    public double toDay(double value) {
        return toNano(value) / nanoToDay;
    }

    public String toString(double value) {
        return String.format("%.2f %s", value, shortName());
    }

    public String toHumanString(double value) {
        if (Math.abs(value) > upperUnitCap()) {
            TimeUnit nextUpperUnit = getNextUpperUnit();
            return nextUpperUnit == null ?
                    toString(value) :
                    nextUpperUnit.toHumanString(nextUpperUnit.fromNano(toNano(value)));

        }

        if (Math.abs(value) < lowerUnitCap()) {
            TimeUnit nextLowerUnit = getNextLowerUnit();
            return nextLowerUnit == null ?
                    toString(value) :
                    nextLowerUnit.toHumanString(nextLowerUnit.fromNano(toNano(value)));

        }

        return toString(value);
    }
}
