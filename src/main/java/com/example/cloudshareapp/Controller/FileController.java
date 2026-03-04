package com.example.cloudshareapp.Controller;

import com.example.cloudshareapp.Services.FileMetaDataService;
import com.example.cloudshareapp.Services.UserCredutsService;
import com.example.cloudshareapp.document.UserCredits;
import com.example.cloudshareapp.dto.FileMetaDataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController {

    private final FileMetaDataService fileMetaDataService;
    private final UserCredutsService userCredutsService;

    // ---------------------------------------------------
    // UPLOAD FILES
    // ---------------------------------------------------
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFiles(@RequestPart("files") MultipartFile[] files) throws IOException {

        Map<String, Object> response = new HashMap<>();

        List<FileMetaDataDto> uploadedFiles = fileMetaDataService.uploadFiles(files);
        UserCredits finalCredits = userCredutsService.getUserCredits();

        response.put("files", uploadedFiles);
        response.put("remainingCredits", finalCredits.getCredits());

        return ResponseEntity.ok(response);
    }

    // ---------------------------------------------------
    // GET FILES FOR CURRENT USER
    // ---------------------------------------------------
    @GetMapping("/my")
    public ResponseEntity<?> getFilesForCurrentUser() {
        List<FileMetaDataDto> files = fileMetaDataService.getFiles();
        return ResponseEntity.ok(files);
    }

    // ---------------------------------------------------
    // GET PUBLIC FILE
    // ---------------------------------------------------
    @GetMapping("/public/{id}")
    public ResponseEntity<?> getPublicFile(@PathVariable String id) {
        FileMetaDataDto file = fileMetaDataService.getPublicFile(id);
        return ResponseEntity.ok(file);
    }

    // ---------------------------------------------------
    // DOWNLOAD FILE
    // ---------------------------------------------------
    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable String id) {

        FileMetaDataDto file = fileMetaDataService.getDownloadableFIle(id);

        return ResponseEntity.ok(Map.of("url", file.getFileLocation()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable String id){
        fileMetaDataService.deleteFile(id);

        return  ResponseEntity.noContent().build();

    }

    @PatchMapping("/{id}/toggle-public")
    public  ResponseEntity<?> togglePublic(@PathVariable String id){
        FileMetaDataDto file=fileMetaDataService.togglePublic(id);
        return ResponseEntity.ok(file);



    }
}
