package com.example.cloudshareapp.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "files")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FileMetadataDocument {

    @Id
    private  String id;
    private  String name;
    private  String type;
    private  Long size;
    private  String clerkId;
    private  Boolean isPublic;
    private  String fileLocation;
    private String publicId;
    private String resourceType;
    private LocalDateTime uploadAt;

}
