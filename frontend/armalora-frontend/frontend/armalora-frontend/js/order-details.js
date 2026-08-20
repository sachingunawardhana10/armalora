document.addEventListener(
    "DOMContentLoaded",
    () => {

        /* ========================================
           Elements
           ======================================== */

        const orderNumber =
            document.getElementById(
                "orderNumber"
            );


        const orderDate =
            document.getElementById(
                "orderDate"
            );


        const orderStatus =
            document.getElementById(
                "orderStatus"
            );


        const customerName =
            document.getElementById(
                "customerName"
            );


        const customerEmail =
            document.getElementById(
                "customerEmail"
            );


        const deliveryMethod =
            document.getElementById(
                "deliveryMethod"
            );


        const deliveryLocation =
            document.getElementById(
                "deliveryLocation"
            );


        const paymentMethod =
            document.getElementById(
                "paymentMethod"
            );


        const paymentStatus =
            document.getElementById(
                "paymentStatus"
            );


        const orderProducts =
            document.getElementById(
                "orderProducts"
            );


        const orderSubtotal =
            document.getElementById(
                "orderSubtotal"
            );


        const orderShipping =
            document.getElementById(
                "orderShipping"
            );


        const orderTotal =
            document.getElementById(
                "orderTotal"
            );


        /* ========================================
           Get Orders
           ======================================== */

        function getOrders() {

            return (
                JSON.parse(
                    localStorage.getItem(
                        "armaloraOrders"
                    )
                ) || []
            );

        }


        /* ========================================
           Format Price
           ======================================== */

        function formatPrice(
            price
        ) {

            return new Intl.NumberFormat(
                "en-LK"
            ).format(
                Number(price)
            );

        }


        /* ========================================
           Format Date
           ======================================== */

        function formatDate(
            date
        ) {

            return new Intl.DateTimeFormat(
                "en-LK",
                {
                    day: "numeric",
                    month: "long",
                    year: "numeric"
                }
            ).format(
                new Date(date)
            );

        }


        /* ========================================
           Delivery Name
           ======================================== */

        function getDeliveryName(
            delivery
        ) {

            if (
                delivery === "standard"
            ) {

                return "Standard Delivery";

            }


            if (
                delivery === "free"
            ) {

                return "Free Delivery";

            }


            return delivery ||
                "Delivery";

        }


        /* ========================================
           Payment Name
           ======================================== */

        function getPaymentName(
            payment
        ) {

            if (
                payment === "card"
            ) {

                return "Card Payment";

            }


            if (
                payment === "cash"
            ) {

                return "Cash on Delivery";

            }


            return payment ||
                "Payment";

        }


        /* ========================================
           Get Order Index
           ======================================== */

        function getOrderIndex() {

            const params =
                new URLSearchParams(
                    window.location.search
                );


            return Number(
                params.get(
                    "index"
                )
            );

        }


        /* ========================================
           Render Products
           ======================================== */

        function renderProducts(
            items
        ) {

            orderProducts.innerHTML =
                "";


            items.forEach(
                item => {

                    const product =
                        document.createElement(
                            "article"
                        );


                    product.className =
                        "order-product";


                    const itemTotal =
                        Number(
                            item.price
                        ) *
                        Number(
                            item.quantity
                        );


                    product.innerHTML = `

                        <div
                            class="order-product-image"
                        >
                            Product
                        </div>


                        <div>

                            <p
                                class="order-product-name"
                            >
                                ${item.name}
                            </p>


                            <p
                                class="order-product-meta"
                            >

                                Size:
                                ${item.size}

                                <br>

                                Quantity:
                                ${item.quantity}

                            </p>

                        </div>


                        <p
                            class="order-product-price"
                        >

                            LKR
                            ${formatPrice(
                                itemTotal
                            )}

                        </p>

                    `;


                    orderProducts.appendChild(
                        product
                    );

                }
            );

        }


        /* ========================================
           Load Order
           ======================================== */

        function loadOrder() {

            const orders =
                getOrders();


            const index =
                getOrderIndex();


            /* ========================================
               Validate Index
               ======================================== */

            if (
                !Number.isInteger(index) ||
                index < 0 ||
                index >= orders.length
            ) {

                showInvalidOrder();

                return;

            }


            const order =
                orders[index];


            /* ========================================
               Header
               ======================================== */

            orderNumber.textContent =
                order.orderNumber;


            orderDate.textContent =
                formatDate(
                    order.createdAt
                );


            orderStatus.textContent =
                "Confirmed";


            /* ========================================
               Customer
               ======================================== */

            customerName.textContent =
                `${order.customer.firstName}
                 ${order.customer.lastName}`;


            customerEmail.textContent =
                order.customer.email;


            /* ========================================
               Delivery
               ======================================== */

            deliveryMethod.textContent =
                getDeliveryName(
                    order.delivery
                );


            deliveryLocation.textContent =
                "Sri Lanka";


            /* ========================================
               Payment
               ======================================== */

            paymentMethod.textContent =
                getPaymentName(
                    order.payment
                );


            paymentStatus.textContent =
                order.payment === "card"
                    ? "Payment confirmed"
                    : "Payment on delivery";


            /* ========================================
               Products
               ======================================== */

            renderProducts(
                order.items
            );


            /* ========================================
               Summary
               ======================================== */

            orderSubtotal.textContent =
                `LKR ${formatPrice(
                    order.subtotal
                )}`;


            orderShipping.textContent =
                order.shipping === 0
                    ? "FREE"
                    : `LKR ${formatPrice(
                        order.shipping
                    )}`;


            orderTotal.textContent =
                `LKR ${formatPrice(
                    order.total
                )}`;

        }


        /* ========================================
           Invalid Order
           ======================================== */

        function showInvalidOrder() {

            orderNumber.textContent =
                "Order Not Found";


            orderDate.textContent =
                "The requested order could not be found.";


            orderStatus.textContent =
                "Unavailable";


            customerName.textContent =
                "-";


            customerEmail.textContent =
                "-";


            deliveryMethod.textContent =
                "-";


            deliveryLocation.textContent =
                "-";


            paymentMethod.textContent =
                "-";


            paymentStatus.textContent =
                "-";


            orderProducts.innerHTML = `

                <div
                    style="
                        padding: 40px 0;
                        color: var(--color-secondary);
                    "
                >

                    This order could not be found.

                </div>

            `;


            orderSubtotal.textContent =
                "LKR 0";


            orderShipping.textContent =
                "LKR 0";


            orderTotal.textContent =
                "LKR 0";

        }


        /* ========================================
           Initial Load
           ======================================== */

        loadOrder();

    }
);