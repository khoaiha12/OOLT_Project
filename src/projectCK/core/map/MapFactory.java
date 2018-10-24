package projectCK.core.map;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import projectCK.core.unit.Unit;
import projectCK.core.unit.UnitFactory;

public class MapFactory {

	private static Scanner fis;

	private MapFactory() {
	}

	public static Map createMap(File map_file) throws IOException {
		
		File fiss = new File(map_file.getAbsolutePath());
		fis = new Scanner(fiss);
		String author_name = "default";
		fis.nextLine();
		boolean[] team_access = new boolean[4];
		for (int team = 0; team < 4; team++) {
			team_access[team] = fis.nextBoolean();
		}
		int width = fis.nextInt();
		int height = fis.nextInt();
		short[][] map_data = new short[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				map_data[i][j] = fis.nextShort();
			}
		}
		Map map = new Map(map_data, team_access, author_name);
		int unit_count = fis.nextInt();
		
		for (int i = 0; i < unit_count; i++) {
			int team = fis.nextInt();
			int index = fis.nextInt();
			int x = fis.nextInt();
			int y = fis.nextInt();
			Unit unit = UnitFactory.createUnit(index, team);
			unit.setX(x);
			unit.setY(y);
			map.addUnit(unit);
		}
		return map;
	}

}
