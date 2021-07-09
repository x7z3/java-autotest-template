package ru.bootdev.test.core.helper;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FileHelper {

    public static final String TMP_DIR = System.getProperty("java.io.tmpdir");
    public static final String ALL_FILES = ".*";

    private FileHelper() {
    }

    public static File packFilesToZip(File zip, List<File> filesToPack) {
        filesToPack.forEach(file -> packFileToZip(zip, file));
        return zip;
    }

    public static File packFileToZip(File zip, File file) {
        ZipFile archive = new ZipFile(zip);
        try {
            if (file.isDirectory()) {
                archive.addFolder(file);
            } else {
                archive.addFile(file);
            }
        } catch (ZipException e) {
            e.printStackTrace();
        }
        return zip;
    }

    public static List<File> getDirectoryFiles(String path, String fileMask) {
        return getDirectoryFiles(path, file -> file.getName().matches(fileMask));
    }

    public static List<File> getDirectoryFiles(String path) {
        return getDirectoryFiles(path, ALL_FILES);
    }

    public static List<File> getDirectoryFiles(String path, Predicate<File> filter) {
        File[] files = new File(path).listFiles();
        if (files == null || files.length == 0) return new ArrayList<>();
        return Arrays.stream(files).filter(filter).collect(Collectors.toList());
    }

    public static void deleteFiles(List<File> files) {
        deleteFiles(files.toArray(new File[0]));
    }

    public static void deleteFiles(File... files) {
        if (files.length == 0) return;
        Arrays.asList(files).forEach(file -> {
            try {
                Files.delete(file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static File createTmpFile() {
        String currentTime = Long.toString(System.nanoTime());
        return createTmpFile(currentTime + ".tmp");
    }

    public static File createTmpFile(String fileName) {
        return new File(TMP_DIR, fileName);
    }

    public static File getResourceFile(String path) {
        return new File(Objects.requireNonNull(FileHelper.class.getClassLoader().getResource(path)).getPath());
    }

    public static String readFile(String path) throws IOException {
        return readFile(Paths.get(path));
    }

    public static String readFile(File file) throws IOException {
        return readFile(file.toPath());
    }

    public static String readFile(Path path) throws IOException {
        return new String(Files.readAllBytes(path));
    }

    public static void writeFile(File file, String text) throws IOException {
        Files.write(file.toPath(), text.getBytes());
    }
}
