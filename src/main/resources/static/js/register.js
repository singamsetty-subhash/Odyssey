// Global handler for dynamic manual department input so inline onchange works immediately
function checkOtherDept(shouldFocus = false) {
    const deptSelect = document.getElementById('dept');
    const otherDeptWrapper = document.getElementById('otherDeptWrapper');
    const otherDeptInput = document.getElementById('otherDept');
    if (!deptSelect || !otherDeptWrapper || !otherDeptInput) return;

    if (deptSelect.value === 'Other') {
        otherDeptWrapper.style.display = 'block';
        otherDeptInput.required = true;
        if (shouldFocus) {
            setTimeout(() => otherDeptInput.focus(), 50);
        }
    } else {
        otherDeptWrapper.style.display = 'none';
        otherDeptInput.required = false;
    }
}
window.checkOtherDept = checkOtherDept;

document.addEventListener('DOMContentLoaded', () => {
    // 1. Dynamic Group Size, Member Blocks & Dynamic QR Code update
    const teamSizeCards = document.querySelectorAll('.team-size-card');
    const member2Block = document.getElementById('member2-block');
    const member3Block = document.getElementById('member3-block');
    const member4Block = document.getElementById('member4-block');
    const member5Block = document.getElementById('member5-block');

    const amountBadge = document.getElementById('amountBadge');
    const amountSub = document.getElementById('amountSub');
    const upiPayLink = document.getElementById('upiPayLink');

    const baseUpiId = "7981705559-4@ybl";
    const basePhone = "7981705559";
    const payeeName = "Shaik  Sumaya";

    function updateTeamAndPayment(size) {
        // Enforce 3 to 5 persons per group
        size = Math.max(3, Math.min(5, size));

        // Member 2 & 3 are always required since min team size is 3
        const m2Inputs = member2Block ? member2Block.querySelectorAll('input') : [];
        m2Inputs.forEach(input => { input.required = true; });

        const m3Inputs = member3Block ? member3Block.querySelectorAll('input') : [];
        m3Inputs.forEach(input => { input.required = true; });

        // Show/hide and require Member 4 if size >= 4
        if (member4Block) member4Block.classList.toggle('hidden', size < 4);
        const m4Inputs = member4Block ? member4Block.querySelectorAll('input') : [];
        m4Inputs.forEach(input => { input.required = (size >= 4); });

        // Show/hide and require Member 5 if size == 5
        if (member5Block) member5Block.classList.toggle('hidden', size < 5);
        const m5Inputs = member5Block ? member5Block.querySelectorAll('input') : [];
        m5Inputs.forEach(input => { input.required = (size >= 5); });

        // Calculate Amount: ₹300 for 3, ₹400 for 4, ₹500 for 5 persons
        const totalAmount = size * 100;

        if (amountBadge) {
            amountBadge.textContent = `₹${totalAmount}`;
        }
        if (amountSub) {
            amountSub.textContent = `(₹100 × ${size} persons)`;
        }

        // Authentic PhonePe Standee Scanner QR URI (mode=02, purpose=00, mc=0000)
        // Eliminates locked fixed-amount (am) parameter which triggers bank & PhonePe security decline blocks.
        // Opens directly in scan-and-pay mode for Shaik Sumaya.
        const scannerUpiUri = `upi://pay?pa=${encodeURIComponent(baseUpiId)}&pn=${encodeURIComponent(payeeName)}&mc=0000&mode=02&purpose=00`;
        
        if (upiPayLink) {
            upiPayLink.href = scannerUpiUri;
            upiPayLink.innerHTML = `<span>📱 Tap to Pay ₹${totalAmount} via UPI App</span> &rarr;`;
        }

        const amountHint = document.getElementById('amountHint');
        if (amountHint) {
            amountHint.textContent = totalAmount;
        }
    }

    teamSizeCards.forEach(card => {
        card.addEventListener('click', () => {
            teamSizeCards.forEach(c => c.classList.remove('selected'));
            card.classList.add('selected');
            const radio = card.querySelector('input[type="radio"]');
            if (radio) {
                radio.checked = true;
                const size = parseInt(radio.value, 10);
                updateTeamAndPayment(size);
            }
        });
    });

    // Initial load check
    const checkedSize = document.querySelector('input[name="teamSize"]:checked');
    if (checkedSize) {
        updateTeamAndPayment(parseInt(checkedSize.value, 10));
    } else {
        updateTeamAndPayment(3);
    }

    // 2. UPI ID & Phone copy buttons with clipboard fallback
    const copyUpiBtn = document.getElementById('copyUpiBtn');
    const copyBtnText = document.getElementById('copyBtnText');
    const copyPhoneBtn = document.getElementById('copyPhoneBtn');
    const copyPhoneBtnText = document.getElementById('copyPhoneBtnText');

    function copyToClipboard(text, btnElement, textElement, successMsg, defaultText) {
        function triggerSuccess() {
            if (textElement) textElement.textContent = successMsg;
            if (btnElement) {
                btnElement.style.background = 'var(--aqua)';
                btnElement.style.color = 'var(--deep)';
                btnElement.style.borderColor = 'var(--aqua)';
            }
            setTimeout(() => {
                if (textElement) textElement.textContent = defaultText;
                if (btnElement) {
                    btnElement.style.background = '';
                    btnElement.style.color = '';
                    btnElement.style.borderColor = '';
                }
            }, 2000);
        }

        if (navigator.clipboard && navigator.clipboard.writeText) {
            navigator.clipboard.writeText(text).then(triggerSuccess).catch(() => {
                fallbackCopy(text, triggerSuccess);
            });
        } else {
            fallbackCopy(text, triggerSuccess);
        }
    }

    function fallbackCopy(text, onSuccess) {
        const temp = document.createElement('textarea');
        temp.value = text;
        temp.style.position = 'fixed';
        temp.style.opacity = '0';
        document.body.appendChild(temp);
        temp.select();
        try {
            document.execCommand('copy');
            if (onSuccess) onSuccess();
        } catch (e) {
            // silent fail
        }
        document.body.removeChild(temp);
    }

    if (copyUpiBtn) {
        copyUpiBtn.addEventListener('click', () => {
            copyToClipboard(baseUpiId, copyUpiBtn, copyBtnText, '✓ Copied UPI ID!', baseUpiId);
        });
    }

    if (copyPhoneBtn) {
        copyPhoneBtn.addEventListener('click', () => {
            copyToClipboard(basePhone, copyPhoneBtn, copyPhoneBtnText, '✓ Copied Number!', basePhone);
        });
    }

    if (upiPayLink) {
        upiPayLink.addEventListener('click', (e) => {
            const isMobile = /Android|iPhone|iPad|iPod/i.test(navigator.userAgent);
            if (!isMobile) {
                // If on desktop where upi:// protocol isn't handled, smoothly scroll to official standee QR to scan
                e.preventDefault();
                const standee = document.getElementById('officialStandeeImg');
                if (standee) {
                    standee.scrollIntoView({ behavior: 'smooth', block: 'center' });
                    const wrapper = standee.closest('.phonepe-standee-wrapper');
                    if (wrapper) {
                        wrapper.style.outline = '3px solid var(--aqua)';
                        wrapper.style.boxShadow = '0 0 25px rgba(25, 211, 230, 0.6)';
                        setTimeout(() => {
                            wrapper.style.outline = '';
                            wrapper.style.boxShadow = '';
                        }, 2500);
                    }
                }
            }
        });
    }

    // 3. Payment screenshot file preview
    const paymentFileInput = document.getElementById('paymentImage');
    const previewImg = document.getElementById('paymentPreview');
    const uploadPrompt = document.getElementById('uploadPrompt');

    if (paymentFileInput && previewImg) {
        paymentFileInput.addEventListener('change', (e) => {
            const file = e.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = (event) => {
                    previewImg.src = event.target.result;
                    previewImg.style.display = 'inline-block';
                    if (uploadPrompt) {
                        uploadPrompt.textContent = `Selected: ${file.name} (${Math.round(file.size / 1024)} KB)`;
                    }
                };
                reader.readAsDataURL(file);
            }
        });
    }

    // 4. Form submission feedback
    const form = document.getElementById('odysseyRegistrationForm');
    if (form) {
        form.addEventListener('submit', () => {
            const submitBtn = document.getElementById('submitBtn');
            if (submitBtn) {
                submitBtn.disabled = true;
                submitBtn.innerHTML = '<span>⚓ Verifying Manifest &amp; Recording to Database...</span>';
                submitBtn.style.opacity = '0.75';
            }
        });
    }

    // 5. Initial check for 'Other' Department
    const deptSelect = document.getElementById('dept');
    const otherDeptInput = document.getElementById('otherDept');
    if (deptSelect) {
        deptSelect.addEventListener('change', () => checkOtherDept(true));
        if (deptSelect.value === 'Other' || (otherDeptInput && otherDeptInput.value.trim() !== '')) {
            deptSelect.value = 'Other';
            checkOtherDept(false);
        }
    }

    // 6. Real-time Team Name Uniqueness Check
    const teamNameInput = document.getElementById('teamName');
    const teamNameStatus = document.getElementById('teamNameStatus');
    let teamNameDebounceTimer = null;

    function escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }

    function verifyTeamNameAvailability() {
        if (!teamNameInput || !teamNameStatus) return;
        const name = teamNameInput.value.trim();
        if (!name) {
            teamNameStatus.style.display = 'none';
            teamNameStatus.textContent = '';
            teamNameInput.setCustomValidity('');
            teamNameInput.style.borderColor = '';
            return;
        }

        fetch(`/api/check-team-name?name=${encodeURIComponent(name)}`)
            .then(res => res.json())
            .then(data => {
                if (data.taken) {
                    teamNameStatus.style.display = 'block';
                    teamNameStatus.style.color = '#ef4444';
                    teamNameStatus.innerHTML = `&#9888; The Team Name "<strong>${escapeHtml(name)}</strong>" is already registered. Please choose another unique Team Name.`;
                    teamNameInput.setCustomValidity('Team name is already registered in database');
                    teamNameInput.style.borderColor = '#ef4444';
                } else {
                    teamNameStatus.style.display = 'block';
                    teamNameStatus.style.color = '#22c55e';
                    teamNameStatus.innerHTML = `&#10003; Team Name "<strong>${escapeHtml(name)}</strong>" is available!`;
                    teamNameInput.setCustomValidity('');
                    teamNameInput.style.borderColor = '#22c55e';
                }
            })
            .catch(() => {
                // Ignore network errors in preview
            });
    }

    window.verifyTeamNameAvailability = verifyTeamNameAvailability;

    if (teamNameInput) {
        teamNameInput.addEventListener('input', () => {
            clearTimeout(teamNameDebounceTimer);
            teamNameDebounceTimer = setTimeout(verifyTeamNameAvailability, 400);
        });
        teamNameInput.addEventListener('blur', verifyTeamNameAvailability);
        if (teamNameInput.value.trim()) {
            verifyTeamNameAvailability();
        }
    }
});
