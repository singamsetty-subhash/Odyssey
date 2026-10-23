    var currentFilter = 'ALL';

    function toggleExportDropdown(e) {
        if (e) e.stopPropagation();
        var menu = document.getElementById('exportDropdown');
        if (!menu) return;
        menu.style.display = (menu.style.display === 'none' || menu.style.display === '') ? 'block' : 'none';
    }

    document.addEventListener('click', function(e) {
        var menu = document.getElementById('exportDropdown');
        var btn = document.getElementById('btnExportToggle');
        if (menu && menu.style.display === 'block') {
            if (!menu.contains(e.target) && (!btn || !btn.contains(e.target))) {
                menu.style.display = 'none';
            }
        }
    });

    function filterByDept(dept) {
        currentFilter = dept;
        var rows = document.querySelectorAll('.manifest-table tbody tr');
        var cards = document.querySelectorAll('.dept-card');
        var badge = document.getElementById('activeFilterBadge');
        var filterDeptName = document.getElementById('filterDeptName');
        var btnDynamicCsv = document.getElementById('btnExportDynamicCsv');
        var btnDynamicExcel = document.getElementById('btnExportDynamicExcel');

        cards.forEach(function(card) {
            if (dept !== 'ALL' && card.getAttribute('data-dept') === dept) {
                card.classList.add('active-dept');
            } else {
                card.classList.remove('active-dept');
            }
        });

        if (dept === 'ALL') {
            if (badge) badge.style.display = 'none';
            if (btnDynamicCsv) {
                btnDynamicCsv.href = '/admin/export/csv';
                btnDynamicCsv.innerHTML = '<span>&#128202; Export Total Excel (.csv)</span>';
            }
            if (btnDynamicExcel) {
                btnDynamicExcel.href = '/admin/export/excel';
                btnDynamicExcel.innerHTML = '<span>&#128196; Export Total Excel (.xls)</span>';
            }
        } else {
            if (badge) {
                badge.style.display = 'inline-block';
                if (filterDeptName) filterDeptName.textContent = dept;
            }
            if (btnDynamicCsv) {
                btnDynamicCsv.href = '/admin/export/csv?dept=' + encodeURIComponent(dept);
                btnDynamicCsv.innerHTML = '<span>&#128202; Export ' + dept + ' Excel (.csv)</span>';
            }
            if (btnDynamicExcel) {
                btnDynamicExcel.href = '/admin/export/excel?dept=' + encodeURIComponent(dept);
                btnDynamicExcel.innerHTML = '<span>&#128196; Export ' + dept + ' Excel (.xls)</span>';
            }
        }

        var visibleIndex = 1;
        rows.forEach(function(row) {
            var rowDept = row.getAttribute('data-dept');
            if (dept === 'ALL' || rowDept === dept) {
                row.style.display = '';
                var snoCell = row.querySelector('.sno-cell');
                if (snoCell) {
                    snoCell.textContent = visibleIndex++;
                }
            } else {
                row.style.display = 'none';
            }
        });
    }

    // --- 30-Minute Inactivity Auto-Logout System ---
    (function() {
        var INACTIVITY_TIMEOUT_MS = 30 * 60 * 1000; // 30 minutes
        var WARNING_WINDOW_MS = 60 * 1000;          // Show countdown warning during the final 60 seconds
        var STORAGE_KEY = 'odyssey_admin_last_activity';

        var lastActivity = Date.now();
        try {
            localStorage.setItem(STORAGE_KEY, lastActivity.toString());
        } catch (e) {}

        var modal = document.getElementById('inactivityWarningModal');
        var countdownEl = document.getElementById('inactivityCountdownSec');
        var badgeTimerText = document.getElementById('sessionTimerText');
        var isWarningActive = false;

        function recordActivity() {
            var now = Date.now();
            if (now - lastActivity > 1000) {
                lastActivity = now;
                try {
                    localStorage.setItem(STORAGE_KEY, now.toString());
                } catch (e) {}
                if (isWarningActive) {
                    dismissWarning();
                }
            }
        }

        window.keepSessionActive = function() {
            lastActivity = Date.now();
            try {
                localStorage.setItem(STORAGE_KEY, lastActivity.toString());
            } catch (e) {}
            dismissWarning();
        };

        function showWarning(remainingSec) {
            isWarningActive = true;
            if (modal) modal.style.display = 'flex';
            if (countdownEl) countdownEl.textContent = remainingSec;
        }

        function dismissWarning() {
            isWarningActive = false;
            if (modal) modal.style.display = 'none';
        }

        // Listen to all keyboard, mouse, touch, and scroll interactions
        var userInteractionEvents = [
            'mousemove', 'mousedown', 'mouseup', 'click', 'dblclick', 'wheel', 'contextmenu',
            'keydown', 'keyup', 'keypress',
            'touchstart', 'touchmove', 'touchend',
            'scroll'
        ];

        userInteractionEvents.forEach(function(eventType) {
            window.addEventListener(eventType, recordActivity, { passive: true, capture: true });
        });

        // Cross-tab synchronization
        window.addEventListener('storage', function(e) {
            if (e.key === STORAGE_KEY && e.newValue) {
                var remoteTimestamp = parseInt(e.newValue, 10);
                if (!isNaN(remoteTimestamp) && remoteTimestamp > lastActivity) {
                    lastActivity = remoteTimestamp;
                    if (isWarningActive) {
                        dismissWarning();
                    }
                }
            }
        });

        // Check inactivity every second
        setInterval(function() {
            var storedTime = lastActivity;
            try {
                var raw = localStorage.getItem(STORAGE_KEY);
                if (raw) {
                    var parsed = parseInt(raw, 10);
                    if (!isNaN(parsed)) storedTime = Math.max(storedTime, parsed);
                }
            } catch (e) {}

            var idleMs = Date.now() - storedTime;
            var remainingMs = INACTIVITY_TIMEOUT_MS - idleMs;

            if (idleMs >= INACTIVITY_TIMEOUT_MS) {
                try {
                    localStorage.removeItem(STORAGE_KEY);
                } catch (e) {}
                window.location.href = '/admin/logout?timeout=true';
                return;
            }

            if (remainingMs <= WARNING_WINDOW_MS) {
                var secLeft = Math.max(1, Math.ceil(remainingMs / 1000));
                showWarning(secLeft);
            } else {
                if (isWarningActive) {
                    dismissWarning();
                }
            }

            if (badgeTimerText) {
                var minsLeft = Math.max(1, Math.ceil(remainingMs / 60000));
                badgeTimerText.textContent = minsLeft + 'm';
            }
        }, 1000);

        // Test hook for fast verification: window.__testInactivityTimeout(seconds)
        window.__testInactivityTimeout = function(seconds) {
            var s = seconds || 5;
            INACTIVITY_TIMEOUT_MS = s * 1000;
            WARNING_WINDOW_MS = Math.max(2000, Math.floor(INACTIVITY_TIMEOUT_MS / 2));
            lastActivity = Date.now();
            try {
                localStorage.setItem(STORAGE_KEY, lastActivity.toString());
            } catch (e) {}
            console.log('[Security] Inactivity timeout modified to ' + s + 's for testing.');
        };
    })();

    /* Delete Record Modal Logic */
    function openDeleteModal(btn) {
        var id = btn.getAttribute('data-id');
        var name = btn.getAttribute('data-name');
        var team = btn.getAttribute('data-team');
        var input = document.getElementById('modalDeleteIdInput');
        var idText = document.getElementById('modalDeleteIdText');
        var nameText = document.getElementById('modalDeleteNameText');
        var teamText = document.getElementById('modalDeleteTeamText');
        var modal = document.getElementById('deleteConfirmModal');
        if (input) input.value = id;
        if (idText) idText.textContent = '#' + id;
        if (nameText) nameText.textContent = name;
        if (teamText) teamText.textContent = team ? ('Team: ' + team) : 'Individual';
        if (modal) modal.style.display = 'flex';
    }

    function closeDeleteModal() {
        var modal = document.getElementById('deleteConfirmModal');
        if (modal) modal.style.display = 'none';
    }

    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') {
            closeDeleteModal();
        }
    });

    var delModal = document.getElementById('deleteConfirmModal');
    if (delModal) {
        delModal.addEventListener('click', function(e) {
            if (e.target === delModal) {
                closeDeleteModal();
            }
        });
    }

    /* Dynamic Total Amount Received Calculation */
    function updateTotalAmountReceived() {
        var totalEl = document.getElementById('totalAmountReceivedVal');
        if (!totalEl) return;

        var feeEls = document.querySelectorAll('.fee-amount-val');
        var sum = 0;
        feeEls.forEach(function(el) {
            var fee = parseInt(el.getAttribute('data-fee') || el.textContent.replace(/[^0-9]/g, ''), 10);
            if (!isNaN(fee) && fee > 0) {
                sum += fee;
            }
        });

        var serverAttr = totalEl.getAttribute('data-server-amount');
        var serverAmount = (serverAttr !== null && serverAttr !== '') ? parseInt(serverAttr, 10) : 0;
        if (!isNaN(serverAmount) && serverAmount > 0) {
            totalEl.textContent = '₹' + serverAmount;
        } else if (sum > 0) {
            totalEl.textContent = '₹' + sum;
        } else {
            totalEl.textContent = '₹0';
        }
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', updateTotalAmountReceived);
    } else {
        updateTotalAmountReceived();
    }
