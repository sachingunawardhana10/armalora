document.addEventListener("DOMContentLoaded", () => {


    /* ========================================
       Mock Product Data
       ======================================== */

    const products = [

        {
            id: 1,
            name: "Signature Oversized Shirt",
            category: "essentials",
            categoryName: "Essentials",
            price: 8500,
            badge: "",
            featured: true
        },

        {
            id: 2,
            name: "Embroidered Linen Shirt",
            category: "handcrafted",
            categoryName: "Handcrafted",
            price: 12500,
            badge: "Handcrafted",
            featured: true
        },

        {
            id: 3,
            name: "Relaxed Cotton Trousers",
            category: "new-arrivals",
            categoryName: "New Arrivals",
            price: 10900,
            badge: "New",
            featured: true
        },

        {
            id: 4,
            name: "Armalora Classic Tee",
            category: "essentials",
            categoryName: "Essentials",
            price: 5900,
            badge: "",
            featured: false
        },

        {
            id: 5,
            name: "Hand Embroidered Overshirt",
            category: "handcrafted",
            categoryName: "Handcrafted",
            price: 15900,
            badge: "Handcrafted",
            featured: true
        },

        {
            id: 6,
            name: "Relaxed Linen Trousers",
            category: "new-arrivals",
            categoryName: "New Arrivals",
            price: 11900,
            badge: "New",
            featured: false
        },

        {
            id: 7,
            name: "Essential Oxford Shirt",
            category: "essentials",
            categoryName: "Essentials",
            price: 7900,
            badge: "",
            featured: true
        },

        {
            id: 8,
            name: "Signature Embroidered Kurta",
            category: "handcrafted",
            categoryName: "Handcrafted",
            price: 14500,
            badge: "Limited",
            featured: false
        }

    ];


    /* ========================================
       DOM Elements
       ======================================== */

    const productGrid =
        document.getElementById("productGrid");

    const productCount =
        document.getElementById("productCount");

    const emptyProducts =
        document.getElementById("emptyProducts");

    const sortSelect =
        document.getElementById("sortSelect");

    const categoryButtons =
        document.querySelectorAll(".category-button");


    let currentCategory = "all";


    /* ========================================
       Format Price
       ======================================== */

    function formatPrice(price) {

        return new Intl.NumberFormat(
            "en-LK"
        ).format(price);

    }


    /* ========================================
       Render Products
       ======================================== */

    function renderProducts(productList) {

        productGrid.innerHTML = "";


        if (productList.length === 0) {

            emptyProducts.classList.add("visible");

            productCount.textContent =
                "Showing 0 products";

            return;
        }


        emptyProducts.classList.remove("visible");


        productCount.textContent =
            `Showing ${productList.length} products`;


        productList.forEach((product) => {

            const article =
                document.createElement("article");

            article.className =
                "catalog-product-card";


            const badgeHTML =
                product.badge
                    ? `
                        <span class="product-badge">
                            ${product.badge}
                        </span>
                    `
                    : "";


            article.innerHTML = `

                <div class="catalog-product-image">

                    ${badgeHTML}

                    <button
                        type="button"
                        class="product-wishlist"
                        aria-label="Add ${product.name} to wishlist"
                        data-product-id="${product.id}"
                    >
                        ♡
                    </button>

                    <span>
                        Product Image
                    </span>

                </div>


                <div class="catalog-product-info">

                    <p class="catalog-product-category">
                        ${product.categoryName}
                    </p>

                    <h2 class="catalog-product-name">
                        ${product.name}
                    </h2>

                    <p class="catalog-product-price">
                        LKR ${formatPrice(product.price)}
                    </p>

                </div>

            `;
            article.addEventListener(
    "click",
    (event) => {

        if (
            event.target.closest(
                ".product-wishlist"
            )
        ) {
            return;
        }

        window.location.href =
            `product-details.html?id=${product.id}`;

    }
);

            productGrid.appendChild(article);

        });


        attachWishlistEvents();

    }


    /* ========================================
       Filter Products
       ======================================== */

    function filterProducts() {

        if (currentCategory === "all") {

            return [...products];

        }


        return products.filter(
            (product) =>
                product.category === currentCategory
        );

    }


    /* ========================================
       Sort Products
       ======================================== */

    function sortProducts(productList) {

        const sortValue =
            sortSelect.value;


        const sorted =
            [...productList];


        switch (sortValue) {

            case "price-low":

                sorted.sort(
                    (a, b) =>
                        a.price - b.price
                );

                break;


            case "price-high":

                sorted.sort(
                    (a, b) =>
                        b.price - a.price
                );

                break;


            case "name":

                sorted.sort(
                    (a, b) =>
                        a.name.localeCompare(
                            b.name
                        )
                );

                break;


            case "featured":

                sorted.sort(
                    (a, b) =>
                        Number(b.featured) -
                        Number(a.featured)
                );

                break;

        }


        return sorted;

    }


    /* ========================================
       Update Catalog
       ======================================== */

    function updateCatalog() {

        const filtered =
            filterProducts();


        const sorted =
            sortProducts(filtered);


        renderProducts(sorted);

    }


    /* ========================================
       Category Buttons
       ======================================== */

    categoryButtons.forEach((button) => {

        button.addEventListener(
            "click",
            () => {

                categoryButtons.forEach(
                    (item) => {
                        item.classList.remove(
                            "active"
                        );
                    }
                );


                button.classList.add(
                    "active"
                );


                currentCategory =
                    button.dataset.category;


                updateCatalog();

            }
        );

    });


    /* ========================================
       Sort
       ======================================== */

    sortSelect.addEventListener(
        "change",
        updateCatalog
    );


    /* ========================================
       Wishlist
       ======================================== */

    function attachWishlistEvents() {

        const wishlistButtons =
            document.querySelectorAll(
                ".product-wishlist"
            );


        wishlistButtons.forEach(
            (button) => {

                button.addEventListener(
                    "click",
                    () => {

                        button.classList.toggle(
                            "active"
                        );


                        const isActive =
                            button.classList.contains(
                                "active"
                            );


                        button.textContent =
                            isActive
                                ? "♥"
                                : "♡";

                    }
                );

            }
        );

    }


    /* ========================================
       Initial Render
       ======================================== */

    updateCatalog();

});