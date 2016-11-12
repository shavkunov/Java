package ru.spbau.shavkunov;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class zipTest {
    private Path correct = Paths.get("").resolve("zipTest").resolve("correct");
    private Path test = Paths.get("").resolve("zipTest").resolve("test");

    @Test
    public void unzipFilesTest() throws Exception {
        Main.main(new String[]{test.toString(), "a[b|bb].*"});

        File firstCorrect = correct.resolve("first.zip").toFile();
        File secondCorrect = correct.resolve("second.zip").toFile();
        File textFileCorrect = correct.resolve("file.txt").toFile();
        File firstFolderCorrect = correct.resolve("first_folder").toFile();
        File secondFolderCorrect = correct.resolve("second_folder").toFile();

        File first = test.resolve("first.zip").toFile();
        File second = test.resolve("second.zip").toFile();
        File textFile = test.resolve("file.txt").toFile();
        File firstFolder = test.resolve("first_folder").toFile();
        File secondFolder = test.resolve("second_folder").toFile();

        checkFiles(firstCorrect, first);
        checkFiles(secondCorrect, second);
        checkFiles(textFileCorrect, textFile);
        checkFolders(firstFolderCorrect, firstFolder);
        checkFolders(secondFolderCorrect, secondFolder);
    }

    public void checkFiles(File correct, File test) throws IOException {
        byte[] correctBytes = Files.readAllBytes(correct.toPath());
        byte[] bytes = Files.readAllBytes(test.toPath());

        assertEquals(correct.getName(), test.getName());
        assertArrayEquals(correctBytes, bytes);
    }

    public void checkFolders(File correctFolder, File folder) throws IOException {
        assertEquals(correctFolder.getName(), folder.getName());
        assertEquals(1, folder.listFiles().length);
        File correctFile = correctFolder.listFiles()[0];
        File testFile = folder.listFiles()[0];

        checkFiles(correctFile, testFile);
    }

    @After
    public void clear() throws Exception {
        FileUtils.deleteDirectory(new File(test.resolve("first_folder").toString()));
        FileUtils.deleteDirectory(new File(test.resolve("second_folder").toString()));
    }
}