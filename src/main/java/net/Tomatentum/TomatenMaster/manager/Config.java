package net.Tomatentum.TomatenMaster.manager;

import org.bspfsystems.yamlconfiguration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {
	private YamlConfiguration config;
	private File file;
	private YamlConfiguration rrconfig;
	boolean isnew= false;
	private File rrfile;


	public Config() {

		try{
			this.file = new File("database.yml");
			this.rrfile = new File("rrdata.yml");
			if (!file.exists()) {
				file.createNewFile();
				isnew = true;
			}
			if (!rrfile.exists()) {
				rrfile.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		config = YamlConfiguration.loadConfiguration(file);
		rrconfig = YamlConfiguration.loadConfiguration(rrfile);
		if (isnew) {
			config.set("TOKEN", null);
			save();
		}
		System.out.println("[Config] loaded Config");

	}
	public void save() {
		try {
			config.save(file);
			rrconfig.save(rrfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public YamlConfiguration getYML() {
		return config;
	}

	public File getFile() {
		return file;
	}
	public YamlConfiguration getRrconfig() {
		return rrconfig;
	}
}
