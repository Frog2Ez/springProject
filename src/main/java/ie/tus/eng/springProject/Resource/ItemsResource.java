package ie.tus.eng.springProject.Resource;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ie.tus.eng.springProject.items.Items;
import ie.tus.eng.springProject.items.jdbc.ItemsJdbcRepository;

@CrossOrigin(origins = "*")
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
	public ResponseEntity<Items> retrieveItem(@PathVariable long id) {
		try {
			Items item = repository.findById(id);
			return ResponseEntity.ok(item);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/items")
	public ResponseEntity<Items> createItem(@RequestBody Items item) {
		repository.insert(item);

		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(item.getId())
				.toUri();

		return ResponseEntity.created(location).body(item);
	}

	@PutMapping("/items/{id}")
	public ResponseEntity<Items> editItem(@PathVariable long id, @RequestBody Items item) {
		try {
			Items existingItem = repository.findById(id);
			if (existingItem == null) {
				return ResponseEntity.notFound().build();
			}
			item.setId(id);
			repository.update(item);
			return ResponseEntity.ok(item);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/items/{id}")
	public ResponseEntity<Void> deleteItem(@PathVariable long id) {
		if (repository.deleteById(id) > 0) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping("/items/search")
	public List<Items> search(@RequestParam(required = false) String productName,
			@RequestParam(required = false) String categoryID,
			@RequestParam(required = false) String description) {
		// If no parameters provided, return all items
		if (productName == null && categoryID == null && description == null) {
			return repository.findAll();
		}

		return repository.findAll().stream()
				.filter(i -> productName == null || i.getProductName() != null
						&& i.getProductName().toLowerCase().contains(productName.toLowerCase()))
				.filter(i -> categoryID == null || i.getCategoryID() != null
						&& i.getCategoryID().toLowerCase().contains(categoryID.toLowerCase()))
				.filter(i -> description == null || i.getDescription() != null
						&& i.getDescription().toLowerCase().contains(description.toLowerCase()))
				.collect(java.util.stream.Collectors.toList());
	}
}