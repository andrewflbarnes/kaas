package net.aflb.kaas.core.legacy.races.division.impl;

import net.aflb.kaas.core.legacy.races.division.DivisionConfiguration;
import net.aflb.kaas.kings.engine.GroupConfiguration;
import net.aflb.kaas.utils.ArrayUtils;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Set;

// TODO rename to DivisionConfiguration once all types added and DivisionConfiguration removed
public enum DivisionConfig {
    // 4, 5
    KNOCKOUT_FOUR(Type.KNOCKOUT, Set.of(4, 5), new String[][]{
            {"1A", "2A"}, {"3A", "4A"}
    }),
    KNOCKOUT_SIX(Type.KNOCKOUT, Set.of(6), new String[][]{
            {"1A", "2A"}, {"3A", "4A"}, {"5A", "6A"}
    }),
    KNOCKOUT_SEVEN(Type.KNOCKOUT, Set.of(7), new String[][]{
            {"11", "21"}, {"31", "41"}
    }),
    KNOCKOUT_EIGHT(Type.KNOCKOUT, Set.of(8), new String[][]{
            {"11", "21"}, {"31", "41"}, {"12", "22"}, {"32", "42"}
    }),
    // 9, 10
    KNOCKOUT_NINE(Type.KNOCKOUT, Set.of(9, 10), new String[][]{
            {"11", "12"}, {"21", "22"}, {"31", "32"}
    }),
    // 11, 12, 13, 15
    KNOCKOUT_ELEVEN(Type.KNOCKOUT, Set.of(11, 12, 13, 15), new String[][]{
            {"11", "12"}, {"21", "22"}, {"31", "32"}, {"41", "42"}
    }),
    KNOCKOUT_FOURTEEN(Type.KNOCKOUT, Set.of(14), new String[][]{
            {"11", "12"}, {"21", "22"}, {"31", "32"}, {"41", "42"},
            {"13", "14"}, {"23", "24"}, {"33", "34"}
    }),
    KNOCKOUT_SIXTEEN(Type.KNOCKOUT, Set.of(16), new String[][]{
            {"11", "12"}, {"21", "22"}, {"31", "32"}, {"41", "42"},
            {"13", "23"}, {"33", "43"}
    }),
    KNOCKOUT_SEVENTEEN(Type.KNOCKOUT, Set.of(17), new String[][]{
            {"11", "12"}, {"21", "22"}, {"31", "32"}, {"13", "14"},
            {"23", "24"}, {"33", "34"}, {"15", "16"}, {"25", "26"}
    }),
    // 18, 19
    KNOCKOUT_EIGHTEEN(Type.KNOCKOUT, Set.of(18, 19), new String[][]{
            {"11", "12"}, {"21", "22"}, {"31", "32"}, {"13", "14"},
            {"23", "24"}, {"33", "34"}, {"15", "16"}, {"25", "26"},
            {"35", "36"}
    }),
    KNOCKOUT_TWENTY(Type.KNOCKOUT, Set.of(20), new String[][]{
            {"11", "12"}, {"21", "22"}, {"31", "32"}, {"13", "14"},
            {"23", "24"}, {"33", "34"}, {"15", "16"}, {"25", "26"},
            {"35", "36"}, {"45", "46"}
    }),
    KNOCKOUT_TWENTY_ONE(Type.KNOCKOUT, Set.of(21), new String[][]{
            {"11", "12"}, {"21", "22"}, {"31", "32"}, {"41", "42"},
            {"13", "14"}, {"23", "24"}, {"33", "34"}, {"43", "44"},
            {"15", "16"}, {"25", "26"}
    }),
    // 22, 23, 24, 25, 26, 27 ,28, 29, 30, 31, 32
    KNOCKOUT_TWENTY_TWO(Type.KNOCKOUT, Set.of(22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32), new String[][]{
            {"11", "12"}, {"21", "22"}, {"31", "32"}, {"41", "42"},
            {"13", "14"}, {"23", "24"}, {"33", "34"}, {"43", "44"}
    });

    private static final String KNOCKOUT_LABEL_12 = "1st/2nd";
    private static final String KNOCKOUT_LABEL_34 = "3rd/4th";
    private static final String KNOCKOUT_LABEL_56 = "5th/6th";
    private static final String KNOCKOUT_LABEL_78 = "7th/8th";
    private static final String KNOCKOUT_LABEL_0910 = "9th/10th";
    private static final String KNOCKOUT_LABEL_1112 = "11th/12th";
    private static final String KNOCKOUT_LABEL_1314 = "13th/14th";
    private static final String KNOCKOUT_LABEL_1516 = "15th/16th";
    private static final String KNOCKOUT_LABEL_1718 = "17th/18th";
    private static final String KNOCKOUT_LABEL_1920 = "19th/20th";

    public enum Type {
        ONE,
        TWO,
        KNOCKOUT;
    }

    private final Type type;
    private final Set<Integer> teams;
    private final GroupConfiguration[] groupConfigs;
    private final String[] groupNames;
    private final String[][] transformationMapping;

    public static DivisionConfig of(final Type type, final int teamCount) {
        return Arrays.stream(DivisionConfig.values())
                .filter(v -> v.getTeams().contains(teamCount))
                .filter(v -> type.equals(v.getType()))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException(String.format(
                        "No %s could be found for type %s and teams %d",
                        DivisionConfig.class.getSimpleName(), type, teamCount)));
    }

    public static DivisionConfig knockout(final int teamCount) {
        return of(Type.KNOCKOUT, teamCount);
    }

    // TODO remove one fully implemented
    private static class DivisionConfigurationAdapter implements DivisionConfiguration {
        private final DivisionConfig config;

        public DivisionConfigurationAdapter(DivisionConfig config) {
            this.config = config;
        }

        @Override
        public String[][] getTransformationMapping() {
            return config.getTransformationMapping();
        }

        @Override
        public String[] getGroupNames() {
            return config.initGroupNames();
        }

        @Override
        public GroupConfiguration[] getGroupConfigs() {
            return config.initGroupConfigs();
        }

        @Override
        public void setTeams(int numTeams) {
            // nothing to do
        }
    }

    DivisionConfig(final Type type, final Set<Integer> teams, final String[][] transformationMapping) {
        this.type = type;
        this.teams = Set.copyOf(teams);
        final var groupCount = transformationMapping.length;
        this.transformationMapping = transformationMapping;
        this.groupConfigs = initGroupConfigs(groupCount);
        this.groupNames = initGroupNames(groupCount);
    }

    public Type getType() {
        return type;
    }

    public Set<Integer> getTeams() {
        return Set.copyOf(teams);
    }

    public String[][] getTransformationMapping() {
        return ArrayUtils.copy2(transformationMapping);
    }

    public String[] initGroupNames() {
        return Arrays.copyOf(groupNames, groupNames.length);
    }

    public GroupConfiguration[] initGroupConfigs() {
        return Arrays.copyOf(groupConfigs, groupConfigs.length);
    }

    public DivisionConfiguration asLegacy() {
        return new DivisionConfigurationAdapter(this);
    }

    private GroupConfiguration[] initGroupConfigs(final int length) {
        final var init = new GroupConfiguration[length];
        Arrays.fill(init, GroupConfiguration.KNOCKOUT);
        return init;
    }

    private String[] initGroupNames(final int length) {
        // Includes 5
        return switch (length) {
            case 2 -> new String[]{
                    KNOCKOUT_LABEL_12, KNOCKOUT_LABEL_34
            };
            case 3 -> new String[]{
                    KNOCKOUT_LABEL_12, KNOCKOUT_LABEL_34,
                    KNOCKOUT_LABEL_56
            };
            case 4 -> new String[]{
                    KNOCKOUT_LABEL_12, KNOCKOUT_LABEL_34,
                    KNOCKOUT_LABEL_56, KNOCKOUT_LABEL_78
            };
            case 6 -> new String[]{
                    KNOCKOUT_LABEL_12, KNOCKOUT_LABEL_34,
                    KNOCKOUT_LABEL_56, KNOCKOUT_LABEL_78,
                    KNOCKOUT_LABEL_0910, KNOCKOUT_LABEL_1112
            };
            case 7 -> new String[]{
                    KNOCKOUT_LABEL_12, KNOCKOUT_LABEL_34,
                    KNOCKOUT_LABEL_56, KNOCKOUT_LABEL_78,
                    KNOCKOUT_LABEL_0910, KNOCKOUT_LABEL_1112,
                    KNOCKOUT_LABEL_1314,
            };
            case 8 -> new String[]{
                    KNOCKOUT_LABEL_12, KNOCKOUT_LABEL_34,
                    KNOCKOUT_LABEL_56, KNOCKOUT_LABEL_78,
                    KNOCKOUT_LABEL_0910, KNOCKOUT_LABEL_1112,
                    KNOCKOUT_LABEL_1314, KNOCKOUT_LABEL_1516
            };
            case 9 -> new String[]{
                    KNOCKOUT_LABEL_12, KNOCKOUT_LABEL_34,
                    KNOCKOUT_LABEL_56, KNOCKOUT_LABEL_78,
                    KNOCKOUT_LABEL_0910, KNOCKOUT_LABEL_1112,
                    KNOCKOUT_LABEL_1314, KNOCKOUT_LABEL_1516,
                    KNOCKOUT_LABEL_1718
            };
            case 10 -> new String[]{
                    KNOCKOUT_LABEL_12, KNOCKOUT_LABEL_34,
                    KNOCKOUT_LABEL_56, KNOCKOUT_LABEL_78,
                    KNOCKOUT_LABEL_0910, KNOCKOUT_LABEL_1112,
                    KNOCKOUT_LABEL_1314, KNOCKOUT_LABEL_1516,
                    KNOCKOUT_LABEL_1718, KNOCKOUT_LABEL_1920
            };
            default -> throw new NoSuchElementException("No group names exist for the required number of " +
                    "groups (" + length + ")");
        };
    }
}
