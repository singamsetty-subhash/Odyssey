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
});
