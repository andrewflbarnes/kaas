/**
 * Kings Ski Club Race Organiser
 */
package net.aflb.kaas.core.legacy.races.division.impl;

import net.aflb.kaas.core.legacy.races.division.DivisionConfiguration;
import net.aflb.kaas.kings.engine.GroupConfiguration;

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
