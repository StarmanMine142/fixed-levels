package com.neptunecentury.fixedlevels;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Manages the configuration loading and saving
 * @param <T> The type of the config class
 */
public class ConfigManager<T> {
    private final String _name;
    private final Logger _logger;
    private T _cfg;

    /**
     * Constructor
     *
     * @param name The name of the config file
     */
    public ConfigManager(String name, Logger logger) {
        _name = name;
        _logger = logger;
    }

    /**
     * Gets the configuration
     *
     * @return An object containing the configuration
     */
    public T getConfig() {
        return _cfg;
    }

    /**
     * Loads the configuration file
     * @param clazz The class object for the class type to load
     */
    public void load(Class<T> clazz) {
        // Create a new Jankson instance
        var jankson = Jankson.builder().build();
        // Parse the config file into a JSON Object
        try {
            // Locate the file in the config folder and return it as a File object
            File configFile = FabricLoader.getInstance().getConfigDir().resolve(_name + ".json5").toFile();

            // Load the file
            JsonObject configJson = jankson.load(configFile);

            // Convert the raw object into your POJO type
            T config = jankson.fromJson(configJson, clazz);

            _logger.info("[{}] Loaded configuration", _name);

            _cfg = config;

        } catch (FileNotFoundException e) {
            _logger.warn("[{}] Could not load config: {}: Using default values.", _name, e.getMessage());
            // Create defaults
            try {
                _cfg = clazz.getDeclaredConstructor().newInstance();

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

        } catch (Exception ex) {
            _logger.error("[{}] Error loading config: {}", _name, ex.getMessage());
            throw new RuntimeException(ex);

        }

    }

    /**
     * Saves the configuration to a file
     */
    public void save() {
        if (_cfg == null) {
            return;
        }

        // Locate the file in the config folder and return it as a File object
        File configFile = FabricLoader.getInstance().getConfigDir().resolve(_name + ".json5").toFile();

        var jankson = Jankson.builder().build();
        var result = jankson
                .toJson(_cfg)                       // The first call makes a JsonObject
                .toJson(true, true); // The second turns the JsonObject into a String

        try {
            // Check if the file exists or if we successfully created new one
            var fileIsUsable = configFile.exists() || configFile.createNewFile();
            if (!fileIsUsable) throw new FileNotFoundException();

            // Create output stream from our file.
            var out = new FileOutputStream(configFile, false);

            // Write out the file and close it.
            out.write(result.getBytes());
            out.flush();
            out.close();

        } catch (IOException e) {
            _logger.warn("[{}] Could not save config: {}", _name, e.getMessage());

        }
    }

}
