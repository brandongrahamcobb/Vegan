package com.brandongcobb.vegan.store.service;

import java.io.InputStream;

public interface FileStorageService {
    String store(String filename, InputStream data);
    String generateUniqueFilename(String originalFilename);
    String getPublicUrl(String filename);
}
