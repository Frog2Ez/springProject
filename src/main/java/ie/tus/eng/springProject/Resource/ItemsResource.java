package ie.tus.eng.springProject.Resource;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ie.tus.eng.springProject.items.Items;
import ie.tus.eng.springProject.items.jdbc.ItemsJdbcRepository;

@RestController
public class ItemsResource {
	private ItemsJdbcRepository repository;
	
	@Autowired
	public ItemsResource(ItemsJdbcRepository repository) {
		this.repository = repository;
	}
	
	@GetMapping("/items")
	public List<Items> retrieveAllItems() {
		return repository.findAll();
	}
	
	@GetMapping("/items/{id}")
	public Items retrieveItem(@PathVariable long id) {
		Items item = repository.findById(id);
		return item;
	}
	
	@DeleteMapping("/items/{id}")
	public ResponseEntity<Void> deleteItem(@PathVariable long id) {
		if (repository.deleteById(id) > 0) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping("/items")
	public ResponseEntity<Items> createItem(@RequestBody Items item) {
		repository.insert(item);
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(item.getId())
				.toUri();
		
		return ResponseEntity.created(location).build();
	}
}