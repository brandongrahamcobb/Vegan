public interface FileStorageService {
    /**
     * Store the incoming stream under the given filename (must be unique or renamed),
     * and return a public URL (e.g. “/uploads/…”).
     */
    String store(String filename, InputStream data);

    // Add a method to generate a unique filename based on the original filename
    String generateUniqueFilename(String originalFilename);

    // Add a method to get the public URL for a file
    String getPublicUrl(String filename);
}
