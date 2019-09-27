package name.ulbricht.sudoku;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectPackages({ "name.ulbricht.sudoku.grid", "name.ulbricht.sudoku" })
public final class AllTests {

}