package springboot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.Resource;
import springboot.service.ImageUploadService;

@SpringBootApplication
public class SpringFileUploadServerApplication implements CommandLineRunner {
		@Resource
		ImageUploadService iService;
	public static void main(String[] args) {
		SpringApplication.run(SpringFileUploadServerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		iService.init();
		
	}

}
