document.addEventListener("DOMContentLoaded", () => {

    /* ========================================
       Mock Product Data
       ======================================== */

    const products = [

        {
            id: 1,
            name: "Signature Oversized Shirt",
            category: "Essentials",
            price: 8500,
            badge: "",
            description:
                "A relaxed everyday shirt designed with a clean silhouette and understated Armalora detailing.",
            sizes: ["S", "M", "L", "XL"]
        },

        {
            id: 2,
            name: "Embroidered Linen Shirt",
            category: "Handcrafted",
            price: 12500,
            badge: "Handcrafted",
            description:
                "A lightweight linen shirt featuring carefully crafted embroidery inspired by contemporary Sri Lankan design.",
            sizes: ["S", "M", "L", "XL"]
        },

        {
            id: 3,
            name: "Relaxed Cotton Trousers",
            category: "New Arrivals",
            price: 10900,
            badge: "New",
            description:
                "Relaxed cotton trousers created for effortless everyday styling and comfortable movement.",
            sizes: ["S", "M", "L", "XL"]
        },

        {
            id: 4,
            name: "Armalora Classic Tee",
            category: "Essentials",
            price: 5900,
            badge: "",
            description:
                "A refined everyday essential with a comfortable fit and minimal Armalora identity.",
            sizes: ["S", "M", "L", "XL"]
        },

        {
            id: 5,
            name: "Hand Embroidered Overshirt",
            category: "Handcrafted",
            price: 15900,
            badge: "Handcrafted",
            description:
                "A statement overshirt combining contemporary tailoring with detailed hand embroidery.",
            sizes: ["S", "M", "L", "XL"]
        },

        {
            id: 6,
            name: "Relaxed Linen Trousers",
            category: "New Arrivals",
            price: 11900,
            badge: "New",
            description:
                "Lightweight linen trousers designed with a relaxed silhouette for warm-weather dressing.",
            sizes: ["S", "M", "L", "XL"]
        },

        {
            id: 7,
            name: "Essential Oxford Shirt",
            category: "Essentials",
            price: 7900,
            badge: "",
            description:
                "A timeless Oxford shirt designed to become a versatile part of your everyday wardrobe.",
            sizes: ["S", "M", "L", "XL"]
        },

        {
            id: 8,
            name: "Signature Embroidered Kurta",
            category: "Handcrafted",
            price: 14500,
            badge: "Limited",
            description:
                "A contemporary kurta featuring distinctive embroidery and a relaxed modern silhouette.",
            sizes: ["S", "M", "L", "XL"]
        }

    ];


    /* ========================================
       Get Product ID
       ======================================== */

    const params =
        new URLSearchParams(
            window.location.search
        );

    const productId =
        Number(params.get("id")) || 1;


    const product =
        products.find(
            item => item.id === productId
        );


    /* ========================================
       Elements
       ======================================== */

    const productCategory =
        document.getElementById("productCategory");

    const productName =
        document.getElementById("productName");

    const productPrice =
        document.getElementById("productPrice");

    const productDescription =
        document.getElementById("productDescription");

    const productImage =
        document.getElementById("productImage");

    const productBadge =
        document.getElementById("productBadge");

    const sizeOptions =
        document.getElementById("sizeOptions");

    const quantityValue =
        document.getElementById("quantityValue");

    const addToCartButton =
        document.getElementById("addToCartButton");

    const wishlistButton =
        document.getElementById("wishlistButton");

    const breadcrumbName =
        document.getElementById("breadcrumbName");


    /* ========================================
       Product Not Found
       ======================================== */

    if (!product) {

        document.body.innerHTML = `
            <main class="section">
                <div class="container">
                    <h1>Product not found</h1>

                    <p>
                        The product you are looking for
                        does not exist.
                    </p>

                    <a href="products.html">
                        Back to Shop
                    </a>
                </div>
            </main>
        `;

        return;
    }


    /* ========================================
       Format Price
       ======================================== */

    function formatPrice(price) {

        return new Intl.NumberFormat(
            "en-LK"
        ).format(price);

    }


    /* ========================================
       Render Product
       ======================================== */

    productCategory.textContent =
        product.category;

    productName.textContent =
        product.name;

    productPrice.textContent =
        `LKR ${formatPrice(product.price)}`;

    productDescription.textContent =
        product.description;

    breadcrumbName.textContent =
        product.name;

    productImage.textContent =
        "Product Image";


    /* ========================================
       Badge
       ======================================== */

    if (product.badge) {

        productBadge.textContent =
            product.badge;

        productBadge.hidden = false;

    } else {

        productBadge.hidden = true;

    }


    /* ========================================
       Render Sizes
       ======================================== */

    product.sizes.forEach(
        (size, index) => {

            const button =
                document.createElement("button");

            button.type = "button";

            button.className =
                "size-button";

            button.textContent =
                size;

            button.dataset.size =
                size;


            if (index === 1) {

                button.classList.add(
                    "active"
                );

            }


            button.addEventListener(
                "click",
                () => {

                    document
                        .querySelectorAll(
                            ".size-button"
                        )
                        .forEach(
                            item =>
                                item.classList.remove(
                                    "active"
                                )
                        );

                    button.classList.add(
                        "active"
                    );

                }
            );


            sizeOptions.appendChild(button);

        }
    );


    /* ========================================
       Quantity
       ======================================== */

    let quantity = 1;


    document
        .getElementById("decreaseQuantity")
        .addEventListener(
            "click",
            () => {

                if (quantity > 1) {

                    quantity--;

                    quantityValue.textContent =
                        quantity;

                }

            }
        );


    document
        .getElementById("increaseQuantity")
        .addEventListener(
            "click",
            () => {

                if (quantity < 10) {

                    quantity++;

                    quantityValue.textContent =
                        quantity;

                }

            }
        );


    /* ========================================
       Wishlist
       ======================================== */

    wishlistButton.addEventListener(
        "click",
        () => {

            wishlistButton.classList.toggle(
                "active"
            );


            const active =
                wishlistButton.classList.contains(
                    "active"
                );


            wishlistButton.textContent =
                active
                    ? "♥"
                    : "♡";

        }
    );


    /* ========================================
       Add To Cart
       ======================================== */

    /* ========================================
   Add To Cart
   ======================================== */

addToCartButton.addEventListener(
    "click",
    () => {

        const selectedSize =
            document.querySelector(
                ".size-button.active"
            );


        if (!selectedSize) {

            alert(
                "Please select a size."
            );

            return;
        }


        const selectedSizeValue =
            selectedSize.dataset.size;


        const cart =
            JSON.parse(
                localStorage.getItem(
                    "armaloraCart"
                )
            ) || [];


        const existingItem =
            cart.find(
                item =>
                    item.productId === product.id &&
                    item.size === selectedSizeValue
            );


        if (existingItem) {

            existingItem.quantity += quantity;

        } else {

            cart.push({

                productId: product.id,

                name: product.name,

                category: product.category,

                price: product.price,

                size: selectedSizeValue,

                quantity: quantity

            });

        }


        localStorage.setItem(
            "armaloraCart",
            JSON.stringify(cart)
        );


        window.location.href =
            "cart.html";

    }
);


    /* ========================================
       Related Products
       ======================================== */

    const relatedGrid =
        document.getElementById(
            "relatedGrid"
        );


    products
        .filter(item => item.id !== product.id)
        .slice(0, 4)
        .forEach(item => {

            const card =
                document.createElement("article");

            card.className =
                "related-card";


            card.innerHTML = `

                <div class="related-image">
                    Product Image
                </div>

                <div class="related-info">

                    <h3 class="related-name">
                        ${item.name}
                    </h3>

                    <p class="related-price">
                        LKR ${formatPrice(item.price)}
                    </p>

                </div>

            `;


            card.addEventListener(
                "click",
                () => {

                    window.location.href =
                        `product-details.html?id=${item.id}`;

                }
            );


            relatedGrid.appendChild(card);

        });


    /* ========================================
       Accordion
       ======================================== */

    document
        .querySelectorAll(".information-button")
        .forEach(button => {

            button.addEventListener(
                "click",
                () => {

                    const item =
                        button.closest(
                            ".information-item"
                        );

                    item.classList.toggle(
                        "open"
                    );

                }
            );

        });

});