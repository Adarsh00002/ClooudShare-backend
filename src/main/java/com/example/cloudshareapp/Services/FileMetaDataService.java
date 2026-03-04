package com.example.cloudshareapp.Services;

import com.cloudinary.Cloudinary;

import com.cloudinary.utils.ObjectUtils;
import com.example.cloudshareapp.Repository.FileMetaDocumentRepository;
import com.example.cloudshareapp.document.FileMetadataDocument;
import com.example.cloudshareapp.document.ProfileDocument;
import com.example.cloudshareapp.dto.FileMetaDataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileMetaDataService {

    private final ProfileService profileService;
    private final UserCredutsService userCredutsService;
    private final FileMetaDocumentRepository fileMetaDocumentRepository;
    private final Cloudinary cloudinary;

    public List<FileMetaDataDto> uploadFiles(MultipartFile[] files) throws IOException {

        ProfileDocument currentProfile = profileService.getCurrentProfile();
        List<FileMetadataDocument> savedFiles = new ArrayList<>();

        if (!userCredutsService.hasEnoughtCredits(files.length)) {
            throw new RuntimeException("Not Enough Credits to Upload files.");
        }

        for (MultipartFile file : files) {

            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "auto",
                            "folder", "cloudshare-app"
                    )
            );

            String fileUrl = uploadResult.get("secure_url").toString();
            String publicId = uploadResult.get("public_id").toString();
            String resourceType = uploadResult.get("resource_type").toString();

            FileMetadataDocument fileMetaData = FileMetadataDocument.builder()
                    .fileLocation(fileUrl)
                    .publicId(publicId)// 🔥 Now storing URL instead of path
                    .name(file.getOriginalFilename())
                    .size(file.getSize())
                    .type(file.getContentType())
                    .resourceType(resourceType)
                    .clerkId(currentProfile.getClerkId())
                    .isPublic(false)
                    .uploadAt(LocalDateTime.now())
                    .build();

            userCredutsService.consumeCredits();
            savedFiles.add(fileMetaDocumentRepository.save(fileMetaData));
        }

        return savedFiles.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    private FileMetaDataDto mapToDto(FileMetadataDocument fileMetadataDocument) {
        return FileMetaDataDto.builder()
                .id(fileMetadataDocument.getId())
                .fileLocation(fileMetadataDocument.getFileLocation())
                .name(fileMetadataDocument.getName())
                .size(fileMetadataDocument.getSize())
                .type(fileMetadataDocument.getType())
                .clerkId(fileMetadataDocument.getClerkId())
                .isPublic(fileMetadataDocument.getIsPublic())
                .uploadAt(fileMetadataDocument.getUploadAt())
                .build();
    }

    public  List<FileMetaDataDto> getFiles(){
        ProfileDocument currentProfile=profileService.getCurrentProfile();
        List<FileMetadataDocument> files=fileMetaDocumentRepository.findByclerkId(currentProfile.getClerkId());
       return files.stream().map(this::mapToDto).collect((Collectors.toList()));

    }
    public FileMetaDataDto getPublicFile(String id){
        FileMetadataDocument file = fileMetaDocumentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));

        if (!file.getIsPublic()) {
            throw new RuntimeException("File is private");
        }

        return mapToDto(file);
    }




    public FileMetaDataDto getDownloadableFIle(String id){
     FileMetadataDocument file=  fileMetaDocumentRepository.findById(id).orElseThrow(()-> new RuntimeException("File not found"));
     return mapToDto(file);
    }


    public void deleteFile(String id) {

        ProfileDocument currentProfile = profileService.getCurrentProfile();

        FileMetadataDocument file = fileMetaDocumentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));

        if (!file.getClerkId().equals(currentProfile.getClerkId())) {
            throw new RuntimeException("File does not belong to current user");
        }

        try {

            // 🔥 Important Fix
            cloudinary.uploader().destroy(
                    file.getPublicId(),
                    ObjectUtils.asMap(
                            "resource_type",file.getResourceType(),
                            "invalidate", true
                    )
            );

            fileMetaDocumentRepository.deleteById(id);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting file from Cloudinary");
        }
    }

    public FileMetaDataDto togglePublic(String id) {
        FileMetadataDocument file = fileMetaDocumentRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));

        file.setIsPublic(!file.getIsPublic()); // 🔥 actual toggle
        fileMetaDocumentRepository.save(file);

        return mapToDto(file);
    }

}
