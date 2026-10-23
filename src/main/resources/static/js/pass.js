/* ==========================================================================
   ODYSSEY BOARDING PASS - DEDICATED JAVASCRIPT
   ========================================================================== */

function openCalModal() {
    var modal = document.getElementById('calAutoModal');
    if (modal) modal.classList.add('open');
}

function closeCalModal() {
    var modal = document.getElementById('calAutoModal');
    if (modal) modal.classList.remove('open');
}

window.openCalModal = openCalModal;
window.closeCalModal = closeCalModal;

document.addEventListener('DOMContentLoaded', function () {
    var modal = document.getElementById('calAutoModal');
    if (!modal) return;

    var isAutoCal = modal.getAttribute('data-auto-cal') === 'true';
    var calUrl = modal.getAttribute('data-cal-url') || '';
    var passId = modal.getAttribute('data-pass-id') || '';

    if (isAutoCal && calUrl) {
        // Check if already prompted in this browser session
        var storageKey = 'odyssey_cal_prompted_' + passId;
        if (!sessionStorage.getItem(storageKey)) {
            sessionStorage.setItem(storageKey, '1');
            try {
                var win = window.open(calUrl, '_blank');
                if (!win || win.closed || typeof win.closed === 'undefined') {
                    // Popup blocked by browser policy -> show the smooth modal
                    openCalModal();
                }
            } catch (e) {
                openCalModal();
            }
        }
    }
});
