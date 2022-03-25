package net.aflb.kaas.kings;

import net.aflb.kaas.core.model.Club;
import net.aflb.kaas.core.model.Division;
import net.aflb.kaas.core.model.League;
import net.aflb.kaas.core.model.Registry;
import net.aflb.kaas.core.model.Team;
import net.aflb.kaas.core.model.competing.Round;
import net.aflb.kaas.kings.engine.SetOneMatchGenerator;

import java.util.Arrays;
import java.util.Map;

public class TestEntities {

    public static final Division MIXED = Division.of("Mixed", 0);
    public static final Division LADIES = Division.of("Ladies", 1);
    public static final Division BOARD = Division.of("Board", 2);

    public static final League NORTHERN = League.of("Northern");

    public static final Club DUSSC = Club.of("Durham");
    public static final Team DUSSC1 = Team.of("Durham 1");
    public static final Team DUSSC2 = Team.of("Durham 2");
    public static final Team DUSSC3 = Team.of("Durham 3");
    public static final Team DUSSC4 = Team.of("Durham 4");

    public static final Club NUSSC = Club.of("Newcastle");
    public static final Team NUSSC1 = Team.of("Newcastle 1");
    public static final Team NUSSC2 = Team.of("Newcastle 2");
    public static final Team NUSSC3 = Team.of("Newcastle 3");
    public static final Team NUSSC4 = Team.of("Newcastle 4");

    public static final Club SHU = Club.of("Sheffield Hallam");
    public static final Team SHU1 = Team.of("Sheffield Hallam 1");
    public static final Team SHU2 = Team.of("Sheffield Hallam 2");
    public static final Team SHU3 = Team.of("Sheffield Hallam 3");
    public static final Team SHU4 = Team.of("Sheffield Hallam 4");

    public static final Club SKUM = Club.of("Manchester");
    public static final Team SKUM1 = Team.of("Manchester 1");
    public static final Team SKUM2 = Team.of("Manchester 2");
    public static final Team SKUM3 = Team.of("Manchester 3");
    public static final Team SKUM4 = Team.of("Manchester 4");

    public static final Club LEEDS = Club.of("Leeds");
    public static final Team LEEDS1 = Team.of("Leeds 1");
    public static final Team LEEDS2 = Team.of("Leeds 2");
    public static final Team LEEDS3 = Team.of("Leeds 3");
    public static final Team LEEDS4 = Team.of("Leeds 4");

    public static final Club LIVERPOOL = Club.of("Liverpool");
    public static final Team LIVERPOOL1 = Team.of("Liverpool 1");
    public static final Team LIVERPOOL2 = Team.of("Liverpool 2");
    public static final Team LIVERPOOL3 = Team.of("Liverpool 3");
    public static final Team LIVERPOOL4 = Team.of("Liverpool 4");

    public static final Registry REGISTRY = new Registry();

    public static final Round ROUND1 = Round.of(true, "Test round", Map.of(
            MIXED, Arrays.asList(DUSSC1, NUSSC1, DUSSC2, NUSSC2, DUSSC3, NUSSC3, DUSSC4, NUSSC4),
            LADIES, Arrays.asList(SHU1, SKUM1, SHU2, SKUM2, SHU3, SKUM3, SHU4, SKUM4),
            BOARD, Arrays.asList(LEEDS1, LIVERPOOL1, LEEDS2, LIVERPOOL2, LEEDS3, LIVERPOOL3, LEEDS4, LIVERPOOL4)
    ), NORTHERN);

    static {
        REGISTRY.registerLeague(NORTHERN);

        REGISTRY.registerDivision(LADIES);
        REGISTRY.registerDivision(MIXED);
        REGISTRY.registerDivision(BOARD);

        REGISTRY.registerClub(DUSSC, NORTHERN);
        REGISTRY.registerTeam(DUSSC1, DUSSC, MIXED);
        REGISTRY.registerTeam(DUSSC2, DUSSC, MIXED);
        REGISTRY.registerTeam(DUSSC3, DUSSC, MIXED);
        REGISTRY.registerTeam(DUSSC4, DUSSC, MIXED);

        REGISTRY.registerClub(NUSSC, NORTHERN);
        REGISTRY.registerTeam(NUSSC1, NUSSC, MIXED);
        REGISTRY.registerTeam(NUSSC2, NUSSC, MIXED);
        REGISTRY.registerTeam(NUSSC3, NUSSC, MIXED);
        REGISTRY.registerTeam(NUSSC4, NUSSC, MIXED);

        REGISTRY.registerClub(SHU, NORTHERN);
        REGISTRY.registerTeam(SHU1, SHU, LADIES);
        REGISTRY.registerTeam(SHU2, SHU, LADIES);
        REGISTRY.registerTeam(SHU3, SHU, LADIES);
        REGISTRY.registerTeam(SHU4, SHU, LADIES);

        REGISTRY.registerClub(SKUM, NORTHERN);
        REGISTRY.registerTeam(SKUM1, SKUM, LADIES);
        REGISTRY.registerTeam(SKUM2, SKUM, LADIES);
        REGISTRY.registerTeam(SKUM3, SKUM, LADIES);
        REGISTRY.registerTeam(SKUM4, SKUM, LADIES);

        REGISTRY.registerClub(LEEDS, NORTHERN);
        REGISTRY.registerTeam(LEEDS1, LEEDS, LADIES);
        REGISTRY.registerTeam(LEEDS2, LEEDS, LADIES);
        REGISTRY.registerTeam(LEEDS3, LEEDS, LADIES);
        REGISTRY.registerTeam(LEEDS4, LEEDS, LADIES);

        REGISTRY.registerClub(LIVERPOOL, NORTHERN);
        REGISTRY.registerTeam(LIVERPOOL1, LIVERPOOL, LADIES);
        REGISTRY.registerTeam(LIVERPOOL2, LIVERPOOL, LADIES);
        REGISTRY.registerTeam(LIVERPOOL3, LIVERPOOL, LADIES);
        REGISTRY.registerTeam(LIVERPOOL4, LIVERPOOL, LADIES);

        new SetOneMatchGenerator().generate(ROUND1);
    }

    public TestEntities() {
    }
}
