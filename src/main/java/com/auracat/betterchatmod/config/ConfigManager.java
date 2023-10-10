package com.auracat.betterchatmod.config;

import com.auracat.betterchatmod.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigManager {
    @Nullable
    public static Path configFolderPath = null;
    public static final Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .create();

    public static void initializeConfigFolder(String configFolderPathString) throws IOException {
        configFolderPath = Paths.get(configFolderPathString);

        try {
            Files.createDirectory(configFolderPath);
        } catch (IOException e) {
            if (!(e instanceof FileAlreadyExistsException)) {
                throw e;
            }
        }
    }

    /**
     * @param filePathString - The path of the file. Starts from "{minecraft_folder}/config/reindev_betterchatmod"
     * @param configClass - The class that will contain the parsed Json values
     * @return T
     */
    public static <T> T readConfigFile(String filePathString, Class<T> configClass, Config defaultObject) throws IOException {
        assert configFolderPath != null;
        Path resolvedFilePath = configFolderPath.resolve(filePathString);
        File file = new File(
                resolvedFilePath.toUri()
        );

        if (!file.exists()) {
            Utils.log("Config file at \"" + resolvedFilePath + "\" not found. Creating new file with default values.");
            writeConfigFile(filePathString, defaultObject);
        }

        String jsonString = null;
        try (
                BufferedReader reader = new BufferedReader(
                        new FileReader(file)
                )
        ) {
            jsonString = Utils.readAllCharactersOneByOne(reader);
        }

        return gson.fromJson(jsonString, configClass);
    }

    public static void writeConfigFile(String filePathString, Object obj) throws IOException {
        assert configFolderPath != null;

        File file = new File(
                configFolderPath.resolve(filePathString).toUri()
        );
        File tempFile = new File(
                configFolderPath.resolve(filePathString + ".temp").toUri()
        );

        try (FileWriter tempWriter = new FileWriter(tempFile)) {
            String jsonString = gson.toJson(obj);
            tempWriter.write(jsonString);
            tempWriter.flush();
        }

        Files.deleteIfExists(file.toPath());
        Files.move(tempFile.toPath(), file.toPath());
    }

    @SuppressWarnings({"unused", "RedundantThrows"})
    public static void initialize() throws IOException {
        Utils.methodNotImplemented();
    }
}
