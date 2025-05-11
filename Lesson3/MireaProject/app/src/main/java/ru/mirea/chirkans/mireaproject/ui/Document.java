package ru.mirea.chirkans.mireaproject.ui;

import java.io.File;

public class Document {
    private final String title;
    private final File file;

    public Document(String title, File file) {
        this.title = title;
        this.file = file;
    }

    public String getTitle() {
        return title;
    }

    public File getFile() {
        return file;
    }
}
