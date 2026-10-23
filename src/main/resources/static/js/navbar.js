// Mobile Navbar Hamburger Toggle Logic
(function () {
    function setupNavbar() {
        var toggle = document.getElementById('navToggle') || document.querySelector('.nav-toggle');
        var menu = document.getElementById('navMenu') || document.querySelector('.nav ul');

        if (!toggle || !menu) return;
        if (toggle.dataset.navbarInitialized === 'true') return;
        toggle.dataset.navbarInitialized = 'true';

        function openNav() {
            toggle.classList.add('open');
            toggle.setAttribute('aria-expanded', 'true');
            menu.classList.add('open');
            document.body.classList.add('nav-open');
        }

        function closeNav() {
            toggle.classList.remove('open');
            toggle.setAttribute('aria-expanded', 'false');
            menu.classList.remove('open');
            document.body.classList.remove('nav-open');
        }

        toggle.addEventListener('click', function (e) {
            e.preventDefault();
            e.stopPropagation();
            if (menu.classList.contains('open')) {
                closeNav();
            } else {
                openNav();
            }
        });

        // Close when clicking any menu link
        var navLinks = menu.querySelectorAll('a');
        navLinks.forEach(function (link) {
            link.addEventListener('click', function () {
                closeNav();
            });
        });

        // Close on clicking outside the navbar
        document.addEventListener('click', function (e) {
            if (!menu.contains(e.target) && !toggle.contains(e.target)) {
                closeNav();
            }
        });

        // Close on pressing Escape key
        document.addEventListener('keydown', function (e) {
            if (e.key === 'Escape' && menu.classList.contains('open')) {
                closeNav();
            }
        });

        // Close menu if window resized to desktop
        window.addEventListener('resize', function () {
            if (window.innerWidth > 1024 && menu.classList.contains('open')) {
                closeNav();
            }
        });
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', setupNavbar);
    } else {
        setupNavbar();
    }
})();
