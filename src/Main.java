import com.custom.DB;
import com.custom.Menu;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
	public static void main(String[] args) {
		Menu menu = Menu.getInstance();

		DB db = new DB();
		try {
			ResultSet res = db.query("select * from medics");
			if (res.isBeforeFirst()) menu.openMenu();
			else menu.createMedic();
		} catch (SQLException ignored) {
			System.out.println("An error has occurred.");
		}

		while (menu.isOpened) {
			menu.handleStep();
		}
	}
}