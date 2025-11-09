package com.carambolos.carambolosapi.application.usecases;

import com.azure.storage.blob.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class AzureStorageService {

    private final BlobContainerClient containerClient;
    private final boolean configured;

    public AzureStorageService(@Value("${azure.storage.connection-string:}") String connectionString,
                               @Value("${azure.storage.container-name:}") String containerName) {
        if (connectionString == null || connectionString.trim().isEmpty() || 
            containerName == null || containerName.trim().isEmpty()) {
            this.containerClient = null;
            this.configured = false;
        } else {
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(connectionString)
                    .buildClient();
            this.containerClient = blobServiceClient.getBlobContainerClient(containerName);
            this.configured = true;
        }
    }

    public String upload(MultipartFile file) {
        if (!configured || containerClient == null) {
            throw new IllegalStateException("Azure Storage não configurado. Configure as propriedades azure.storage.connection-string e azure.storage.container-name no arquivo dev.env");
        }
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            BlobClient blobClient = containerClient.getBlobClient(fileName);
            try (InputStream is = file.getInputStream()) {
                blobClient.upload(is, file.getSize(), true);
            }
            return blobClient.getBlobUrl();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao fazer upload para o Azure Blob Storage", e);
        }
    }
}

