package ie.tus.eng.springProject.items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import ie.tus.eng.springProject.items.jdbc.ItemsJdbcRepository;

@Component
public class ItemsCommandLineRunner implements CommandLineRunner {
	
	@Autowired
	private ItemsJdbcRepository repository;
	
	@Override
	public void run(String... args) throws Exception{
		// Movies
		repository.insert(new Items(1, "The Matrix", "MOVIE", "Sci-fi action film about simulated reality", 4.99, 0.10, 5));
		repository.insert(new Items(2, "Inception", "MOVIE", "Mind-bending thriller about dream invasion", 5.99, 0.15, 3));
		repository.insert(new Items(3, "The Godfather", "MOVIE", "Classic crime drama about a mafia family", 3.99, 0.05, 4));
		repository.insert(new Items(4, "Interstellar", "MOVIE", "Epic space exploration adventure", 5.99, 0.20, 2));
		
		// Games
		repository.insert(new Items(5, "The Last of Us", "GAME", "Post-apocalyptic action-adventure game", 6.99, 0.25, 3));
		repository.insert(new Items(6, "Red Dead Redemption 2", "GAME", "Open-world western adventure", 7.99, 0.15, 4));
		repository.insert(new Items(7, "God of War", "GAME", "Action-adventure game based on Norse mythology", 6.99, 0.20, 5));
		repository.insert(new Items(8, "Spider-Man", "GAME", "Superhero action-adventure game", 5.99, 0.10, 6));
	}
}