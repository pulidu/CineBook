/**
 * Common utility functions for CineBook
 */

// ---------- URL Helpers ----------
function getUrlParam(param) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(param);
}

function setUrlParam(param, value) {
    const url = new URL(window.location.href);
    url.searchParams.set(param, value);
    window.history.pushState({}, '', url);
}

// ---------- Formatting Helpers ----------
function formatDate(dateString) {
    if (!dateString) return 'TBA';
    const date = new Date(dateString);
    if (isNaN(date.getTime())) return dateString;
    return date.toLocaleDateString('en-US', {
        month: 'short',
        day: 'numeric',
        year: 'numeric'
    });
}

function formatTime(timeString) {
    if (!timeString) return '';
    // timeString expected like "14:30" or "02:15 PM"
    if (timeString.includes('AM') || timeString.includes('PM')) return timeString;
    const [hour, minute] = timeString.split(':');
    const h = parseInt(hour);
    const ampm = h >= 12 ? 'PM' : 'AM';
    const hour12 = h % 12 || 12;
    return `${hour12}:${minute} ${ampm}`;
}

function formatCurrency(amount) {
    return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD',
        minimumFractionDigits: 2
    }).format(amount);
}

function formatDuration(minutes) {
    if (!minutes) return 'N/A';
    const hrs = Math.floor(minutes / 60);
    const mins = minutes % 60;
    if (hrs === 0) return `${mins}min`;
    if (mins === 0) return `${hrs}h`;
    return `${hrs}h ${mins}min`;
}

// ---------- Rating Stars ----------
function renderStars(rating, maxStars = 5) {
    const fullStars = Math.floor(rating);
    const hasHalfStar = (rating % 1) >= 0.5;
    let starsHtml = '';

    for (let i = 0; i < fullStars; i++) {
        starsHtml += '<i class="fas fa-star text-warning"></i>';
    }
    if (hasHalfStar) {
        starsHtml += '<i class="fas fa-star-half-alt text-warning"></i>';
    }
    const emptyStars = maxStars - fullStars - (hasHalfStar ? 1 : 0);
    for (let i = 0; i < emptyStars; i++) {
        starsHtml += '<i class="far fa-star text-warning"></i>';
    }
    return starsHtml;
}

// ---------- HTML Escape ----------
function escapeHtml(str) {
    if (!str) return '';
    return str
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;');
}

// ---------- Toast Notifications ----------
let toastContainer = null;

function getToastContainer() {
    if (!toastContainer) {
        toastContainer = document.querySelector('.toast-container');
        if (!toastContainer) {
            toastContainer = document.createElement('div');
            toastContainer.className = 'toast-container position-fixed bottom-0 end-0 p-3';
            toastContainer.style.zIndex = '1100';
            document.body.appendChild(toastContainer);
        }
    }
    return toastContainer;
}

function showToast(message, type = 'success') {
    const container = getToastContainer();
    const toastId = 'toast-' + Date.now();
    const bgClass = type === 'success' ? 'bg-success' : (type === 'danger' ? 'bg-danger' : 'bg-warning');
    const icon = type === 'success' ? 'fa-check-circle' : (type === 'danger' ? 'fa-exclamation-circle' : 'fa-info-circle');

    const toastHtml = `
        <div id="${toastId}" class="toast align-items-center text-white ${bgClass} border-0 m-2" role="alert" aria-live="assertive" aria-atomic="true" data-bs-autohide="true" data-bs-delay="3000">
            <div class="d-flex">
                <div class="toast-body">
                    <i class="fas ${icon} me-2"></i> ${escapeHtml(message)}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        </div>
    `;

    container.insertAdjacentHTML('beforeend', toastHtml);
    const toastElement = document.getElementById(toastId);
    const toast = new bootstrap.Toast(toastElement, { delay: 3500 });
    toast.show();

    toastElement.addEventListener('hidden.bs.toast', () => {
        toastElement.remove();
    });
}

// ---------- Loading Spinner ----------
function showLoadingSpinner() {
    let spinner = document.getElementById('loadingSpinner');
    if (!spinner) {
        spinner = document.createElement('div');
        spinner.id = 'loadingSpinner';
        spinner.className = 'spinner-overlay';
        spinner.innerHTML = '<div class="spinner-border text-primary" role="status"><span class="visually-hidden">Loading...</span></div>';
        spinner.style.position = 'fixed';
        spinner.style.top = '0';
        spinner.style.left = '0';
        spinner.style.width = '100%';
        spinner.style.height = '100%';
        spinner.style.backgroundColor = 'rgba(0,0,0,0.7)';
        spinner.style.display = 'flex';
        spinner.style.alignItems = 'center';
        spinner.style.justifyContent = 'center';
        spinner.style.zIndex = '9999';
        document.body.appendChild(spinner);
    }
    spinner.style.display = 'flex';
}

function hideLoadingSpinner() {
    const spinner = document.getElementById('loadingSpinner');
    if (spinner) {
        spinner.style.display = 'none';
    }
}

// ---------- Authentication (simulated) ----------
let currentUser = null;

function isLoggedIn() {
    // Simulate login check - for demo, always return true or check localStorage
    const user = localStorage.getItem('cinebook_user');
    if (user) {
        try {
            currentUser = JSON.parse(user);
            return true;
        } catch(e) {}
    }
    // For demo, auto-login as guest
    if (!currentUser) {
        currentUser = { id: 'guest_' + Date.now(), name: 'Cinephile', email: 'guest@cinebook.com' };
        localStorage.setItem('cinebook_user', JSON.stringify(currentUser));
    }
    return true;
}

function getCurrentUser() {
    if (!currentUser && isLoggedIn()) return currentUser;
    return currentUser;
}

function logout() {
    localStorage.removeItem('cinebook_user');
    currentUser = null;
    showToast('Logged out successfully', 'info');
    if (window.location.pathname.includes('account.html')) {
        setTimeout(() => { window.location.href = 'index.html'; }, 1000);
    }
}

// ---------- Seat ID Utilities ----------
function generateSeatIds(rows, cols) {
    const seats = [];
    for (let r = 0; r < rows; r++) {
        const rowLetter = String.fromCharCode(65 + r);
        for (let c = 1; c <= cols; c++) {
            seats.push(`${rowLetter}${c}`);
        }
    }
    return seats;
}

function parseSeatId(seatId) {
    const match = seatId.match(/^([A-Z])(\d+)$/);
    if (match) {
        return { row: match[1], col: parseInt(match[2], 10) };
    }
    return null;
}

// ---------- Debounce Utility ----------
function debounce(func, delay) {
    let timeout;
    return function(...args) {
        clearTimeout(timeout);
        timeout = setTimeout(() => func.apply(this, args), delay);
    };
}

// ---------- Initialize common elements ----------
document.addEventListener('DOMContentLoaded', function() {
    // Update navbar based on login status
    const navButtons = document.getElementById('navButtons');
    if (navButtons && isLoggedIn()) {
        const user = getCurrentUser();
        navButtons.innerHTML = `
            <div class="dropdown">
                <button class="btn btn-outline-light dropdown-toggle" type="button" data-bs-toggle="dropdown">
                    <i class="fas fa-user-circle me-1"></i> ${escapeHtml(user.name.split(' ')[0])}
                </button>
                <ul class="dropdown-menu dropdown-menu-end">
                    <li><a class="dropdown-item" href="my-bookings.html"><i class="fas fa-ticket-alt me-2"></i>My Bookings</a></li>
                    <li><hr class="dropdown-divider"></li>
                    <li><a class="dropdown-item" href="#" onclick="logout(); return false;"><i class="fas fa-sign-out-alt me-2"></i>Logout</a></li>
                </ul>
            </div>
        `;
    } else if (navButtons) {
        navButtons.innerHTML = `
            <a href="login.html" class="btn btn-outline-light btn-sm"><i class="fas fa-sign-in-alt me-1"></i>Login</a>
            <a href="signup.html" class="btn btn-primary btn-sm">Sign Up</a>
        `;
    }
});

// Make common functions globally available
window.getUrlParam = getUrlParam;
window.setUrlParam = setUrlParam;
window.formatDate = formatDate;
window.formatTime = formatTime;
window.formatCurrency = formatCurrency;
window.formatDuration = formatDuration;
window.renderStars = renderStars;
window.escapeHtml = escapeHtml;
window.showToast = showToast;
window.showLoadingSpinner = showLoadingSpinner;
window.hideLoadingSpinner = hideLoadingSpinner;
window.isLoggedIn = isLoggedIn;
window.getCurrentUser = getCurrentUser;
window.logout = logout;
window.generateSeatIds = generateSeatIds;
window.parseSeatId = parseSeatId;
window.debounce = debounce;