document.addEventListener(
    "DOMContentLoaded",
    () => {

        /* ========================================
           Elements
           ======================================== */

        const checkoutContent =
            document.getElementById(
                "checkoutContent"
            );

        const checkoutEmpty =
            document.getElementById(
                "checkoutEmpty"
            );

        const checkoutItems =
            document.getElementById(
                "checkoutItems"
            );

        const subtotalElement =
            document.getElementById(
                "checkoutSubtotal"
            );

        const shippingElement =
            document.getElementById(
                "checkoutShipping"
            );

        const totalElement =
            document.getElementById(
                "checkoutTotal"
            );

        const placeOrderButton =
            document.getElementById(
                "placeOrderButton"
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
           Format Currency
           ======================================== */

        function formatPrice(
            price
        ) {

            return new Intl.NumberFormat(
                "en-LK"
            ).format(price);

        }


        /* ========================================
           Calculate Subtotal
           ======================================== */

        function calculateSubtotal(
            cart
        ) {

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
           Shipping
           ======================================== */

        function calculateShipping(
            subtotal
        ) {

            if (
                subtotal >=
                FREE_SHIPPING_LIMIT
            ) {

                return 0;

            }

            return SHIPPING_COST;

        }


        /* ========================================
           Render Order Items
           ======================================== */

        function renderItems(
            cart
        ) {

            checkoutItems.innerHTML =
                "";


            cart.forEach(
                item => {

                    const element =
                        document.createElement(
                            "div"
                        );


                    element.className =
                        "checkout-summary-item";


                    element.innerHTML = `

                        <div class="checkout-summary-image">
                            Product
                        </div>


                        <div>

                            <p class="checkout-summary-item-name">
                                ${item.name}
                            </p>


                            <p class="checkout-summary-item-meta">

                                Size: ${item.size}
                                <br>

                                Quantity: ${item.quantity}

                            </p>

                        </div>


                        <p class="checkout-summary-item-price">

                            LKR
                            ${formatPrice(
                                item.price *
                                item.quantity
                            )}

                        </p>

                    `;


                    checkoutItems.appendChild(
                        element
                    );

                }
            );

        }


        /* ========================================
           Update Summary
           ======================================== */

        function updateSummary(
            cart
        ) {

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


            shippingElement.textContent =
                shipping === 0
                    ? "FREE"
                    : `LKR ${formatPrice(
                        shipping
                    )}`;


            totalElement.textContent =
                `LKR ${formatPrice(
                    total
                )}`;

        }


        /* ========================================
           Delivery UI
           ======================================== */

        const deliveryOptions =
            document.querySelectorAll(
                ".delivery-option"
            );


        deliveryOptions.forEach(
            option => {

                option.addEventListener(
                    "click",
                    () => {

                        deliveryOptions.forEach(
                            item => {

                                item.classList.remove(
                                    "active"
                                );

                            }
                        );


                        option.classList.add(
                            "active"
                        );


                        const radio =
                            option.querySelector(
                                "input"
                            );


                        radio.checked =
                            true;

                    }
                );

            }
        );


        /* ========================================
           Payment UI
           ======================================== */

        const paymentOptions =
            document.querySelectorAll(
                ".payment-option"
            );


        paymentOptions.forEach(
            option => {

                option.addEventListener(
                    "click",
                    () => {

                        paymentOptions.forEach(
                            item => {

                                item.classList.remove(
                                    "active"
                                );

                            }
                        );


                        option.classList.add(
                            "active"
                        );


                        const radio =
                            option.querySelector(
                                "input"
                            );


                        radio.checked =
                            true;

                    }
                );

            }
        );


        /* ========================================
           Validation Helper
           ======================================== */

        function showError(
            inputId,
            errorId,
            show
        ) {

            const input =
                document.getElementById(
                    inputId
                );

            const error =
                document.getElementById(
                    errorId
                );


            if (show) {

                input.classList.add(
                    "error"
                );

                error.classList.add(
                    "visible"
                );

            } else {

                input.classList.remove(
                    "error"
                );

                error.classList.remove(
                    "visible"
                );

            }

        }


        /* ========================================
           Validate Checkout
           ======================================== */

        function validateCheckout() {

            let valid = true;


            const firstName =
                document.getElementById(
                    "firstName"
                ).value.trim();


            const lastName =
                document.getElementById(
                    "lastName"
                ).value.trim();


            const email =
                document.getElementById(
                    "email"
                ).value.trim();


            const phone =
                document.getElementById(
                    "phone"
                ).value.trim();


            const address =
                document.getElementById(
                    "address"
                ).value.trim();


            const city =
                document.getElementById(
                    "city"
                ).value.trim();


            const postalCode =
                document.getElementById(
                    "postalCode"
                ).value.trim();


            const country =
                document.getElementById(
                    "country"
                ).value;


            /* First name */

            const firstNameInvalid =
                firstName.length < 2;


            showError(
                "firstName",
                "firstNameError",
                firstNameInvalid
            );


            if (firstNameInvalid) {
                valid = false;
            }


            /* Last name */

            const lastNameInvalid =
                lastName.length < 2;


            showError(
                "lastName",
                "lastNameError",
                lastNameInvalid
            );


            if (lastNameInvalid) {
                valid = false;
            }


            /* Email */

            const emailValid =
                /^[^\s@]+@[^\s@]+\.[^\s@]+$/
                    .test(email);


            showError(
                "email",
                "emailError",
                !emailValid
            );


            if (!emailValid) {
                valid = false;
            }


            /* Phone */

            const phoneValid =
                /^[0-9+\-\s]{9,15}$/
                    .test(phone);


            showError(
                "phone",
                "phoneError",
                !phoneValid
            );


            if (!phoneValid) {
                valid = false;
            }


            /* Address */

            const addressInvalid =
                address.length < 5;


            showError(
                "address",
                "addressError",
                addressInvalid
            );


            if (addressInvalid) {
                valid = false;
            }


            /* City */

            const cityInvalid =
                city.length < 2;


            showError(
                "city",
                "cityError",
                cityInvalid
            );


            if (cityInvalid) {
                valid = false;
            }


            /* Postal */

            const postalInvalid =
                postalCode.length < 4;


            showError(
                "postalCode",
                "postalCodeError",
                postalInvalid
            );


            if (postalInvalid) {
                valid = false;
            }


            /* Country */

            const countryInvalid =
                country === "";


            showError(
                "country",
                "countryError",
                countryInvalid
            );


            if (countryInvalid) {
                valid = false;
            }


            return valid;

        }


        /* ========================================
           Place Order
           ======================================== */

        placeOrderButton.addEventListener(
            "click",
            () => {

                const valid =
                    validateCheckout();


                if (!valid) {

                    const firstError =
                        document.querySelector(
                            ".checkout-input.error, .checkout-select.error"
                        );


                    if (firstError) {

                        firstError.scrollIntoView(
                            {
                                behavior: "smooth",
                                block: "center"
                            }
                        );

                        firstError.focus();

                    }

                    return;

                }


                /*
                 * IMPORTANT:
                 *
                 * Backend order creation is NOT
                 * connected yet.
                 *
                 * This will be implemented in a
                 * future integration batch.
                 */

                alert(
                    "Checkout information is valid. Backend order processing will be connected in a future batch."
                );

            }
        );


        /* ========================================
           Initial Page Load
           ======================================== */

        const cart =
            getCart();


        if (cart.length === 0) {

            checkoutContent.hidden =
                true;

            checkoutEmpty.hidden =
                false;

            return;

        }


        checkoutContent.hidden =
            false;

        checkoutEmpty.hidden =
            true;


        renderItems(cart);

        updateSummary(cart);

    }
);