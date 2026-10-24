/* ==========================================================================
   ODYSSEY ADMIN STUDENT RESPONSES - DEDICATED JAVASCRIPT
   ========================================================================== */

function filterResponsesTable() {
    var input = document.getElementById('respFilterInput');
    if (!input) return;
    var query = input.value.toLowerCase().trim();
    var rows = document.querySelectorAll('#responsesTable tbody tr');
    rows.forEach(function(row) {
        var text = row.textContent.toLowerCase();
        if (query === '' || text.indexOf(query) !== -1) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });
}

/* Delete Response Modal Logic */
function openDeleteResponseModal(btn) {
    var id = btn.getAttribute('data-id');
    var team = btn.getAttribute('data-team') || 'Unknown Team';
    var name = btn.getAttribute('data-name') || 'Student';

    var input = document.getElementById('modalRespIdInput');
    var idText = document.getElementById('modalRespIdText');
    var teamText = document.getElementById('modalRespTeamText');
    var nameText = document.getElementById('modalRespNameText');
    var modal = document.getElementById('deleteResponseModal');

    if (input) input.value = id;
    if (idText) idText.textContent = '#' + id;
    if (teamText) teamText.textContent = team;
    if (nameText) nameText.textContent = name;
    if (modal) modal.style.display = 'flex';
}

function closeDeleteModal() {
    var modal = document.getElementById('deleteResponseModal');
    if (modal) modal.style.display = 'none';
}

document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape') {
        closeDeleteModal();
    }
});

document.addEventListener('DOMContentLoaded', function() {
    var delModal = document.getElementById('deleteResponseModal');
    if (delModal) {
        delModal.addEventListener('click', function(e) {
            if (e.target === delModal) {
                closeDeleteModal();
            }
        });
    }

    var delForm = document.getElementById('deleteResponseForm');
    if (delForm) {
        delForm.addEventListener('submit', function() {
            var btn = document.getElementById('btnConfirmDeleteResp');
            if (btn) {
                btn.disabled = true;
                btn.innerHTML = '<span>&#8987; Deleting...</span>';
                btn.style.opacity = '0.7';
                btn.style.cursor = 'not-allowed';
            }
        });
    }
});

window.filterResponsesTable = filterResponsesTable;
window.openDeleteResponseModal = openDeleteResponseModal;
window.closeDeleteModal = closeDeleteModal;

