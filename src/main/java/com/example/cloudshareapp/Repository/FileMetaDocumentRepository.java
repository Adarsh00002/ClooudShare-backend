package com.example.cloudshareapp.Repository;

import com.example.cloudshareapp.document.FileMetadataDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FileMetaDocumentRepository extends MongoRepository<FileMetadataDocument,String> {
    List<FileMetadataDocument> findByclerkId(String clerkId);

   Long countByClerkId(String clerkId);


}
