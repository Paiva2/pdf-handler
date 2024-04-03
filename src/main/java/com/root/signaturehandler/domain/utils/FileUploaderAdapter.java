package com.root.signaturehandler.domain.utils;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.root.signaturehandler.config.AwsConfig;
import com.root.signaturehandler.domain.interfaces.FileUploaderModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Component
@Setter
@Getter
public class FileUploaderAdapter implements FileUploaderModel {
    private final AwsConfig aws;
    private String documentsDestination;

    public FileUploaderAdapter(AwsConfig aws) {
        this.aws = aws;
    }

    @Override
    public String uploadDocument(MultipartFile file, String newFileName) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());

        this.aws.amazonS3().putObject(
                this.documentsDestination,
                newFileName,
                file.getInputStream(),
                objectMetadata
        );

        return this.mountDocumentUrl(this.documentsDestination, newFileName);
    }

    private String mountDocumentUrl(String destination, String fileName) {
        try {
            return new URI(
                    null,
                    null,
                    "https://" + destination + ".s3.us-east-1.amazonaws.com/" + fileName,
                    null
            ).toASCIIString();
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
