package com.sample.chat.service;

import com.google.cloud.storage.*;
import com.sample.chat.mapper.AttachmentMapper;
import com.sample.chat.repository.AttachmentRepository;
import com.sample.chat.entity.Attachment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttachmentService {
    private final AttachmentRepository repository;
    //private final GoogleCredentials credentials;
    private final Storage storage;

    @Value("${gcp.bucket}")
    private String bucketName;

    @Transactional
    public Attachment createAttachment(MultipartFile attachmentFile) throws IOException {
        Blob file = uploadObject(attachmentFile);
        return repository.save(AttachmentMapper.toAttachment(file));
    }

    public Blob uploadObject(MultipartFile file) throws IOException {
        log.info(storage.getOptions().getProjectId());
        BlobId blobId = BlobId.of(bucketName, file.getOriginalFilename());
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        return storage.create(blobInfo, file.getBytes());
    }
}
