package ie.tus.eng.springProject.items.jdbc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ie.tus.eng.springProject.items.Items;

@Repository
public class ItemsJdbcRepository {

	@Autowired
	private JdbcTemplate springJdbcTemplate;

	private static String SELECT_QUERY = "SELECT * FROM ITEMS WHERE ID = ?";

	private static String INSERT_QUERY = "INSERT INTO ITEMS (ID, PRODUCT_NAME, CATEGORY_ID, DESCRIPTION, LISTED_PRICE, DISCOUNT, QUANTITY) VALUES(?, ?, ?, ?, ?, ?, ?)";

	private static String DELETE_QUERY = "DELETE FROM ITEMS WHERE ID = ?";

	private static String SELECT_ALL = "SELECT * FROM ITEMS";

	public void insert(Items items) {
		springJdbcTemplate.update(INSERT_QUERY,
				items.getId(), 
				items.getProductName(), 
				items.getCategoryID(), 
				items.getDescription(), 
				items.getListedPrice(), 
				items.getDiscount(), 
				items.getQuantity());
	}

	public int deleteById(long id) {
		return springJdbcTemplate.update(DELETE_QUERY, id);
	}

	public Items findById(long id) {
		return springJdbcTemplate.queryForObject(SELECT_QUERY, new BeanPropertyRowMapper<>(Items.class), id);
	}

	public List<Items> findAll() {
		return springJdbcTemplate.query(SELECT_ALL, new BeanPropertyRowMapper<>(Items.class));
	}
}