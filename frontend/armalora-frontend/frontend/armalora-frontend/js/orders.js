document.addEventListener(
    "DOMContentLoaded",
    () => {

        /* ========================================
           Elements
           ======================================== */

        const ordersList =
            document.getElementById(
                "ordersList"
            );


        const ordersEmpty =
            document.getElementById(
                "ordersEmpty"
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
           Get Payment Name
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


            return "Payment";

        }


        /* ========================================
           Render Orders
           ======================================== */

        function renderOrders() {

            const orders =
                getOrders();


            ordersList.innerHTML =
                "";


            if (
                orders.length === 0
            ) {

                ordersEmpty.hidden =
                    false;

                return;

            }


            ordersEmpty.hidden =
                true;


            orders.forEach(
                (
                    order,
                    index
                ) => {

                    const card =
                        document.createElement(
                            "article"
                        );


                    card.className =
                        "order-card";


                    const itemCount =
                        order.items.reduce(
                            (
                                total,
                                item
                            ) => {

                                return total +
                                    Number(
                                        item.quantity
                                    );

                            },
                            0
                        );


                    card.innerHTML = `

                        <div class="order-card-header">

                            <div>

                                <p class="order-number">

                                    ${order.orderNumber}

                                </p>

                                <p class="order-date">

                                    ${formatDate(
                                        order.createdAt
                                    )}

                                </p>

                            </div>


                            <span class="order-status">

                                Confirmed

                            </span>

                        </div>


                        <div class="order-card-body">


                            <div>

                                <span class="order-info-label">

                                    Items

                                </span>

                                <span class="order-info-value">

                                    ${itemCount}

                                </span>

                            </div>


                            <div>

                                <span class="order-info-label">

                                    Payment

                                </span>

                                <span class="order-info-value">

                                    ${getPaymentName(
                                        order.payment
                                    )}

                                </span>

                            </div>


                            <div>

                                <span class="order-info-label">

                                    Delivery

                                </span>

                                <span class="order-info-value">

                                    ${order.delivery}

                                </span>

                            </div>


                            <div>

                                <span class="order-info-label">

                                    Customer

                                </span>

                                <span class="order-info-value">

                                    ${order.customer.firstName}

                                </span>

                            </div>


                        </div>


                        <div class="order-card-footer">


                            <strong class="order-total">

                                LKR ${formatPrice(
                                    order.total
                                )}

                            </strong>


                            <a
                                href="order-details.html?index=${index}"
                                class="order-view-button"
                            >

                                View Order

                            </a>


                        </div>

                    `;


                    ordersList.appendChild(
                        card
                    );

                }
            );

        }


        /* ========================================
           Initial Load
           ======================================== */

        renderOrders();

    }
);