document.addEventListener('DOMContentLoaded', function () {
    const menuItems = document.querySelectorAll('.page-sidebar-menu li.has-sub > a');
    menuItems.forEach(function (item) {
        item.addEventListener('click', function (e) {
            e.preventDefault();

            const parent = this.parentElement;
            parent.classList.toggle('open');

            const submenu = parent.querySelector('.sub-menu');
            if (submenu) {
                submenu.style.display = submenu.style.display === 'block' ? 'none' : 'block';
            }
        });
    });
});