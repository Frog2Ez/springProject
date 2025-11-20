const $ = (selector) => document.querySelector(selector);
const $$ = (selector) => document.querySelectorAll(selector);

// API base URL - use relative base when static files are served from the Spring Boot app
const API_BASE_URL = '';

// Current items loaded from server (used to wire add-to-cart handlers)
let currentItems = [];
let cart = [];

const updateCartDisplay = function () {
    const cartCount = $('#cart-count');
    const cartTotal = $('#cart-total');
    if (!cartCount || !cartTotal) return;

    const totalItems = cart.reduce((sum, item) => sum + item.quantity, 0);
    const totalPrice = cart.reduce((sum, item) => sum + (item.price * item.quantity), 0);

    cartCount.textContent = `(${totalItems})`;
    cartTotal.textContent = `€${totalPrice.toFixed(2)}`;
};

const addToCartById = function (id) {
    const item = currentItems.find(i => String(i.id) === String(id));
    if (!item) return;

    const price = parseFloat(calculateDiscountedPrice(Number(item.listedPrice || 0), Number(item.discount || 0)));
    const existing = cart.find(c => c.id === item.id);
    if (existing) existing.quantity += 1;
    else cart.push({ id: item.id, name: item.productName || 'Product', price, quantity: 1 });

    updateCartDisplay();
};

const calculateDiscountedPrice = function (price, discount) {
    const p = Number(price) || 0;
    const d = Number(discount) || 0;
    return (p * (1 - d)).toFixed(2);
};

const createProductCard = function (item) {
    const categoryRaw = item.categoryID ?? 'PRODUCT';
    const category = String(categoryRaw);
    const categoryClass = category.toLowerCase();

    const price = (typeof item.listedPrice === 'number') ? item.listedPrice : parseFloat(item.listedPrice) || 0;
    const discount = (typeof item.discount === 'number') ? item.discount : parseFloat(item.discount) || 0;
    const quantity = Number.isInteger(item.quantity) ? item.quantity : parseInt(item.quantity) || 0;

    const discountedPrice = calculateDiscountedPrice(price, discount);
    const discountPercentage = Math.round(discount * 100);

    const iconClass = (category.toUpperCase() === 'MOVIE') ? 'fa-film' : 'fa-gamepad';
    const productName = item.productName || 'Unnamed product';
    const description = item.description || '';

    return `
        <div class="product-card">
            <div class="product-type ${categoryClass}">${category}</div>
            ${discount > 0 ? `<div class="product-discount">-${discountPercentage}%</div>` : ''}
            <div class="product-image">
                <i class="fas ${iconClass}"></i>
            </div>
            <h3 class="product-name">${productName}</h3>
            <p class="product-description">${description}</p>
            <div class="product-pricing">
                ${discount > 0 ? `<span class="original-price">€${price.toFixed(2)}</span>` : ''}
                <span class="discounted-price">€${discountedPrice}</span>
            </div>
            <div class="product-stock">
                ${quantity > 0 ? `<span class="in-stock"><i class="fas fa-check-circle"></i> ${quantity} in stock</span>` : '<span class="out-of-stock">Out of stock</span>'}
            </div>
            <button class="add-to-cart-btn" data-id="${item.id}" ${quantity === 0 ? 'disabled' : ''}>
                <i class="fas fa-shopping-cart"></i> Add to Cart
            </button>
        </div>
    `;
};

const renderProducts = function (items) {
    const productsGrid = $('#products-grid');
    const loading = $('#loading');
    const error = $('#error');

    currentItems = items || [];

    if (loading) loading.style.display = 'none';
    if (!productsGrid) return;

    if (!currentItems || currentItems.length === 0) {
        productsGrid.innerHTML = '<p class="no-products">No products available at the moment.</p>';
        return;
    }

    productsGrid.innerHTML = currentItems.map(item => createProductCard(item)).join('');

    // Wire add-to-cart buttons
    $$('.add-to-cart-btn').forEach(btn => {
        btn.addEventListener('click', function () {
            const id = btn.dataset.id;
            addToCartById(id);
        });
    });
};

const buildSearchUrl = function (params) {
    if (!params || Object.keys(params).length === 0) return `${API_BASE_URL}/items`;
    const query = Object.entries(params)
        .filter(([, v]) => v !== undefined && v !== null && String(v).trim() !== '')
        .map(([k, v]) => `${encodeURIComponent(k)}=${encodeURIComponent(String(v))}`)
        .join('&');
    if (!query) return `${API_BASE_URL}/items`;
    return `${API_BASE_URL}/items/search?${query}`;
};

const loadProducts = function (params) {
    const loading = $('#loading');
    const error = $('#error');

    if (loading) loading.style.display = 'block';
    if (error) error.style.display = 'none';

    const url = buildSearchUrl(params);

    fetch(url)
        .then((response) => response.json())
        .then((items) => {
            renderProducts(items);
            console.log('Loaded items from API:', items);
        })
        .catch((err) => {
            console.error('Error loading products:', err);
            if (loading) loading.style.display = 'none';
            if (error) error.style.display = 'block';
        });
};

document.addEventListener('DOMContentLoaded', function () {
    // Initial load
    loadProducts();

    const searchInput = $('#search-input');
    const searchButton = $('#search-button');

    const doSearch = function (q) {
        const trimmed = q ? String(q).trim() : '';
        if (!trimmed) {
            loadProducts();
        } else {
            // Use productName param to search; backend also filters categoryID and description
            loadProducts({ productName: trimmed });
        }
    };

    if (searchInput) {
        searchInput.addEventListener('keyup', function (e) {
            if (e.key === 'Enter') {
                doSearch(searchInput.value);
            }
        });
    }

    if (searchButton) {
        searchButton.addEventListener('click', function () {
            doSearch(searchInput ? searchInput.value : '');
        });
    }
});