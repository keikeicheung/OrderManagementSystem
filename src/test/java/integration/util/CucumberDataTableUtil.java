package integration.util;

import cucumber.api.DataTable;
import gherkin.formatter.model.DataTableRow;

import java.util.List;

/**
 * Created by keikeicheung on 03/03/2018.
 */
public class CucumberDataTableUtil {
    public static String getValue(List<String> titles, DataTableRow row, String targetTitle) {
        int index = titles.indexOf(targetTitle);
        return index >= 0 ? row.getCells().get(index) : null;
    }

    public static List<String> getTitles(DataTable table) {
        return table.getGherkinRows().get(0).getCells();
    }
}
