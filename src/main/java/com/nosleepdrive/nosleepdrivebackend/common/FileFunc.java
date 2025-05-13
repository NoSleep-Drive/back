package com.nosleepdrive.nosleepdrivebackend.common;

import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class FileFunc {
    @AllArgsConstructor
    public class returnFileData {
        public InputStreamResource stream;
        public Long fileSize;
    }

    public returnFileData makeOneFileToZip(String zipFilePath, File videoFile){
        try {
            try (FileOutputStream fos = new FileOutputStream(zipFilePath);
                 ZipOutputStream zipOut = new ZipOutputStream(fos)) {
                addFileToZip(videoFile, zipOut);
            }

            Path zipPath = Paths.get(zipFilePath);
            if (Files.notExists(zipPath)) {
                throw new CustomError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "압축 파일 생성 실패.");
            }

            InputStreamResource resource = new InputStreamResource(Files.newInputStream(zipPath));
            return new returnFileData(resource, Files.size(zipPath));
        }
        catch (Exception e){
            throw new CustomError(HttpStatus.BAD_REQUEST.value(), Message.ERR_DURING_STREAM.getMessage());
        }
    }

    private static void addFileToZip(File file, ZipOutputStream zipOut) {
        try (FileInputStream fis = new FileInputStream(file)) {
            ZipEntry zipEntry = new ZipEntry(file.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
        }
        catch(Exception e){
            throw new CustomError(HttpStatus.BAD_REQUEST.value(), Message.ERR_DURING_STREAM.getMessage());
        }
    }
}
