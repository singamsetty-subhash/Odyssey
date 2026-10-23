/* =========================================================
   ODYSSEY CALENDAR & EVENT SCHEDULE JAVASCRIPT
   ========================================================= */

document.addEventListener('DOMContentLoaded', () => {
    // 1. Countdown Timer (Target: Event Start Date - October 29, 2026 10:00 AM IST)
    // Locked explicitly to Andhra Pradesh Timezone: Indian Standard Time (IST / Asia/Kolkata: UTC+05:30)
    const eventTargetTimeIST = new Date('2026-10-29T10:00:00+05:30').getTime();

    function updateCountdown() {
        const now = new Date().getTime();
        const diff = eventTargetTimeIST - now;

        const countDays = document.getElementById('countDays');
        const countHours = document.getElementById('countHours');
        const countMinutes = document.getElementById('countMinutes');
        const countSeconds = document.getElementById('countSeconds');

        if (!countDays) return;

        if (diff <= 0) {
            countDays.textContent = '00';
            countHours.textContent = '00';
            countMinutes.textContent = '00';
            countSeconds.textContent = '00';
            return;
        }

        const days = Math.floor(diff / (1000 * 60 * 60 * 24));
        const hours = Math.floor((diff / (1000 * 60 * 60)) % 24);
        const minutes = Math.floor((diff / 1000 / 60) % 60);
        const seconds = Math.floor((diff / 1000) % 60);

        countDays.textContent = String(days).padStart(2, '0');
        countHours.textContent = String(hours).padStart(2, '0');
        countMinutes.textContent = String(minutes).padStart(2, '0');
        countSeconds.textContent = String(seconds).padStart(2, '0');
    }

    updateCountdown();
    setInterval(updateCountdown, 1000);

    // Live Andhra Pradesh (IST) Clock
    function updateApLiveClock() {
        const clockEl = document.getElementById('apLiveClock');
        if (!clockEl) return;
        try {
            const now = new Date();
            const formatter = new Intl.DateTimeFormat('en-IN', {
                timeZone: 'Asia/Kolkata',
                hour: '2-digit',
                minute: '2-digit',
                second: '2-digit',
                hour12: true,
                day: 'numeric',
                month: 'short'
            });
            clockEl.textContent = formatter.format(now) + ' IST';
        } catch (e) {
            clockEl.textContent = new Date().toLocaleTimeString('en-IN') + ' IST';
        }
    }
    updateApLiveClock();
    setInterval(updateApLiveClock, 1000);

    // 2. View Mode Switching (Month Grid vs Hourly Agenda vs Milestones)
    const viewBtns = document.querySelectorAll('.view-btn');
    const viewSections = document.querySelectorAll('.calendar-view-section');

    viewBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            viewBtns.forEach(b => b.classList.remove('active'));
            viewSections.forEach(s => s.classList.remove('active'));

            btn.classList.add('active');
            const targetViewId = btn.getAttribute('data-view');
            const targetSection = document.getElementById(targetViewId);
            if (targetSection) {
                targetSection.classList.add('active');
            }
        });
    });

    // 3. Hourly Agenda Day Switching (Day 1 vs Day 2)
    const agendaDayBtns = document.querySelectorAll('.agenda-day-btn');
    const agendaDayPanels = document.querySelectorAll('.agenda-day-panel');

    agendaDayBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            agendaDayBtns.forEach(b => b.classList.remove('active'));
            agendaDayPanels.forEach(p => p.style.display = 'none');

            btn.classList.add('active');
            const dayKey = btn.getAttribute('data-day');
            const targetPanel = document.getElementById('agenda' + dayKey.toUpperCase());
            if (targetPanel) {
                targetPanel.style.display = 'block';
            }
        });
    });

    // 4. Live Search and Category Filtering
    const searchInput = document.getElementById('calSearchInput');
    const categoryPills = document.querySelectorAll('.category-pill');
    let currentCategory = 'all';

    function filterEvents() {
        const query = searchInput ? searchInput.value.toLowerCase().trim() : '';
        const agendaCards = document.querySelectorAll('.agenda-card');

        agendaCards.forEach(card => {
            const title = card.querySelector('.agenda-title')?.textContent.toLowerCase() || '';
            const desc = card.querySelector('.agenda-desc')?.textContent.toLowerCase() || '';
            const time = card.querySelector('.agenda-time-badge')?.textContent.toLowerCase() || '';
            const tag = card.querySelector('.agenda-tag')?.textContent.toLowerCase() || '';
            const cardCat = card.getAttribute('data-category') || 'all';

            const matchesQuery = !query || title.includes(query) || desc.includes(query) || time.includes(query) || tag.includes(query);
            const matchesCat = currentCategory === 'all' || cardCat === currentCategory;

            if (matchesQuery && matchesCat) {
                card.style.display = 'flex';
            } else {
                card.style.display = 'none';
            }
        });
    }

    if (searchInput) {
        searchInput.addEventListener('input', filterEvents);
    }

    categoryPills.forEach(pill => {
        pill.addEventListener('click', () => {
            categoryPills.forEach(p => p.classList.remove('active'));
            pill.classList.add('active');
            currentCategory = pill.getAttribute('data-category');
            filterEvents();
        });
    });

    // 5. Interactive Monthly Calendar Grid Generator
    const currentCalMonth = new Date(2026, 9, 1); // October 2026 (0-indexed: 9 = October)
    const daysGrid = document.getElementById('calendarDaysGrid');
    const monthTitle = document.getElementById('calendarMonthTitle');
    const selectedDayDetails = document.getElementById('selectedDayDetails');
    const selectedDayTitle = document.getElementById('selectedDayTitle');
    const selectedDayContent = document.getElementById('selectedDayContent');

    const calendarEventsData = {
        '2026-10-29': [
            { type: 'challenge', title: 'Odyssey Day 1 — Formation, Rounds 1 to 6 & Strategy Challenge', time: '10:00 AM – 5:00 PM IST' }
        ],
        '2026-10-30': [
            { type: 'finale', title: 'Odyssey Day 2 — Crisis Shock, Investor Pitches & Awards Gala', time: '10:00 AM – 5:00 PM IST' }
        ]
    };

    function renderMonthlyGrid() {
        if (!daysGrid || !monthTitle) return;

        const year = currentCalMonth.getFullYear();
        const month = currentCalMonth.getMonth();

        const monthNames = [
            'January', 'February', 'March', 'April', 'May', 'June',
            'July', 'August', 'September', 'October', 'November', 'December'
        ];

        monthTitle.textContent = `${monthNames[month]} ${year}`;
        daysGrid.innerHTML = '';

        const firstDayIndex = new Date(year, month, 1).getDay();
        const daysInMonth = new Date(year, month + 1, 0).getDate();
        const prevMonthDays = new Date(year, month, 0).getDate();

        // Previous month filler days
        for (let i = firstDayIndex - 1; i >= 0; i--) {
            const cell = document.createElement('div');
            cell.className = 'day-cell inactive';
            cell.innerHTML = `<span class="day-num">${prevMonthDays - i}</span>`;
            daysGrid.appendChild(cell);
        }

        // Current month days
        for (let day = 1; day <= daysInMonth; day++) {
            const dateStr = `${year}-${String(month + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
            const events = calendarEventsData[dateStr] || [];

            const cell = document.createElement('div');
            cell.className = 'day-cell' + (events.length > 0 ? ' has-event' : '');
            cell.setAttribute('data-date', dateStr);

            let pillsHtml = '';
            events.forEach(evt => {
                pillsHtml += `<span class="mini-event-pill ${evt.type}">${evt.title}</span>`;
            });

            cell.innerHTML = `
                <span class="day-num">${day}</span>
                <div class="day-events-pills">${pillsHtml}</div>
            `;

            cell.addEventListener('click', () => {
                document.querySelectorAll('.day-cell').forEach(c => c.classList.remove('selected'));
                cell.classList.add('selected');
                showDayDetails(dateStr, day, monthNames[month], events);
            });

            daysGrid.appendChild(cell);
        }

        // Auto-select Day 29 (Day 1 of Odyssey)
        const day29Cell = document.querySelector('.day-cell[data-date="2026-10-29"]');
        if (day29Cell) {
            day29Cell.click();
        }
    }

    function showDayDetails(dateStr, day, monthName, events) {
        if (!selectedDayDetails || !selectedDayTitle || !selectedDayContent) return;

        selectedDayTitle.textContent = `Manifest Schedule for ${monthName} ${day}, 2026`;

        if (events.length === 0) {
            selectedDayContent.innerHTML = `
                <div style="color: var(--muted); padding: 1rem 0; font-style: italic;">
                    No flagship rounds scheduled on this day. Use this time to prepare your crew manifest and startup hypothesis.
                </div>
            `;
            return;
        }

        let html = '<div style="display: flex; flex-direction: column; gap: 0.8rem;">';
        events.forEach(evt => {
            html += `
                <div style="background: rgba(2, 17, 31, 0.7); border: 1px solid rgba(25, 211, 230, 0.25); border-radius: 12px; padding: 1rem 1.4rem; display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 0.8rem;">
                    <div>
                        <div style="font-weight: 700; color: #ffffff; font-size: 1.05rem;">${evt.title}</div>
                        <div style="font-size: 0.82rem; color: var(--aqua); margin-top: 0.25rem;">⏰ ${evt.time} &middot; Venue: KITS Guntur, Andhra Pradesh (IST &middot; UTC+05:30)</div>
                    </div>
                    <a href="/register" class="sync-cal-btn" style="padding: 0.45rem 1rem; font-size: 0.8rem;">Enlist Crew &rarr;</a>
                </div>
            `;
        });
        html += '</div>';

        selectedDayContent.innerHTML = html;
    }

    renderMonthlyGrid();

    // Month Navigation
    const prevMonthBtn = document.getElementById('prevMonthBtn');
    const nextMonthBtn = document.getElementById('nextMonthBtn');

    if (prevMonthBtn) {
        prevMonthBtn.addEventListener('click', () => {
            currentCalMonth.setMonth(currentCalMonth.getMonth() - 1);
            renderMonthlyGrid();
        });
    }

    if (nextMonthBtn) {
        nextMonthBtn.addEventListener('click', () => {
            currentCalMonth.setMonth(currentCalMonth.getMonth() + 1);
            renderMonthlyGrid();
        });
    }

    // 6. Add to Google Calendar & iCal Export Functions (Locked to Andhra Pradesh / Asia/Kolkata IST)
    window.addToGoogleCalendar = function () {
        const title = encodeURIComponent("Odyssey 2026: Beyond the Horizon (E-Cell Flagship Event)");
        const details = encodeURIComponent("Two days of high-stakes entrepreneurship, strategic challenge rounds, and investor pitching.\n\nDates: October 29 & 30, 2026\nTime Zone: Indian Standard Time (IST / Asia/Kolkata, Andhra Pradesh - UTC+05:30)\nOrganized by KITS E-Cell.\nOfficial Website: https://kitsecellgnt.com/");
        const location = encodeURIComponent("CV Raman Hall, KKR & KSR Institute of Technology and Sciences (KITS), Vinjanampadu, Vatticherukuru Mandal, Guntur, Andhra Pradesh 522019, India");
        // Start: Oct 29, 2026 10:00 AM IST (04:30 UTC), End: Oct 30, 2026 05:00 PM IST (11:30 UTC)
        const dates = "20261029T043000Z/20261030T113000Z";
        const ctz = "Asia/Kolkata";

        const url = `https://calendar.google.com/calendar/render?action=TEMPLATE&text=${title}&dates=${dates}&details=${details}&location=${location}&ctz=${ctz}`;
        window.open(url, '_blank');
    };

    window.downloadIcsCalendar = function () {
        const icsData = [
            "BEGIN:VCALENDAR",
            "VERSION:2.0",
            "PRODID:-//KITS E-Cell//Odyssey 2026 Calendar//EN",
            "CALSCALE:GREGORIAN",
            "METHOD:PUBLISH",
            "X-WR-TIMEZONE:Asia/Kolkata",
            "X-WR-CALNAME:Odyssey 2026 Schedule",
            "BEGIN:VTIMEZONE",
            "TZID:Asia/Kolkata",
            "X-LIC-LOCATION:Asia/Kolkata",
            "BEGIN:STANDARD",
            "TZOFFSETFROM:+0530",
            "TZOFFSETTO:+0530",
            "TZNAME:IST",
            "DTSTART:19700101T000000",
            "END:STANDARD",
            "END:VTIMEZONE",
            "BEGIN:VEVENT",
            "UID:odyssey-day1-2026@kitsecell.com",
            "SUMMARY:Odyssey 2026 — Day 1: Startup Formation & Strategic Challenge (Oct 29)",
            "DESCRIPTION:Rounds 1 to 6 of Odyssey flagship entrepreneurial journey.\\nProblem Drop, Customer Hunt, Solution Lab, and Strategy Challenge.\\nTime Zone: Indian Standard Time (IST, Andhra Pradesh).",
            "LOCATION:CV Raman Hall, KKR & KSR Institute of Technology and Sciences (KITS), Guntur, Andhra Pradesh 522019, India",
            "DTSTART:20261029T043000Z",
            "DTEND:20261029T113000Z",
            "STATUS:CONFIRMED",
            "END:VEVENT",
            "BEGIN:VEVENT",
            "UID:odyssey-day2-2026@kitsecell.com",
            "SUMMARY:Odyssey 2026 — Day 2: Crisis Shock, Investor Pitch & Grand Finale (Oct 30)",
            "DESCRIPTION:Rounds 7 to 9 of Odyssey.\\nCrisis Envelope, Pitching Arena, Investor Evaluation, and Awards Ceremony.\\nTime Zone: Indian Standard Time (IST, Andhra Pradesh).",
            "LOCATION:CV Raman Hall, KKR & KSR Institute of Technology and Sciences (KITS), Guntur, Andhra Pradesh 522019, India",
            "DTSTART:20261030T043000Z",
            "DTEND:20261030T113000Z",
            "STATUS:CONFIRMED",
            "END:VEVENT",
            "END:VCALENDAR"
        ].join("\r\n");

        const blob = new Blob([icsData], { type: 'text/calendar;charset=utf-8' });
        const link = document.createElement('a');
        link.href = window.URL.createObjectURL(blob);
        link.setAttribute('download', 'Odyssey-2026-Schedule-AndhraPradesh-IST.ics');
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    };
});
