/*******************************************************************************
 * Copyright 2019, 2020 grondag
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package grondag.facility;

import java.io.File;
import java.io.FileOutputStream;

import blue.endless.jankson.Comment;
import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.fabricmc.loader.api.FabricLoader;

public class FacilityConfig {

	@SuppressWarnings("hiding")
	public static class ConfigData {
		@Comment("Gives more space for REI item display.")
		public boolean shiftScreensLeftIfReiPresent = true;

		@Comment("Use Minecraft font in storage screens.")
		public boolean useVanillaFonts = false;
	}

	public static final ConfigData DEFAULTS = new ConfigData();
	private static final Gson GSON = new GsonBuilder().create();
	private static final Jankson JANKSON = Jankson.builder().build();
	private static File configFile;

	public static int maxRenderDistance =  32;
	public static long maxRenderDistanceSq = maxRenderDistance * maxRenderDistance;
	public static long keepaliveIntervalMilliseconds = 10000;
	public static boolean  shiftScreensLeftIfReiPresent = DEFAULTS.shiftScreensLeftIfReiPresent;
	public static boolean useVanillaFonts = DEFAULTS.useVanillaFonts;

	public static void init() {
		configFile = new File(FabricLoader.getInstance().getConfigDirectory(), "exotic-blocks.json5");
		if (configFile.exists()) {
			loadConfig();
		} else {
			saveConfig();
		}
	}

	private static void loadConfig() {
		ConfigData config = new ConfigData();
		try {
			final JsonObject configJson = JANKSON.load(configFile);
			final String regularized = configJson.toJson(false, false, 0);
			config = GSON.fromJson(regularized, ConfigData.class);
		} catch (final Exception e) {
			e.printStackTrace();
			Facility.LOG.error("Unable to load config. Using default values.");
		}

		shiftScreensLeftIfReiPresent =  config.shiftScreensLeftIfReiPresent;
		useVanillaFonts = config.useVanillaFonts;
	}

	public static void saveConfig() {
		final ConfigData config = new ConfigData();

		config.shiftScreensLeftIfReiPresent = shiftScreensLeftIfReiPresent;
		config.useVanillaFonts = useVanillaFonts;

		try {
			final String result = JANKSON.toJson(config).toJson(true, true, 0);
			if (!configFile.exists()) {
				configFile.createNewFile();
			}

			try (FileOutputStream out = new FileOutputStream(configFile, false);) {
				out.write(result.getBytes());
				out.flush();
				out.close();
			}
		} catch (final Exception e) {
			e.printStackTrace();
			Facility.LOG.error("Unable to save config.");
			return;
		}
	}
}
