package com.ge.si.dcmexport;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AppConfig {
	public static String SCP_LOCAL_AE = "scp.localAE";
	public static String SCP_LOCAL_PORT = "scp.localPort";
	public static String SCP_FILEPATH = "scp.filepath";
	public static String SCP_DIRECTORY = "scp.directory";

	public static String MOVE_LOCAL_AE = "move.localAE";
	public static String MOVE_DEST_AE = "move.destAE";
	public static String MOVE_REMOTE_AE = "move.remoteAE";
	public static String MOVE_REMOTE_PORT = "move.remotePort";
	public static String MOVE_REMOTE_HOST = "move.remoteHost";
	public static String MOVE_PARAMETERS = "move.parameters";
	Properties props = new Properties();
	private AppConfig() {
		try {
			props.load(new FileInputStream(new File("config/config.properties")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static AppConfig config = new AppConfig();

	public static AppConfig getInstance() {
		return config;
	}

	public String getProperty(String key) {
		return props.getProperty(key);
	}

}
