package net.aflb.kaas.core.legacy.export;

import lombok.extern.slf4j.Slf4j;
import net.aflb.kaas.core.model.competing.Match;
import net.aflb.kaas.core.model.Team;

//import crl.android.pdfwriter.PDFWriter;
//import crl.android.pdfwriter.PaperSize;
//import crl.android.pdfwriter.StandardFonts;

import java.util.List;
import java.util.Optional;

@Slf4j
public class PdfMatchSerializer implements MatchSerializer {

//    private static final String STRING_BUFFER = "                          ";
//    private static final int TOP_START_POS = PaperSize.A4_HEIGHT - 60;
//    private static final int LEFT_BORDER = 25;
//    private static final int TEAM_BOX_WIDTH = 200;
//    private static final int VS_BOX_WIDTH = 30;
//    private static final int BOX_HEIGHT = 20;
//    private static final int RACE_BOX_WIDTH = 30;
//    private static final int DIVISION_BOX_WIDTH = 30;
//    private static final int LOWEST_WRITE_HEIGHT = 60;

    @Override
    public Result writeRaceList(List<Match> matches, List<Team> teams) {
        return new Result(true, Optional.of(new UnsupportedOperationException("Unimplemented")), Optional.empty());
//        PDFWriter writer = new PDFWriter(PaperSize.A4_WIDTH, PaperSize.A4_HEIGHT);
//        writer.setFont(StandardFonts.SUBTYPE, StandardFonts.COURIER_BOLD);
//        int toppos = TOP_START_POS;
//
//        SparseArray<Team> teamArray = new SparseArray<>(teamsList.size());
//        for (int i = 0, n = teamsList.size(); i < n; i++) {
//            teamArray.append(teamsList.get(i).getTeamId(), teamsList.get(i));
//        }
//
//        String teamOne;
//        String teamTwo;
//        String raceNum;
//        String thisDiv;
//        String prevDiv = "DUMMY";
//        int offset;
//        int count = 0;
//        for (int i = 0, n = races.size(); i < n; i++) {
//            count += 1;
//            raceNum = String.valueOf(races.get(i).getRaceNo());
//            teamOne = " " + teamArray.get(races.get(i).getTeamOne()).getTeamName();
//            teamTwo = teamArray.get(races.get(i).getTeamTwo()).getTeamName();
//            teamTwo = STRING_BUFFER.substring(0, 22 - teamTwo.length()) + teamTwo;
//            try {
//                thisDiv = " " + races.get(i).getDivision().substring(0, 1).toUpperCase();
//            } catch (NullPointerException | IndexOutOfBoundsException e) {
//                // TODO Log error message
//                thisDiv = " -";
//            }
//
//            log.debug(LOG_TAG, "Writing race:" + raceNum + ", " + thisDiv + ", " + teamOne + ", " + teamTwo.trim());
//
//            offset = LEFT_BORDER;
//            writer.addText(offset, toppos, 14, thisDiv);
//            writer.addRectangle(offset, toppos - 5, DIVISION_BOX_WIDTH, BOX_HEIGHT);
//            offset += DIVISION_BOX_WIDTH;
//            writer.addText(offset, toppos, 14, " " + raceNum);
//            writer.addRectangle(offset, toppos - 5, RACE_BOX_WIDTH, BOX_HEIGHT);
//            offset += RACE_BOX_WIDTH;
//            writer.addText(offset, toppos, 14, teamOne);
//            writer.addRectangle(offset, toppos - 5, TEAM_BOX_WIDTH, BOX_HEIGHT);
//            offset += TEAM_BOX_WIDTH;
//            writer.addText(offset, toppos, 14, " v");
//            writer.addRectangle(offset, toppos - 5, VS_BOX_WIDTH, BOX_HEIGHT);
//            offset += VS_BOX_WIDTH;
//            writer.addText(offset, toppos, 14, teamTwo);
//            writer.addRectangle(offset, toppos - 5, TEAM_BOX_WIDTH, BOX_HEIGHT);
//            offset += TEAM_BOX_WIDTH;
//            writer.addText(offset, toppos, 14, " " + raceNum);
//            writer.addRectangle(offset, toppos - 5, RACE_BOX_WIDTH, BOX_HEIGHT);
//            offset += RACE_BOX_WIDTH;
//
//            // Hard line at top of page or division change
//            if (count == 1 || !prevDiv.equalsIgnoreCase(thisDiv)) {
//                writeBoldLine(writer, toppos, offset);
//            }
//
//            toppos -= BOX_HEIGHT;
//
//            // Hard line on bottom of page and end of races
//            if (i == n - 1 || toppos < LOWEST_WRITE_HEIGHT) {
//                writeBoldLine(writer, toppos, offset);
//                count = 0;
//            }
//
//            if (toppos < 60) {
//                if (i < n - 1) {
//                    writer.newPage();
//                    toppos = TOP_START_POS;
//                    writer.setFont(StandardFonts.SUBTYPE, StandardFonts.COURIER_BOLD);
//                    count = 0;
//                }
//            }
//            prevDiv = thisDiv;
//        }
//
//        try {
//            log.(LOG_TAG, "Creating file..." + outputFile.getName() + "...");
//
//            boolean fileCreated = outputFile.createNewFile();
//
//            if (fileCreated) {
//                log.info(LOG_TAG, "...File successfully created");
//            } else {
//                log.error(LOG_TAG, "...File was not created!");
//                return false;
//            }
//        } catch(IOException e) {
//            log.error(LOG_TAG, "IOException creating " + outputFile.getName(), e);
//            return false;
//        }
//
//        try {
//            log.info(LOG_TAG, "Writing file " + outputFile.getName() + "...");
//            FileOutputStream pdfFile = new FileOutputStream(outputFile);
//            pdfFile.write(writer.asString().getBytes(encoding));
//            pdfFile.close();
//            log.info(LOG_TAG, "...done");
//            return true;
//        } catch(UnsupportedEncodingException e) {
//            log.error(LOG_TAG, "UnsupportedEncodingException creating " + outputFile.getName(), e);
//        } catch(IOException e) {
//            log.error(LOG_TAG, "FileNotFoundException creating " + outputFile.getName(), e);
//        }
//
//        return false;
    }

//    private void writeBoldLine(PDFWriter writer, int toppos, int offset) {
//        writer.addRawContent("3 w\n");
//        writer.addLine(LEFT_BORDER, toppos - 5 + BOX_HEIGHT, offset, toppos - 5 + BOX_HEIGHT);
//        writer.addRawContent("1 w\n");
//    }
}
