package de.ptb.backend.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

public class DKCRResponseMessage {
    String fileName;
    String base64String;

    public DKCRResponseMessage(String fileName, File file) throws IOException {
        this.fileName = fileName;
        byte[] fileContent = Files.readAllBytes(file.toPath());
        this.base64String = Base64.getEncoder().encodeToString(fileContent);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getBase64String() {
        return base64String;
    }

    public void setBase64String(File file) throws IOException {
        byte[] fileContent = Files.readAllBytes(file.toPath());
        this.base64String = Base64.getEncoder().encodeToString(fileContent);
    }

    @Override
    public String toString() {
        return "DKCRResponseMessage{" +
                "fileName='" + fileName + ".xml"+'\'' +
                ", base64String='" + base64String + '\'' +
                '}';
    }
}
