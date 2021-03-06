/**
 * Kings Ski Club Race Organiser
 */
package net.aflb.kaas.core.legacy.races.division.impl;

import net.aflb.kaas.core.legacy.races.division.DivisionConfiguration;
import net.aflb.kaas.core.legacy.races.group.GroupConfiguration;

/**
 * @author Barnesly.
 */
public class DivisionConfigurationKnockout implements DivisionConfiguration {

    private GroupConfiguration[] groupGrid;
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
    public GroupConfiguration[] getGroupGrid() {
        return this.groupGrid;
    }

    @Override
    public void setTeams(int numTeams) throws InvalidNumberOfTeamsException {

        if (numTeams < 4 || numTeams > 32) {
            throw new InvalidNumberOfTeamsException("Too many/few teams (" + String.valueOf(numTeams) + ")");
        }

        switch (numTeams) {
        case 4:
        case 5:
            this.groupGrid = new GroupConfiguration[] {
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT
            };
            this.transformationMapping = new String[][] {
                    {"1A", "2A"}, {"3A", "4A"}
            };
            break;
        case 6:
            this.groupGrid = new GroupConfiguration[] {
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT,
                    GroupConfiguration.KNOCKOUT
            };
            this.transformationMapping = new String[][] {
                    {"1A", "2A"}, {"3A", "4A"}, {"5A", "6A"}
            };
            break;
        case 7:
            this.groupGrid = new GroupConfiguration[] {
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT
            };
            this.transformationMapping = new String[][] {
                    {"11", "21"}, {"31", "41"}
            };
            break;
        case 8:
            this.groupGrid = new GroupConfiguration[] {
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
            this.groupGrid = new GroupConfiguration[] {
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
            this.groupGrid = new GroupConfiguration[] {
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT,
                    GroupConfiguration.KNOCKOUT, GroupConfiguration.KNOCKOUT
            };
            this.transformationMapping = new String[][] {
                    {"11", "12"}, {"21", "22"},
                    {"31", "32"}, {"41", "42"}
            };
            break;
        case 14:
            this.groupGrid = new GroupConfiguration[] {
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
            this.groupGrid = new GroupConfiguration[] {
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
            this.groupGrid = new GroupConfiguration[] {
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
            this.groupGrid = new GroupConfiguration[] {
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
            this.groupGrid = new GroupConfiguration[] {
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
            this.groupGrid = new GroupConfiguration[] {
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
            this.groupGrid = new GroupConfiguration[] {
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

        switch (this.groupGrid.length) {
        case 2:
            this.groupNames = new String[] {
                    "1st/2nd", "3rd/4th"
            };
            break;
        case 3:
            this.groupNames = new String[] {
                    "1st/2nd", "3rd/4th",
                    "5th/6th"
            };
            break;
        case 4:
            this.groupNames = new String[] {
                    "1st/2nd", "3rd/4th",
                    "5th/6th", "7th/8th"
            };
            break;
        case 6:
            this.groupNames = new String[] {
                    "1st/2nd", "3rd/4th",
                    "5th/6th", "7th/8th",
                    "9th/10th", "11th/12th"
            };
            break;
        case 7:
            this.groupNames = new String[] {
                    "1st/2nd", "3rd/4th",
                    "5th/6th", "7th/8th",
                    "9th/10th", "11th/12th",
                    "13th/14th",
            };
            break;
        case 8:
            this.groupNames = new String[] {
                    "1st/2nd", "3rd/4th",
                    "5th/6th", "7th/8th",
                    "9th/10th", "11th/12th",
                    "13th/14th", "15th/16th"
            };
            break;
        case 9:
            this.groupNames = new String[] {
                    "1st/2nd", "3rd/4th",
                    "5th/6th", "7th/8th",
                    "9th/10th", "11th/12th",
                    "3th/14th", "15th/16th",
                    "16th/18th"
            };
            break;
        case 10:
            this.groupNames = new String[] {
                    "1st/2nd", "3rd/4th",
                    "5th/6th", "7th/8th",
                    "9th/10th", "11th/12th",
                    "3th/14th", "15th/16th",
                    "16th/18th", "19th/20th"
            };
            break;
        default:
            // Includes 5
            throw new InvalidSetupException("No group names exist for the required number of " +
                    "groups (" + this.groupGrid.length + ")");
        }

        if (this.groupGrid.length != this.groupNames.length) {
            throw new InvalidSetupException("Number of groups (" + this.groupGrid.length + ") and" +
                    " number of group names (" + this.groupNames.length + ") do not match");
        }
    }
}
