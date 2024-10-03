import java.util.ArrayList;
import java.util.List;

class Pokeball {
    private static List<Pokeball> allPokeballs;

    static {
        initializePokeballs();
    }

    private String type;
    private double catchRate;

    public Pokeball(String type, double catchRate) {
        this.type = type;
        this.catchRate = catchRate;
    }

    public String getType() {
        return type;
    }

    public double getCatchRate() {
        return catchRate;
    }

    public static List<Pokeball> getAllPokeballs() {
        return allPokeballs;
    }

    private static void initializePokeballs() {
        allPokeballs = new ArrayList<>();
        allPokeballs.add(new Pokeball1());
        allPokeballs.add(new GreatBall());
        allPokeballs.add(new UltraBall());
        allPokeballs.add(new MasterBall());
    }
}

class Pokeball1 extends Pokeball {
    public Pokeball1() {
        super("pokeball", 0.10);
    }
}
class GreatBall extends Pokeball {
    public GreatBall() {
        super("greatball", 0.25);
    }
}

class UltraBall extends Pokeball {
    public UltraBall() {
        super("ultraball", 0.50);
    }
}

class MasterBall extends Pokeball {
    public MasterBall() {
        super("masterball", 1.0);
    }
}
