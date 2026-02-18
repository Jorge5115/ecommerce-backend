package com.jorge.ecommerce.controller;

import com.cloudinary.utils.ObjectUtils;
import com.jorge.ecommerce.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@Tag(name = "Images", description = "Endpoints de subida de imagenes")
public class ImageController {

    private final ImageService imageService;

    @Operation(summary = "Subir imagen de producto")
    @PostMapping("/upload/product")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> uploadProductImage(
            @RequestParam("file") MultipartFile file) {
        try {
            String url = imageService.uploadImage(file, "products");
            return ResponseEntity.ok(Map.of("url", url));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error al subir la imagen"));
        }
    }

    @Operation(summary = "Subir imagen de categoria")
    @PostMapping("/upload/category")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> uploadCategoryImage(
            @RequestParam("file") MultipartFile file) {
        try {
            String url = imageService.uploadImage(file, "categories");
            return ResponseEntity.ok(Map.of("url", url));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error al subir la imagen"));
        }
    }

    @Operation(summary = "Obtener imagenes de la galeria")
    @GetMapping("/list/{folder}")
    public ResponseEntity<List<String>> getGallery(@PathVariable String folder) {
        try {
            List<String> urls = imageService.getGalleryImages(folder);
            return ResponseEntity.ok(urls);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}