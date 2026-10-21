package com.selecta.photo.photo.storage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.UUID;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LocalPhotoStorageService implements PhotoStorageService {

    private static final int PREVIEW_MAX_SIZE = 1600;
    private static final int THUMBNAIL_MAX_SIZE = 350;

    private final Path storageRoot;

    public LocalPhotoStorageService(@Value("${selecta.storage.root}") String storageRoot) {
        this.storageRoot = Paths.get(storageRoot);
    }

    @Override
    public StoredPhotoFiles store(Long albumId, MultipartFile file) throws IOException {
        validateImage(file);

        String id = UUID.randomUUID().toString();

        Path albumRoot = storageRoot.resolve("albums").resolve(albumId.toString());

        Path originals = albumRoot.resolve("originals");
        Path previews = albumRoot.resolve("previews");
        Path thumbnails = albumRoot.resolve("thumbnails");

        Files.createDirectories(originals);
        Files.createDirectories(previews);
        Files.createDirectories(thumbnails);

        String originalExtension = getExtension(file.getOriginalFilename());

        String originalFilename = id + originalExtension;
        String previewFilename = id + ".jpg";
        String thumbnailFilename = id + ".jpg";

        Path originalPath = originals.resolve(originalFilename);
        Path previewPath = previews.resolve(previewFilename);
        Path thumbnailPath = thumbnails.resolve(thumbnailFilename);

        file.transferTo(originalPath);

        BufferedImage originalImage = ImageIO.read(originalPath.toFile());

        if (originalImage == null) {
            Files.deleteIfExists(originalPath);
            throw new IllegalArgumentException("El archivo no es una imagen válida");
        }

        BufferedImage preview = resize(originalImage, PREVIEW_MAX_SIZE);

        addWatermark(preview);

        ImageIO.write(preview, "jpg", previewPath.toFile());

        BufferedImage thumbnail = resize(originalImage, THUMBNAIL_MAX_SIZE);

        ImageIO.write(thumbnail, "jpg", thumbnailPath.toFile());

        String originalKey = storageRoot.relativize(originalPath).toString();

        String previewKey = storageRoot.relativize(previewPath).toString();

        String thumbnailKey = storageRoot.relativize(thumbnailPath).toString();

        return new StoredPhotoFiles(originalKey, previewKey, thumbnailKey);
    }

    @Override
    public void delete(StoredPhotoFiles files) throws IOException {
        Files.deleteIfExists(storageRoot.resolve(files.originalKey()));

        Files.deleteIfExists(storageRoot.resolve(files.previewKey()));

        Files.deleteIfExists(storageRoot.resolve(files.thumbnailKey()));
    }

    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("La fotografía está vacía");
        }

        String contentType = file.getContentType();

        if (!"image/jpeg".equalsIgnoreCase(contentType) && !"image/png".equalsIgnoreCase(contentType)) {
            throw new IllegalArgumentException("Por ahora solo se permiten imágenes JPG y PNG");
        }
    }

    private BufferedImage resize(BufferedImage source, int maxSize) {
        int originalWidth = source.getWidth();
        int originalHeight = source.getHeight();

        double scale = Math.min(1.0, (double) maxSize / Math.max(originalWidth, originalHeight));

        int width = (int) Math.round(originalWidth * scale);

        int height = (int) Math.round(originalHeight * scale);

        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics = resized.createGraphics();

        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);

        graphics.drawImage(source, 0, 0, width, height, null);

        graphics.dispose();

        return resized;
    }

    private void addWatermark(BufferedImage image) {
        Graphics2D graphics = image.createGraphics();

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int fontSize = Math.max(22, image.getWidth() / 35);

        Font font = new Font(Font.SANS_SERIF, Font.BOLD, fontSize);

        graphics.setFont(font);

        String text = "SELECTA EVENTOS";

        FontMetrics metrics = graphics.getFontMetrics(font);

        int margin = Math.max(20, image.getWidth() / 40);

        int x = image.getWidth() - metrics.stringWidth(text) - margin;

        int y = image.getHeight() - margin;

        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.55f));

        graphics.setColor(Color.BLACK);

        graphics.drawString(text, x + 2, y + 2);

        graphics.setColor(Color.WHITE);

        graphics.drawString(text, x, y);

        graphics.dispose();
    }

    private String getExtension(String filename) {
        if (filename == null) {
            return ".jpg";
        }

        int dot = filename.lastIndexOf('.');

        if (dot < 0) {
            return ".jpg";
        }

        String extension = filename.substring(dot).toLowerCase();

        if (extension.equals(".jpg") || extension.equals(".jpeg") || extension.equals(".png")) {
            return extension;
        }

        return ".jpg";
    }

    @Override
    public Resource load(String key) {
        try {
            Path file = storageRoot.resolve(key).normalize();

            if (!file.startsWith(storageRoot.normalize())) {
                throw new IllegalArgumentException("Ruta de archivo inválida");
            }

            Resource resource = new UrlResource(file.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new IllegalArgumentException("La imagen no existe");
            }

            return resource;
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("No se pudo leer la imagen", e);
        }
    }
}
