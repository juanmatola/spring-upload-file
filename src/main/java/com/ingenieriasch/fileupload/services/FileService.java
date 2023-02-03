package com.ingenieriasch.fileupload.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ingenieriasch.fileupload.exceptions.InvalidContentTypeException;
import com.ingenieriasch.fileupload.web.payload.UploadResponse;

@Service
public class FileService {

	private final Path UPLOAD_PATH = Paths.get("uploaded-files");
	private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList("image/png", "image/jpeg", "image/jpg",
			"application/pdf");

	@PostConstruct
	// En el arranque crea carpeta ./uploaded-files si no existe
	private void createUploadFolder() throws IOException {

		if (!Files.exists(UPLOAD_PATH)) {
			Files.createDirectories(UPLOAD_PATH);
		}

	}

	public UploadResponse fileUpload(MultipartFile file) throws IOException, InvalidContentTypeException {

		validateContentType(file.getContentType());

		String randomFileName = UUID.randomUUID().toString();

		try (InputStream inputStream = file.getInputStream()) {

			Path filePath = UPLOAD_PATH.resolve(randomFileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

		} catch (IOException ioe) {

			throw new IOException("No pudo guardarte el archivo: " + file.getOriginalFilename(), ioe);

		}

		UploadResponse res = new UploadResponse();
		res.setUrl("/files/".concat(randomFileName));
		res.setContentType(file.getContentType());
		res.setSize(file.getSize());

		return res;

	}

	private void validateContentType(String contentType) throws InvalidContentTypeException {

		if (!ALLOWED_CONTENT_TYPES.contains(contentType)) {
			throw new InvalidContentTypeException("Extensión de archivo invalida");
		}
	}

}
