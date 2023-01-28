package springboot.repo;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import springboot.model.Image;


@Repository
public interface FileRepo extends CrudRepository<Image, Long>{
	
	public Optional<Image>  findByname(String name);

}
