package springboot.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import springboot.model.Image;
import springboot.model.UploadResponse;
import springboot.repo.FileRepo;
import springboot.util.ImageUtility;

@RestController
public class FileController {

	@Autowired
	FileRepo fRepo;

	@PostMapping("/upload")
	public ResponseEntity<UploadResponse> uploadImage(@RequestParam("image") MultipartFile image) throws IOException {
		Image img = new Image();
		img.setName(image.getOriginalFilename());
		img.setType(image.getContentType());
		//compressed image
		img.setImage(ImageUtility.compressedImage(image.getBytes()));
//save image to database
		fRepo.save(img);
		UploadResponse ur = new UploadResponse();
		ur.setMessage("Image Upload Successfully" + image.getOriginalFilename());
		return ResponseEntity.status(HttpStatus.OK).body(ur);
	}
	
	@GetMapping("image/info/{name}")
	public Image getImageInfo(@PathVariable("name") String name) {
		Optional<Image> optional=fRepo.findByname(name);
		Image img=optional.get();
		img.setImage(ImageUtility.decompressedImage(img.getImage()));
		return img;
	}
	
	@GetMapping("image/{name}")
	public ResponseEntity<byte[]> getImage(@PathVariable("name") String name) {
		Optional<Image> optional=fRepo.findByname(name);
		Image img=optional.get();
		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.valueOf(img.getType()))
				.body(ImageUtility.decompressedImage(img.getImage()));
	}
	

}
