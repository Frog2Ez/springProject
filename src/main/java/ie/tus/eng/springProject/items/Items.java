package ie.tus.eng.springProject.items;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ITEMS")
public class Items {
    @Id
    @Column(name = "ID")
    private long id;
    
    @Column(name = "PRODUCT_NAME")
    private String productName;
    
    @Column(name = "CATEGORY_ID")
    private String categoryID;
    
    @Column(name = "DESCRIPTION")
    private String description;
    
    @Column(name = "LISTED_PRICE")
    private double listedPrice;
    
    @Column(name = "DISCOUNT")
    private double discount;
    
    @Column(name = "QUANTITY")
    private int quantity;
	
	
	public Items() {
		super();
	}
	
	public Items(long id, String productName, String categoryID, String description, double listedPrice, double discount, int quantity) {
		super();
		this.id = id;
		this.productName = productName;
		this.categoryID = categoryID;
		this.description = description;
		this.listedPrice = listedPrice;
		this.discount = discount;
		this.quantity = quantity;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(String categoryID) {
		this.categoryID = categoryID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getListedPrice() {
		return listedPrice;
	}

	public void setListedPrice(double listedPrice) {
		this.listedPrice = listedPrice;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	@Override
	public String toString() {
		return "Items [id=" + id + ", productName=" + productName + ", categoryID=" + categoryID + 
		       ", description=" + description + ", listedPrice=" + listedPrice + 
		       ", discount=" + discount + ", quantity=" + quantity + "]";
	}
}