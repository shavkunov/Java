package ru.spbau.shavkunov;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Usage: path exp");
            return;
        }

        Path path = Paths.get(args[0]);
        Pattern pattern = Pattern.compile(args[1]);

        unzipFiles(path, pattern);
    }

    /**
     * Разархивирование всех архивов в папке.
     * Под каждый архив создается отдельная папка с названием как у архива и с суффиксом _folder.
     * @param rootDir корневая папка с нахождением архивов.
     * @param pattern регулярное выражение для файлов, находящихся в архивах.
     */
    public static void unzipFiles(Path rootDir, Pattern pattern) throws IOException {
        File folder = rootDir.toFile();
        for (File file : folder.listFiles()) {
            String filename = file.getName();
            if (filename.endsWith(".zip")) {
                String filenameWithoutExtension = filename.substring(0, filename.lastIndexOf("."));
                Path curDir = rootDir.resolve(filenameWithoutExtension + "_folder");
                Files.createDirectory(curDir);

                ZipFile zip = new ZipFile(file);
                Enumeration<? extends ZipEntry> entries = zip.entries();

                while (entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();

                    if (entry.isDirectory()) {
                        // не спрашивайте, что это
                        if (!entry.getName().equals("__MACOSX/")) {
                            Files.createDirectories(curDir.resolve(entry.getName()));
                        }
                    } else {
                        if (pattern.matcher(entry.getName()).matches()) {
                            Files.copy(zip.getInputStream(entry), curDir.resolve(entry.getName()), REPLACE_EXISTING);
                        }
                    }
                }
            }
        }
    }
}