/* ==========================================================================
   ODYSSEY ADMIN LOGIN - DEDICATED JAVASCRIPT
   ========================================================================== */

function togglePasswordVisibility() {
    var input = document.getElementById('adminPassword');
    var eyeOpen = document.getElementById('eyeIconOpen');
    var eyeClosed = document.getElementById('eyeIconClosed');
    if (!input) return;

    if (input.type === 'password') {
        input.type = 'text';
        if (eyeOpen) eyeOpen.style.display = 'none';
        if (eyeClosed) eyeClosed.style.display = 'block';
    } else {
        input.type = 'password';
        if (eyeOpen) eyeOpen.style.display = 'block';
        if (eyeClosed) eyeClosed.style.display = 'none';
    }
}

// Prevent browser from pre-filling password
function clearPasswordDefault() {
    var input = document.getElementById('adminPassword');
    if (input) {
        input.value = '';
    }
}

window.togglePasswordVisibility = togglePasswordVisibility;
window.clearPasswordDefault = clearPasswordDefault;

window.addEventListener('DOMContentLoaded', clearPasswordDefault);
window.addEventListener('pageshow', clearPasswordDefault);
setTimeout(clearPasswordDefault, 60);
setTimeout(clearPasswordDefault, 250);
