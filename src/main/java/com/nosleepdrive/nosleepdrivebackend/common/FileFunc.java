package com.nosleepdrive.nosleepdrivebackend.common;

import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
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
        List<File> file = new LinkedList<>();
        file.add(videoFile);
        return makeFilesToZip(zipFilePath, file);
    }

    public returnFileData makeFilesToZip(String zipFilePath, List<File> videoFiles){
        try {
            try (FileOutputStream fos = new FileOutputStream(zipFilePath);
                 ZipOutputStream zipOut = new ZipOutputStream(fos)) {
                Set<String> existingNames = new HashSet<>();
                for(File file: videoFiles){
                    addFileToZip(file, zipOut, existingNames);
                }
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

    public static boolean isFaststartProcessed(String videoPath) {
        try{
            try (RandomAccessFile raf = new RandomAccessFile(videoPath, "r")) {
                while (true) {
                    long pos = raf.getFilePointer();
                    if (pos + 8 > raf.length()) break; // 더 이상 읽을 박스 없음

                    int size = raf.readInt();
                    byte[] typeBytes = new byte[4];
                    raf.readFully(typeBytes);
                    String type = new String(typeBytes, "UTF-8");

                    if ("moov".equals(type)) {
                        // moov 박스 발견
                        System.out.println("moov");
                        return true; // moov가 먼저 나왔으므로 faststart
                    } else if ("mdat".equals(type)) {
                        // mdat 박스가 먼저 나오면 faststart 아님
                        System.out.println("mdat");
                        return false;
                    }

                    if (size < 8) break; // 이상한 박스 크기
                    raf.seek(pos + size); // 다음 박스 위치로 이동
                }
            }
            return false; // moov 또는 mdat 찾지 못함
        }
        catch (Exception e){
            throw new CustomError(HttpStatus.BAD_REQUEST.value(), Message.ERR_INVALID_VIDEO.getMessage());
        }
    }
    public static void applyFaststart(String inputPath, String outputPath) {
        System.out.println("this is not preapplied.");
        try {
            ProcessBuilder builder = new ProcessBuilder(
                    "ffmpeg", "-y",
                    "-i", inputPath,
                    "-movflags", "faststart",
                    outputPath
            );
            builder.redirectErrorStream(true);
            Process process = builder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                while (reader.readLine() != null) {
                }
            }
            process.waitFor();
        }
        catch (Exception e){
            throw new CustomError(HttpStatus.BAD_REQUEST.value(), Message.ERR_INVALID_VIDEO.getMessage());
        }
    }

    private static void addFileToZip(File file, ZipOutputStream zipOut, Set<String> existingNames) {
        try (FileInputStream fis = new FileInputStream(file)) {
            String fileName = file.getName();
            int counter = 1;

            while (existingNames.contains(fileName)) {
                fileName = file.getName().replaceAll("(\\.\\w+)$", "(" + counter + ")$1");
                counter++;
            }
            existingNames.add(fileName);

            ZipEntry zipEntry = new ZipEntry(fileName);
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
        } catch (Exception e) {
            throw new CustomError(HttpStatus.BAD_REQUEST.value(), Message.ERR_DURING_STREAM.getMessage());
        }
    }
}
