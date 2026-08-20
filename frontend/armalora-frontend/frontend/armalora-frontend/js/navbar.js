document.addEventListener("DOMContentLoaded", () => {

    const mobileMenuButton =
        document.getElementById("mobileMenuButton");

    const mobileMenu =
        document.getElementById("mobileMenu");


    if (!mobileMenuButton || !mobileMenu) {
        return;
    }


    mobileMenuButton.addEventListener("click", () => {

        const isOpen =
            mobileMenu.classList.toggle("open");


        mobileMenuButton.setAttribute(
            "aria-expanded",
            isOpen
        );


        mobileMenuButton.setAttribute(
            "aria-label",
            isOpen
                ? "Close navigation menu"
                : "Open navigation menu"
        );

    });


    const mobileLinks =
        mobileMenu.querySelectorAll("a");


    mobileLinks.forEach((link) => {

        link.addEventListener("click", () => {

            mobileMenu.classList.remove("open");

            mobileMenuButton.setAttribute(
                "aria-expanded",
                "false"
            );

            mobileMenuButton.setAttribute(
                "aria-label",
                "Open navigation menu"
            );

        });

    });

});