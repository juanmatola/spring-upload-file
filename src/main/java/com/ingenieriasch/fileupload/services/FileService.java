package com.ingenieriasch.fileupload.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ingenieriasch.fileupload.exceptions.InvalidContentTypeException;
import com.ingenieriasch.fileupload.exceptions.MyFileNotFoundException;
import com.ingenieriasch.fileupload.model.File;
import com.ingenieriasch.fileupload.model.ResourceDto;
import com.ingenieriasch.fileupload.model.repositories.ImageRepository;
import com.ingenieriasch.fileupload.utils.FileUtils;

@Service
public class FileService {

	private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList("image/png", "image/jpeg", "image/jpg",
			"application/pdf");
	
	@Value(value = "${files.upload-folder:uploaded-files}")
	private String UPLOAD_FOLDER;
	
	private Path UPLOAD_PATH;
	
	@Autowired
	private ImageRepository imageRepository;

	@PostConstruct
	private void createUploadFolder() throws IOException {

		UPLOAD_PATH = Paths.get(UPLOAD_FOLDER);
		
		if (!Files.exists(UPLOAD_PATH)) {
			Files.createDirectories(UPLOAD_PATH);
		}

	}
	
	public List<File> loadAll() {
		
		return imageRepository.findAll();
		
	}

	public File upload(MultipartFile resourceFile) throws IOException, InvalidContentTypeException {

		validateContentType(resourceFile.getContentType());

		String filename = resourceFile.getOriginalFilename();
		String extension = filename.substring(filename.lastIndexOf("."));

		File file = new File();
		file.setNombreOriginal(filename);
		file.setContentType(resourceFile.getContentType());
		file = imageRepository.save(file);

		try (InputStream inputStream = resourceFile.getInputStream()) {

			Path filePath = UPLOAD_PATH.resolve(file.getId().concat(extension));
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

		} catch (IOException ioe) {

			imageRepository.delete(file);
			throw new IOException("No pudo guardarse el archivo: " + resourceFile.getOriginalFilename(), ioe);

		}

		return file;

	}

	public ResourceDto loadFileAsResource(String fileId) throws MyFileNotFoundException {
		try {

			File file = loadFile(fileId);
			String extension = getExtension(file);

			Path filePath = UPLOAD_PATH.resolve(fileId.concat(extension)).normalize();
			Resource resource = new UrlResource(filePath.toUri());

			if (resource.exists()) {
				ResourceDto dto = new ResourceDto();
				dto.setContentType(file.getContentType());
				dto.setResource(resource);

				return dto;
			} else {
				throw new MyFileNotFoundException("File not found " + fileId);
			}

		} catch (MalformedURLException ex) {

			throw new MyFileNotFoundException("File not found " + fileId, ex);

		}

	}

	public File loadFile(String id) throws MyFileNotFoundException {

		return imageRepository.findById(id).orElseThrow(() -> new MyFileNotFoundException("File not found " + id));

	}

	private void validateContentType(String contentType) throws InvalidContentTypeException {

		if (!ALLOWED_CONTENT_TYPES.contains(contentType)) {
			throw new InvalidContentTypeException("Extensión de archivo invalida");
		}

	}

	private String getExtension(File file) {

		return FileUtils.EXTENSIONS.get(file.getContentType());

	}

}
