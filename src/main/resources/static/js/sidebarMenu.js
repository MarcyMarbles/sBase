document.addEventListener("DOMContentLoaded", function () {
    console.log("📦 sidebarMenu.js готов");

    // Навесим обработку кликов на весь пункт меню
    setTimeout(() => {
        document.querySelectorAll(".menu-item.has-children > .menu-link").forEach(function (link) {
            link.addEventListener("click", function (e) {
                e.preventDefault(); // чтобы не прыгал на #
                const parentLi = link.closest("li");
                parentLi.classList.toggle("open");
                const arrow = link.querySelector(".arrow-icon");
                if (arrow) arrow.classList.toggle("rotated");

                console.log("🔽 Переключено подменю:", parentLi);
            });
        });
    }, 100);
});
