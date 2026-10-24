/* ==========================================================================
   ODYSSEY STUDENT RESPONSE PORTAL - DEDICATED JAVASCRIPT
   ========================================================================== */

function toggleRespOtherDept() {
    var select = document.getElementById('respDept');
    var wrap = document.getElementById('respOtherDeptWrap');
    var input = document.getElementById('respOtherDept');
    if (!select || !wrap || !input) return;

    if (select.value === 'Other') {
        wrap.style.display = 'block';
        input.required = true;
        setTimeout(function() { input.focus(); }, 50);
    } else {
        wrap.style.display = 'none';
        input.required = false;
    }
}

window.toggleRespOtherDept = toggleRespOtherDept;

document.addEventListener('DOMContentLoaded', function() {
    var textarea = document.getElementById('respMessage');
    var counter = document.getElementById('charCount');
    if (textarea && counter) {
        textarea.addEventListener('input', function() {
            counter.textContent = textarea.value.length + ' / 3000 chars';
        });
        counter.textContent = textarea.value.length + ' / 3000 chars';
    }

    var select = document.getElementById('respDept');
    var otherInput = document.getElementById('respOtherDept');
    if (select && (select.value === 'Other' || (otherInput && otherInput.value.trim() !== ''))) {
        select.value = 'Other';
        toggleRespOtherDept();
    }

    // Name Restriction: Only letters, spaces, and dots (.)
    var leaderNameInput = document.getElementById('respLeaderName');
    if (leaderNameInput) {
        leaderNameInput.addEventListener('input', function(e) {
            var original = e.target.value;
            var cleaned = original.replace(/[^a-zA-Z\s.]/g, '');
            if (original !== cleaned) {
                e.target.value = cleaned;
            }
            if (cleaned === '' || /^[a-zA-Z\s.]+$/.test(cleaned)) {
                e.target.setCustomValidity('');
            } else {
                e.target.setCustomValidity('Name can only contain letters, spaces, and periods (.)');
            }
        });
    }

    // Form submit state
    var form = document.querySelector('form[action*="student-response"]');
    if (form) {
        form.addEventListener('submit', function(e) {
            if (!form.checkValidity()) {
                return;
            }
            var btn = document.getElementById('btnTransmit');
            if (btn) {
                btn.disabled = true;
                btn.innerHTML = '<span>🚀 Verifying Credentials with Database...</span>';
                btn.style.opacity = '0.75';
            }
        });
    }
});
