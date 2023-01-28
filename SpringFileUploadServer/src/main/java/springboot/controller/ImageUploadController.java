package springboot.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import springboot.model.ImageUpload;
import springboot.model.UploadResponse;
import springboot.service.ImageUploadService;

@RestController
public class ImageUploadController {
	@Autowired
	ImageUploadService iService;
	
	@PostMapping("/upload")
	public ResponseEntity<UploadResponse> upload(@RequestParam("image") MultipartFile image){
		try {
			iService.save(image);
			return ResponseEntity.status(HttpStatus.OK)
					.body(new UploadResponse("File Uploaded successfully"+image.getOriginalFilename()));
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).
					body(new UploadResponse("File Not Uploaded Successfully "
			+image.getOriginalFilename()+e.getMessage()));
		}
	}
	
	@GetMapping("/getallfiles")
	public ResponseEntity<List<ImageUpload>> getAllFiles(){
		List<ImageUpload> list=iService.loadAll().map(
				p->{
					String name=p.getFileName().toString();
					String path=MvcUriComponentsBuilder
													.fromMethodName(getClass(),"getfile",p.getFileName().toString())
													.build().toString();
					return new ImageUpload(name, path);
													
				}).collect(Collectors.toList());
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}
	
	@GetMapping("/getfile/{name:.+}")
	public ResponseEntity<Resource>getFile(@PathVariable("name") String name)
	{
		Resource resource=iService.load(name);
		return ResponseEntity.status(HttpStatus.OK)
				.header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+
																																resource.getFilename()+"\"").body(resource);																					
	}

}
