// API base URL - adjust this to match your Spring Boot server
const API_BASE_URL = 'http://localhost:8080';

// Function to calculate discounted price
function calculateDiscountedPrice(price, discount) {
    return (price * (1 - discount)).toFixed(2);
}

// Function to create a product card
function createProductCard(item) {
    const discountedPrice = calculateDiscountedPrice(item.listedPrice, item.discount);
    const discountPercentage = Math.round(item.discount * 100);

    return `
        <div class="product-card">
            <div class="product-type ${item.categoryID.toLowerCase()}">${item.categoryID}</div>
            ${item.discount > 0 ? `<div class="product-discount">-${discountPercentage}%</div>` : ''}
            <div class="product-image">
                <i class="fas ${item.categoryID === 'MOVIE' ? 'fa-film' : 'fa-gamepad'}"></i>
            </div>
            <h3 class="product-name">${item.productName}</h3>
            <p class="product-description">${item.description}</p>
            <div class="product-pricing">
                ${item.discount > 0 ? `<span class="original-price">€${item.listedPrice.toFixed(2)}</span>` : ''}
                <span class="discounted-price">€${discountedPrice}</span>
            </div>
            <div class="product-stock">
                ${item.quantity > 0 ? `<span class="in-stock"><i class="fas fa-check-circle"></i> ${item.quantity} in stock</span>` : '<span class="out-of-stock">Out of stock</span>'}
            </div>
            <button class="add-to-cart-btn" ${item.quantity === 0 ? 'disabled' : ''}>
                <i class="fas fa-shopping-cart"></i> Add to Cart
            </button>
        </div>
    `;
}

// Function to load products from the API
async function loadProducts() {
    const productsGrid = document.getElementById('products-grid');
    const loading = document.getElementById('loading');
    const error = document.getElementById('error');

    try {
        const response = await fetch(`${API_BASE_URL}/items`);

        if (!response.ok) {
            throw new Error('Failed to fetch products');
        }

        const items = await response.json();

        // Hide loading
        loading.style.display = 'none';

        if (items.length === 0) {
            productsGrid.innerHTML = '<p class="no-products">No products available at the moment.</p>';
            return;
        }

        // Create product cards
        productsGrid.innerHTML = items.map(item => createProductCard(item)).join('');

    } catch (err) {
        console.error('Error loading products:', err);
        loading.style.display = 'none';
        error.style.display = 'block';
    }
}

// Load products when the page loads
document.addEventListener('DOMContentLoaded', loadProducts);