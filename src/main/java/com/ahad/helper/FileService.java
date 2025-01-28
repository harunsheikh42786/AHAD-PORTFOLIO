package com.ahad.helper;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {

    // Base directory where images will be stored
    private static final String BASE_DIRECTORY = "C:/Users/91895/Pictures/AHAD-PORTFOLIO/";

    // Method to handle the image upload and save the file
    public static String uploadImage(String category, MultipartFile file) throws IOException {
        // Check if the file is empty
        if (file.isEmpty()) {
            throw new IOException("No file uploaded");
        }

        // Ensure category folder exists (User, Shop, Product)
        String categoryDirectory = BASE_DIRECTORY + category + "/";
        File categoryFolder = new File(categoryDirectory);
        if (!categoryFolder.exists()) {
            categoryFolder.mkdirs(); // Create the folder if it doesn't exist
        }

        // Generate a unique filename for the image
        @SuppressWarnings("null")
        String fileName = System.currentTimeMillis() + "_"
                + file.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-]", "_");

        // Path to save the image
        Path imagePath = Paths.get(categoryDirectory + fileName);

        // Transfer the file to the desired location
        Files.createDirectories(imagePath.getParent());
        file.transferTo(imagePath);

        // Return the relative path of the image
        return "/" + category + "/" + fileName;
    }

    public static void deleteFile(String fileName) {
        // Construct the full path of the file
        String filePath = "C:/Users/91895/Pictures/AHAD-PORTFOLIO/" + fileName;

        File file = new File(filePath);
        if (file.exists()) {
            // Delete the file if it exists
            if (file.delete()) {
                System.out.println("File deleted successfully: " + fileName);
            } else {
                System.out.println("Failed to delete the file: " + fileName);
            }
        } else {
            System.out.println("File not found: " + fileName);
        }
    }

}
