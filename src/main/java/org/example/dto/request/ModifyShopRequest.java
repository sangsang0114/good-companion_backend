package org.example.dto.request;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ModifyShopRequest(String shopId, String phone, String boast, String info, String businessHours,
                                List<MultipartFile> newFiles, List<String> deletedFiles) {
}
