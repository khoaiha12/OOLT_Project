package projectCK.core.map;

import java.io.File;
import java.io.IOException;

import projectCK.core.SuffixFileFilter;

public class LocalMapProvider implements MapProvider {

	private final File map_dir;
	private final SuffixFileFilter map_file_filter = new SuffixFileFilter("dat");

	private File[] map_file_list;

	public LocalMapProvider(File map_dir) {
		this.map_dir = map_dir;
		refreshMapList();
	}

	@Override
	public final int getMapCount() {
		return map_file_list.length;
	}

	@Override
	public final Map getMap(int index) {
		if (0 <= index && index <= map_file_list.length) {
			try {
				return MapFactory.createMap(map_file_list[index]);
			} catch (IOException ex) {
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public final String getMapName(int index) {
		if (0 <= index && index <= map_file_list.length) {
			return map_file_list[index].getName();
		} else {
			return null;
		}
	}

	@Override
	public final void refreshMapList() {
		map_file_list = map_dir.listFiles(map_file_filter);
	}

}
