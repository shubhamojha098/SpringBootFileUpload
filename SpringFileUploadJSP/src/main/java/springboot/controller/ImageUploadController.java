package springboot.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import springboot.model.ImageUpload;
import springboot.model.UploadResponse;
import springboot.service.ImageUploadService;

@Controller
public class ImageUploadController {
	@Autowired
	ImageUploadService iService;
	
	@GetMapping("/")
	public String home() {
		return "form";
	}
	
	
	  @PostMapping("/upload") 
	  public ModelAndView upload(@RequestBody MultipartFile image)
	  { 
		  try 
		  { 
			  ModelAndView mv=new ModelAndView("form");
			  	iService.save(image);
			  	mv.addObject("msg", "File Uploaded Successfully");
			  	return mv;
	  
	  } catch (Exception e)
		  { // TODO: handle exception return
		  ModelAndView mv=new ModelAndView("form");
		  mv.addObject("msg", "File not Uploaded Successfully"+e.getMessage());
		  	return mv;
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
