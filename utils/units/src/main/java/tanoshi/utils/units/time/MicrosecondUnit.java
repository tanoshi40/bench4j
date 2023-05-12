package tanoshi.utils.units.time;

public class MicrosecondUnit extends TimeUnit {
    public static final MicrosecondUnit instance = new MicrosecondUnit();

    @Override
    public double toNano(double value) {
        return value * nanoToMicro;
    }

    @Override
    public String shortName() {
        return "mc";
    }

    @Override
    protected TimeUnit getNextUpperUnit() {
        return MillisecondUnit.instance;
    }

    @Override
    protected TimeUnit getNextLowerUnit() {
        return NanosecondUnit.instance;
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
        return NanosecondUnit.instance.toMicro(nanoValue);
    }

    private MicrosecondUnit() {
    }
}
