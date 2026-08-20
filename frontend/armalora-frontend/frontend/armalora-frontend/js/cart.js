document.addEventListener(
    "DOMContentLoaded",
    () => {


        /* ========================================
           Elements
           ======================================== */

        const cartItemsContainer =
            document.getElementById(
                "cartItems"
            );

        const emptyCart =
            document.getElementById(
                "emptyCart"
            );

        const cartContent =
            document.getElementById(
                "cartContent"
            );

        const subtotalElement =
            document.getElementById(
                "cartSubtotal"
            );

        const shippingElement =
            document.getElementById(
                "cartShipping"
            );

        const totalElement =
            document.getElementById(
                "cartTotal"
            );

        const checkoutButton =
            document.getElementById(
                "checkoutButton"
            );


        /* ========================================
           Constants
           ======================================== */

        const FREE_SHIPPING_LIMIT =
            25000;

        const SHIPPING_COST =
            1500;


        /* ========================================
           Get Cart
           ======================================== */

        function getCart() {

            return (
                JSON.parse(
                    localStorage.getItem(
                        "armaloraCart"
                    )
                ) || []
            );

        }


        /* ========================================
           Save Cart
           ======================================== */

        function saveCart(cart) {

            localStorage.setItem(
                "armaloraCart",
                JSON.stringify(cart)
            );

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
           Calculate Subtotal
           ======================================== */

        function calculateSubtotal(cart) {

            return cart.reduce(
                (
                    total,
                    item
                ) => {

                    return total +
                        (
                            item.price *
                            item.quantity
                        );

                },
                0
            );

        }


        /* ========================================
           Calculate Shipping
           ======================================== */

        function calculateShipping(
            subtotal
        ) {

            if (subtotal === 0) {

                return 0;

            }


            if (
                subtotal >=
                FREE_SHIPPING_LIMIT
            ) {

                return 0;

            }


            return SHIPPING_COST;

        }


        /* ========================================
           Render Cart
           ======================================== */

        function renderCart() {

            const cart =
                getCart();


            /* Empty */

            if (cart.length === 0) {

                cartContent.hidden =
                    true;

                emptyCart.hidden =
                    false;

                updateSummary([]);

                return;

            }


            /* Has Items */

            cartContent.hidden =
                false;

            emptyCart.hidden =
                true;


            cartItemsContainer.innerHTML =
                "";


            cart.forEach(
                (
                    item,
                    index
                ) => {

                    const article =
                        document.createElement(
                            "article"
                        );


                    article.className =
                        "cart-item";


                    article.innerHTML = `

                        <div class="cart-item-image">
                            Product Image
                        </div>


                        <div class="cart-item-main">

                            <p class="cart-item-category">
                                ${item.category}
                            </p>


                            <h2 class="cart-item-name">
                                ${item.name}
                            </h2>


                            <p class="cart-item-size">
                                Size: ${item.size}
                            </p>


                            <div class="cart-item-controls">

                                <div class="cart-quantity">

                                    <button
                                        type="button"
                                        class="cart-quantity-button"
                                        data-action="decrease"
                                        data-index="${index}"
                                    >
                                        −
                                    </button>


                                    <span class="cart-quantity-value">
                                        ${item.quantity}
                                    </span>


                                    <button
                                        type="button"
                                        class="cart-quantity-button"
                                        data-action="increase"
                                        data-index="${index}"
                                    >
                                        +
                                    </button>

                                </div>


                                <button
                                    type="button"
                                    class="remove-cart-item"
                                    data-index="${index}"
                                >
                                    Remove
                                </button>

                            </div>

                        </div>


                        <p class="cart-item-price">

                            LKR
                            ${formatPrice(
                                item.price *
                                item.quantity
                            )}

                        </p>

                    `;


                    cartItemsContainer.appendChild(
                        article
                    );

                }
            );


            updateSummary(cart);

        }


        /* ========================================
           Update Summary
           ======================================== */

        function updateSummary(cart) {

            const subtotal =
                calculateSubtotal(
                    cart
                );


            const shipping =
                calculateShipping(
                    subtotal
                );


            const total =
                subtotal +
                shipping;


            subtotalElement.textContent =
                `LKR ${formatPrice(
                    subtotal
                )}`;


            if (
                shipping === 0 &&
                subtotal > 0
            ) {

                shippingElement.textContent =
                    "FREE";

            } else {

                shippingElement.textContent =
                    `LKR ${formatPrice(
                        shipping
                    )}`;

            }


            totalElement.textContent =
                `LKR ${formatPrice(
                    total
                )}`;

        }


        /* ========================================
           Quantity / Remove Events
           ======================================== */

        cartItemsContainer.addEventListener(
            "click",
            event => {


                const quantityButton =
                    event.target.closest(
                        ".cart-quantity-button"
                    );


                const removeButton =
                    event.target.closest(
                        ".remove-cart-item"
                    );


                /* Quantity */

                if (quantityButton) {

                    const index =
                        Number(
                            quantityButton.dataset.index
                        );


                    const action =
                        quantityButton.dataset.action;


                    const cart =
                        getCart();


                    if (
                        action ===
                        "increase"
                    ) {

                        if (
                            cart[index].quantity <
                            10
                        ) {

                            cart[index].quantity++;

                        }

                    }


                    if (
                        action ===
                        "decrease"
                    ) {

                        if (
                            cart[index].quantity >
                            1
                        ) {

                            cart[index].quantity++;

                            cart[index].quantity--;

                        }

                    }


                    saveCart(cart);

                    renderCart();

                    return;

                }


                /* Remove */

                if (removeButton) {

                    const index =
                        Number(
                            removeButton.dataset.index
                        );


                    const cart =
                        getCart();


                    cart.splice(
                        index,
                        1
                    );


                    saveCart(cart);

                    renderCart();

                }

            }
        );


        /* ========================================
           Checkout
           ======================================== */

        checkoutButton.addEventListener(
            "click",
            () => {

                const cart =
                    getCart();


                if (
                    cart.length === 0
                ) {

                    return;

                }


                /*
                 * Checkout will be implemented
                 * in a future frontend batch.
                 */

                window.location.href =
                    "checkout.html";

            }
        );


        /* ========================================
           Initial Render
           ======================================== */

        renderCart();

    }
);