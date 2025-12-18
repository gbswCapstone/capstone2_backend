package Capstone.capstoneProject.service;

import Capstone.capstoneProject.exceptions.serverError.ImageDeleteException;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Blob;
import com.google.firebase.cloud.StorageClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseStorageService {

    @Value("${firebase.storage.bucket}")
    private String bucketName;

    private final Storage storage;

    public String uploadImage(MultipartFile file, String dir) {
        try {
            String fileName = dir + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

            Bucket bucket = StorageClient.getInstance().bucket(bucketName);
            Blob blob = bucket.create(
                    fileName,
                    file.getInputStream(),
                    file.getContentType()
            );


            return String.format(
                    "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                    bucket.getName(),
                    URLEncoder.encode(fileName, StandardCharsets.UTF_8)
            );

        } catch (IOException e) {
            throw new RuntimeException("Firebase 이미지 업로드 실패", e);
        }
    }

    public void deleteImage(String imageUrl) {
        try {
            String filePath = extractFilePathFromUrl(imageUrl);

            BlobId blobId = BlobId.of(bucketName, filePath);
            boolean deleted = storage.delete(blobId);

            if (!deleted) {
                log.warn("이미지가 존재하지 않음: {}", imageUrl);
            }
        } catch (Exception e) {
            log.error("이미지 삭제 실패: {}", imageUrl, e);
            throw new ImageDeleteException("이미지 삭제에 실패했습니다.");
        }
    }

    // url 파싱 처리
    private String extractFilePathFromUrl(String imageUrl) {
        try {
            String decodedUrl = URLDecoder.decode(imageUrl, StandardCharsets.UTF_8);

            int index = decodedUrl.indexOf("/o/");
            if (index == -1) {
                throw new IllegalArgumentException("Firebase Storage URL 아님");
            }

            String pathWithParams = decodedUrl.substring(index + 3);
            int queryIndex = pathWithParams.indexOf("?");

            return queryIndex == -1
                    ? pathWithParams
                    : pathWithParams.substring(0, queryIndex);

        } catch (Exception e) {
            throw new IllegalArgumentException("이미지 URL 파싱 실패");
        }
    }
}
