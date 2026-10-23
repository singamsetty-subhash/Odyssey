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

window.filterResponsesTable = filterResponsesTable;
