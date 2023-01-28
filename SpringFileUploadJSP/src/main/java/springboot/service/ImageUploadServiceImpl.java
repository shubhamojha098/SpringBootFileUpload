package springboot.service;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageUploadServiceImpl implements ImageUploadService {
	
	private final Path path=Paths.get("uploadfile");
	

	@Override
	public void init() {
		// TODO Auto-generated method stub
		try {
			Files.createDirectories(path);
		} catch (Exception e) {
			throw new RuntimeException("Cant create upload directory");
			// TODO: handle exception
		}
		
	}

	@Override
	public void save(MultipartFile file) {
		// TODO Auto-generated method stub
		try {
			Files.copy(file.getInputStream(),this.path.resolve(file.getOriginalFilename()));
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException("File Already Exixts"+e.getMessage());
		}
	}

	@Override
	public Resource load(String filename) {
		// TODO Auto-generated method stub
		try {
			Path p=path.resolve(filename);
			Resource resource=new UrlResource(p.toUri());
			if(resource.exists()&&resource.isReadable())
			{
				return resource;
			}else {
				throw new RuntimeException("Cant't Read File");
			}
		} catch (MalformedURLException e) {
			// TODO: handle exception
			throw new RuntimeException("Error"+e.getMessage());
		}
		
	}

	@Override
	public Stream<Path> loadAll() {
		// TODO Auto-generated method stub
		try {
			return Files.walk(path,1).filter(p->!p.equals(this.path)).map(this.path::relativize);
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException("Error in load"+e.getMessage());
		}
	}

}
