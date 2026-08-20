document.addEventListener("DOMContentLoaded", () => {

    const newsletterForm =
        document.getElementById("newsletterForm");

    const newsletterEmail =
        document.getElementById("newsletterEmail");


    if (!newsletterForm || !newsletterEmail) {
        return;
    }


    newsletterForm.addEventListener("submit", (event) => {

        event.preventDefault();

        const email =
            newsletterEmail.value.trim();


        if (!email) {
            return;
        }


        alert(
            `Thank you for subscribing to Armalora, ${email}!`
        );


        newsletterForm.reset();

    });

});