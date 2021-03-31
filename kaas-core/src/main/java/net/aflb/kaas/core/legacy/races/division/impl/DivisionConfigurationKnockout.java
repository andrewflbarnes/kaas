/**
 * Kings Ski Club Race Organiser
 */
package net.aflb.kaas.core.legacy.races.division.impl;

import net.aflb.kaas.core.legacy.races.division.DivisionConfiguration;
import net.aflb.kaas.kings.engine.GroupConfiguration;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Set;

public class DivisionConfigurationKnockout implements DivisionConfiguration {
    private static final String NAME_12 = "1st/2nd";
    private static final String NAME_34 = "3rd/4th";
    private static final String NAME_56 = "5th/6th";
    private static final String NAME_78 = "7th/8th";
    private static final String NAME_0910 = "9th/10th";
    private static final String NAME_1112 = "11th/12th";
    private static final String NAME_1314 = "13th/14th";
    private static final String NAME_1516 = "15th/16th";
    private static final String NAME_1718 = "17th/18th";
    private static final String NAME_1920 = "19th/20th";

    public enum Type {
        ONE,
        TWO,
        KNOCKOUT;
    }

    public enum Configuration {
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

        private Type type;
        private Set<Integer> teams;
        private GroupConfiguration[] groupConfigs;
        private String[] groupNames;
        private String[][] transformationMapping;

        Configuration(final Type type, final Set<Integer> teams, final String[][] transformationMapping) {
            this.type = type;
            this.teams = Set.copyOf(teams);
            final var groupCount = transformationMapping.length;
            this.transformationMapping = transformationMapping;
            this.groupConfigs = getGroupConfigs(groupCount);
            this.groupNames = getGroupNames(groupCount);
        }

        public Type getType() {
            return type;
        }

        public Set<Integer> getTeams() {
            return Set.copyOf(teams);
        }

        public String[][] getTransformationMapping() {
            return transformationMapping;
        }

        public String[] getGroupNames() {
            return groupNames;
        }

        public GroupConfiguration[] getGroupConfigs() {
            return groupConfigs;
        }

        private GroupConfiguration[] getGroupConfigs(final int length) {
            final var groupConfigs = new GroupConfiguration[length];
            Arrays.fill(groupConfigs, GroupConfiguration.KNOCKOUT);
            return groupConfigs;
        }

        private String[] getGroupNames(final int length) {
            // Includes 5
            return switch (length) {
                case 2 -> new String[]{
                        NAME_12, NAME_34
                };
                case 3 -> new String[]{
                        NAME_12, NAME_34,
                        NAME_56
                };
                case 4 -> new String[]{
                        NAME_12, NAME_34,
                        NAME_56, NAME_78
                };
                case 6 -> new String[]{
                        NAME_12, NAME_34,
                        NAME_56, NAME_78,
                        NAME_0910, NAME_1112
                };
                case 7 -> new String[]{
                        NAME_12, NAME_34,
                        NAME_56, NAME_78,
                        NAME_0910, NAME_1112,
                        NAME_1314,
                };
                case 8 -> new String[]{
                        NAME_12, NAME_34,
                        NAME_56, NAME_78,
                        NAME_0910, NAME_1112,
                        NAME_1314, NAME_1516
                };
                case 9 -> new String[]{
                        NAME_12, NAME_34,
                        NAME_56, NAME_78,
                        NAME_0910, NAME_1112,
                        NAME_1314, NAME_1516,
                        NAME_1718
                };
                case 10 -> new String[]{
                        NAME_12, NAME_34,
                        NAME_56, NAME_78,
                        NAME_0910, NAME_1112,
                        NAME_1314, NAME_1516,
                        NAME_1718, NAME_1920
                };
                default -> throw new NoSuchElementException("No group names exist for the required number of " +
                        "groups (" + length + ")");
            };
        }

        public static Configuration of(final Type type, final int teamCount) {
            return Arrays.stream(Configuration.values())
                    .filter(v -> v.teams.contains(teamCount))
                    .filter(v -> type.equals(v.type))
                    .findAny()
                    .orElseThrow(() -> new NoSuchElementException(String.format(
                            "No %s could be found for type %s and teams %d",
                            Configuration.class.getSimpleName(), type, teamCount)));
        }
    }

    private GroupConfiguration[] groupConfigs;
    private String[] groupNames;
    private String[][] transformationMapping;

    public DivisionConfigurationKnockout(final int numTeams) throws InvalidNumberOfTeamsException {
        setTeams(numTeams);
    }

    @Override
    public String[][] getTransformationMapping() {
        return this.transformationMapping;
    }

    @Override
    public String[] getGroupNames() {
        return this.groupNames;
    }

    @Override
    public GroupConfiguration[] getGroupConfigs() {
        return this.groupConfigs;
    }

    @Override
    public void setTeams(int numTeams) throws InvalidNumberOfTeamsException {

        if (numTeams < 4 || numTeams > 32) {
            throw new InvalidNumberOfTeamsException("Too many/few teams (" + numTeams + ")");
        }

        switch (numTeams) {
        case 4:
        case 5:
            this.groupConfigs = new GroupConfiguration[] {
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT
            };
            this.transformationMapping = new String[][] {
                    {"1A", "2A"}, {"3A", "4A"}
            };
            break;
        case 6:
            this.groupConfigs = new GroupConfiguration[] {
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT,
                    GroupConfiguration.KNOCKOUT
            };
            this.transformationMapping = new String[][] {
                    {"1A", "2A"}, {"3A", "4A"}, {"5A", "6A"}
            };
            break;
        case 7:
            this.groupConfigs = new GroupConfiguration[] {
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT
            };
            this.transformationMapping = new String[][] {
                    {"11", "21"}, {"31", "41"}
            };
            break;
        case 8:
            this.groupConfigs = new GroupConfiguration[] {
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT,
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT
            };
            this.transformationMapping = new String[][] {
                    {"11", "21"}, {"31", "41"},
                    {"12", "22"}, {"32", "42"}
            };
            break;
        case 9:
        case 10:
            this.groupConfigs = new GroupConfiguration[] {
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT,
                    GroupConfiguration.KNOCKOUT
            };
            this.transformationMapping = new String[][] {
                    {"11", "12"}, {"21", "22"},
                    {"31", "32"}
            };
            break;
        case 11:
        case 12:
        case 13:
        case 15:
            this.groupConfigs = new GroupConfiguration[] {
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT,
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT
            };
            this.transformationMapping = new String[][] {
                    {"11", "12"}, {"21", "22"},
                    {"31", "32"}, {"41", "42"}
            };
            break;
        case 14:
            this.groupConfigs = new GroupConfiguration[] {
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT,
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT,
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT,
                    GroupConfiguration.KNOCKOUT
            };
            this.transformationMapping = new String[][] {
                    {"11", "12"}, {"21", "22"},
                    {"31", "32"}, {"41", "42"},
                    {"13", "14"}, {"23", "24"},
                    {"33", "34"}
            };
            break;
        case 16:
            this.groupConfigs = new GroupConfiguration[] {
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT,
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT,
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT
            };
            this.transformationMapping = new String[][] {
                    {"11", "12"}, {"21", "22"},
                    {"31", "32"}, {"41", "42"},
                    {"13", "23"}, {"33", "43"},
            };
            break;
        case 17:
            this.groupConfigs = new GroupConfiguration[] {
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT,
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT,
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT,
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT
            };
            this.transformationMapping = new String[][] {
                    {"11", "12"}, {"21", "22"},
                    {"31", "32"}, {"13", "14"},
                    {"23", "24"}, {"33", "34"},
                    {"15", "16"}, {"25", "26"}
            };
            break;
        case 18:
        case 19:
            this.groupConfigs = new GroupConfiguration[] {
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT,
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT,
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT,
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT,
                    GroupConfiguration.KNOCKOUT
            };
            this.transformationMapping = new String[][] {
                    {"11", "12"}, {"21", "22"},
                    {"31", "32"}, {"13", "14"},
                    {"23", "24"}, {"33", "34"},
                    {"15", "16"}, {"25", "26"},
                    {"35", "36"}
            };
            break;
        case 20:
            this.groupConfigs = new GroupConfiguration[] {
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT,
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT,
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT,
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT,
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT
            };
            this.transformationMapping = new String[][] {
                    {"11", "12"}, {"21", "22"},
                    {"31", "32"}, {"13", "14"},
                    {"23", "24"}, {"33", "34"},
                    {"15", "16"}, {"25", "26"},
                    {"35", "36"}, {"45", "46"}
            };
            break;
        case 21:
            this.groupConfigs = new GroupConfiguration[] {
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT,
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT,
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT,
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT,
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT
            };
            this.transformationMapping = new String[][] {
                    {"11", "12"}, {"21", "22"},
                    {"31", "32"}, {"41", "42"},
                    {"13", "14"}, {"23", "24"},
                    {"33", "34"}, {"43", "44"},
                    {"15", "16"}, {"25", "26"}
            };
            break;
        case 22:
        case 23:
        case 24:
        case 25:
        case 26:
        case 27:
        case 28:
        case 29:
        case 30:
        case 31:
        case 32:
            this.groupConfigs = new GroupConfiguration[] {
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT,
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT,
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT,
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT,
            };
            this.transformationMapping = new String[][] {
                    {"11", "12"}, {"21", "22"},
                    {"31", "32"}, {"41", "42"},
                    {"13", "14"}, {"23", "24"},
                    {"33", "34"}, {"43", "44"}
            };
            break;
        }

        switch (this.groupConfigs.length) {
        case 2:
            this.groupNames = new String[] {
                    NAME_12, NAME_34
            };
            break;
        case 3:
            this.groupNames = new String[] {
                    NAME_12, NAME_34,
                    NAME_56
            };
            break;
        case 4:
            this.groupNames = new String[] {
                    NAME_12, NAME_34,
                    NAME_56, NAME_78
            };
            break;
        case 6:
            this.groupNames = new String[] {
                    NAME_12, NAME_34,
                    NAME_56, NAME_78,
                    NAME_0910, NAME_1112
            };
            break;
        case 7:
            this.groupNames = new String[] {
                    NAME_12, NAME_34,
                    NAME_56, NAME_78,
                    NAME_0910, NAME_1112,
                    NAME_1314,
            };
            break;
        case 8:
            this.groupNames = new String[] {
                    NAME_12, NAME_34,
                    NAME_56, NAME_78,
                    NAME_0910, NAME_1112,
                    NAME_1314, NAME_1516
            };
            break;
        case 9:
            this.groupNames = new String[] {
                    NAME_12, NAME_34,
                    NAME_56, NAME_78,
                    NAME_0910, NAME_1112,
                    NAME_1314, NAME_1516,
                    NAME_1718
            };
            break;
        case 10:
            this.groupNames = new String[] {
                    NAME_12, NAME_34,
                    NAME_56, NAME_78,
                    NAME_0910, NAME_1112,
                    NAME_1314, NAME_1516,
                    NAME_1718, NAME_1920
            };
            break;
        default:
            // Includes 5
            throw new InvalidSetupException("No group names exist for the required number of " +
                    "groups (" + this.groupConfigs.length + ")");
        }

        if (this.groupConfigs.length != this.groupNames.length) {
            throw new InvalidSetupException("Number of groups (" + this.groupConfigs.length + ") and" +
                    " number of group names (" + this.groupNames.length + ") do not match");
        }
    }

}
