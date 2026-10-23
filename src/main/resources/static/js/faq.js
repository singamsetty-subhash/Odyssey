/* ==========================================================================
   ODYSSEY FAQ PORTAL - DEDICATED JAVASCRIPT
   ========================================================================== */

let currentCategory = 'all';

function toggleFaq(id) {
    const item = document.getElementById(id);
    if (!item) return;
    const btn = item.querySelector('.faq-question-btn');
    const isActive = item.classList.contains('active');

    if (isActive) {
        item.classList.remove('active');
        if (btn) btn.setAttribute('aria-expanded', 'false');
    } else {
        item.classList.add('active');
        if (btn) btn.setAttribute('aria-expanded', 'true');
    }
}

function expandAllFaqs() {
    const items = document.querySelectorAll('.faq-item');
    items.forEach(item => {
        if (item.style.display !== 'none') {
            item.classList.add('active');
            const btn = item.querySelector('.faq-question-btn');
            if (btn) btn.setAttribute('aria-expanded', 'true');
        }
    });
}

function collapseAllFaqs() {
    const items = document.querySelectorAll('.faq-item');
    items.forEach(item => {
        item.classList.remove('active');
        const btn = item.querySelector('.faq-question-btn');
        if (btn) btn.setAttribute('aria-expanded', 'false');
    });
}

function setCategory(cat, pillEl) {
    currentCategory = cat;
    document.querySelectorAll('.cat-pill').forEach(p => p.classList.remove('active'));
    if (pillEl) pillEl.classList.add('active');
    filterFaqs();
}

function filterFaqs() {
    const searchInput = document.getElementById('faqSearchInput');
    const query = (searchInput ? searchInput.value : '').toLowerCase().trim();
    const items = document.querySelectorAll('.faq-item');
    let visibleCount = 0;

    items.forEach(item => {
        const itemCat = item.getAttribute('data-category');
        const text = item.textContent.toLowerCase();

        const matchesCategory = (currentCategory === 'all' || itemCat === currentCategory);
        const matchesSearch = (!query || text.includes(query));

        if (matchesCategory && matchesSearch) {
            item.style.display = '';
            visibleCount++;
            // If user is searching and matches, auto expand
            if (query.length > 2) {
                item.classList.add('active');
            }
        } else {
            item.style.display = 'none';
        }
    });

    const noResults = document.getElementById('noResultsBox');
    if (noResults) {
        noResults.style.display = (visibleCount === 0) ? 'block' : 'none';
    }
}

window.toggleFaq = toggleFaq;
window.expandAllFaqs = expandAllFaqs;
window.collapseAllFaqs = collapseAllFaqs;
window.setCategory = setCategory;
window.filterFaqs = filterFaqs;
