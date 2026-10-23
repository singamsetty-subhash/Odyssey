/* ==========================================================================
   ODYSSEY FIND MY TICKET - DEDICATED JAVASCRIPT
   ========================================================================== */

document.addEventListener('DOMContentLoaded', function () {
    const inputs = document.querySelectorAll('.find-input');
    inputs.forEach(input => {
        input.addEventListener('blur', function () {
            this.value = this.value.trim();
        });
    });

    const form = document.querySelector('.find-card-form');
    if (form) {
        form.addEventListener('submit', function () {
            const btn = document.getElementById('btnVerifyFindTicket');
            if (btn) {
                btn.style.opacity = '0.75';
                btn.style.pointerEvents = 'none';
                btn.innerHTML = '<span>&#8987; Verifying Credentials...</span>';
            }
        });
    }
});
