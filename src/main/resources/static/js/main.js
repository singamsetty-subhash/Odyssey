// Fade sections and cards in as they scroll into view
document.addEventListener('DOMContentLoaded', function () {
    const targets = document.querySelectorAll('.card, .section-title, .lead, .round-card, .day-hero-banner');
    targets.forEach(el => el.classList.add('reveal'));

    if ('IntersectionObserver' in window) {
        const observer = new IntersectionObserver(entries => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('visible');
                    observer.unobserve(entry.target);
                }
            });
        }, { threshold: 0.15 });

        targets.forEach(el => observer.observe(el));
    } else {
        targets.forEach(el => el.classList.add('visible'));
    }

    // Initialize Day 1 view
    switchScheduleDay('day1');
    // NOTE: the mobile hamburger is handled solely by navbar.js (loaded on every page).
    // A duplicate implementation here used to open the menu and instantly close it again.
});

// Day 1 & Day 2 Schedule Button Switcher
function switchScheduleDay(day) {
    var d1 = document.getElementById('scheduleDay1');
    var d2 = document.getElementById('scheduleDay2');
    var b1 = document.getElementById('btnDay1');
    var b2 = document.getElementById('btnDay2');

    if (day === 'day1') {
        if (d1) {
            d1.style.display = 'block';
            d1.classList.add('active');
        }
        if (d2) {
            d2.style.display = 'none';
            d2.classList.remove('active');
        }
        if (b1) b1.classList.add('active');
        if (b2) b2.classList.remove('active');
        if (d1) {
            d1.querySelectorAll('.round-card, .day-hero-banner').forEach(function (el) {
                el.classList.add('visible');
                el.style.opacity = '1';
                el.style.transform = 'translateY(0)';
            });
        }
    } else {
        if (d1) {
            d1.style.display = 'none';
            d1.classList.remove('active');
        }
        if (d2) {
            d2.style.display = 'block';
            d2.classList.add('active');
        }
        if (b2) b2.classList.add('active');
        if (b1) b1.classList.remove('active');
        if (d2) {
            d2.querySelectorAll('.round-card, .day-hero-banner').forEach(function (el) {
                el.classList.add('visible');
                el.style.opacity = '1';
                el.style.transform = 'translateY(0)';
            });
        }
    }
}

window.switchScheduleDay = switchScheduleDay;

