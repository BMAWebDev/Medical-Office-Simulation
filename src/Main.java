import com.custom.Menu;

public class Main {
	public static void main(String[] args) {
		Menu menu = Menu.getInstance();
		menu.openMenu();

		while (menu.isOpened) {
			menu.handleStep();
		}
	}
}