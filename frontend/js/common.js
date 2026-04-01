// ========================================
// CineBook - Common Functions
// ========================================

$(document).ready(function() {
    // Initialize toast container
    if ($('.toast-container').length === 0) {
        $('body').append('<div class="toast-container position-fixed bottom-0 end-0 p-3"></div>');
    }

    // Initialize loading spinner
    if ($('#loadingSpinner').length === 0) {
        $('body').append(`
            <div id="loadingSpinner" class="spinner-overlay">
                <div class="spinner"></div>
            </div>
        `);
    }

    // Update navigation on every page
    updateNavigation();
});

// ==================== Toast Notifications ====================

function showToast(message, type = 'success') {
    const toastId = 'toast-' + Date.now();
    const bgClass = type === 'success' ? 'bg-success' :
        type === 'error' ? 'bg-danger' :
            type === 'warning' ? 'bg-warning' : 'bg-info';
    const icon = type === 'success' ? 'fa-check-circle' :
        type === 'error' ? 'fa-exclamation-triangle' :
            type === 'warning' ? 'fa-exclamation-circle' : 'fa-info-circle';

    const toastHtml = `
        <div id="${toastId}" class="toast align-items-center text-white ${bgClass} border-0 mb-2" role="alert">
            <div class="d-flex">
                <div class="toast-body">
                    <i class="fas ${icon} me-2"></i> ${message}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
            </div>
        </div>
    `;

    $('.toast-container').append(toastHtml);
    const toastEl = document.getElementById(toastId);
    const bsToast = new bootstrap.Toast(toastEl, { delay: 4000 });
    bsToast.show();

    $(toastEl).on('hidden.bs.toast', function() {
        $(this).remove();
    });
}

// ==================== Loading Spinner ====================

function showLoading() {
    $('#loadingSpinner').addClass('active');
}

function hideLoading() {
    $('#loadingSpinner').removeClass('active');
}

// ==================== Auth Functions ====================

function getToken() {
    return localStorage.getItem('token');
}

function getUser() {
    return JSON.parse(localStorage.getItem('user') || '{}');
}

function isLoggedIn() {
    return !!getToken();
}

function isAdmin() {
    const user = getUser();
    return user.role === 'ADMIN';
}

function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    showToast('Logged out successfully!', 'success');
    setTimeout(() => {
        window.location.href = 'index.html';
    }, 1000);
}

// ==================== Navigation ====================

function updateNavigation() {
    const token = getToken();
    const user = getUser();
    const navButtons = $('#navButtons');

    if (navButtons.length === 0) return;

    if (token && user.name) {
        if (user.role === 'ADMIN') {
            navButtons.html(`
                <span class="text-white me-2">
                    <i class="fas fa-user-circle me-1"></i> ${escapeHtml(user.name)}
                </span>
                <a href="admin-dashboard.html" class="btn btn-warning btn-sm px-3">
                    <i class="fas fa-crown"></i> Admin
                </a>
                <button onclick="logout()" class="btn btn-outline-primary btn-sm px-3">
                    <i class="fas fa-sign-out-alt"></i> Logout
                </button>
            `);
        } else {
            navButtons.html(`
                <span class="text-white me-2">
                    <i class="fas fa-user-circle me-1"></i> ${escapeHtml(user.name)}
                </span>
                <a href="dashboard.html" class="btn btn-primary btn-sm px-3">
                    <i class="fas fa-tachometer-alt"></i> Dashboard
                </a>
                <button onclick="logout()" class="btn btn-outline-primary btn-sm px-3">
                    <i class="fas fa-sign-out-alt"></i> Logout
                </button>
            `);
        }
    } else {
        navButtons.html(`
            <a href="login.html" class="btn btn-outline-primary btn-sm px-4">
                <i class="fas fa-sign-in-alt"></i> Sign In
            </a>
            <a href="register.html" class="btn btn-primary btn-sm px-4">
                <i class="fas fa-user-plus"></i> Join Now
            </a>
        `);
    }
}

// ==================== Format Functions ====================

function formatCurrency(amount) {
    return new Intl.NumberFormat('en-LK', {
        style: 'currency',
        currency: 'LKR',
        minimumFractionDigits: 2
    }).format(amount || 0);
}

function formatDate(dateString) {
    if (!dateString) return 'N/A';
    return new Date(dateString).toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
    });
}

function formatTime(timeString) {
    if (!timeString) return 'N/A';
    return new Date(`2000-01-01T${timeString}`).toLocaleTimeString('en-US', {
        hour: 'numeric',
        minute: '2-digit',
        hour12: true
    });
}

function formatDateTime(dateTimeString) {
    return formatDate(dateTimeString) + ' at ' + formatTime(dateTimeString);
}

// ==================== Rating Functions ====================

function renderStars(rating) {
    const fullStars = Math.floor(rating);
    const halfStar = rating % 1 >= 0.5;
    const emptyStars = 5 - fullStars - (halfStar ? 1 : 0);

    let stars = '';
    for (let i = 0; i < fullStars; i++) stars += '<i class="fas fa-star text-warning"></i>';
    if (halfStar) stars += '<i class="fas fa-star-half-alt text-warning"></i>';
    for (let i = 0; i < emptyStars; i++) stars += '<i class="far fa-star text-warning"></i>';

    return stars;
}

// ==================== Validation Functions ====================

function validateEmail(email) {
    const re = /^[^\s@]+@([^\s@]+\.)+[^\s@]+$/;
    return re.test(email);
}

function validatePassword(password) {
    return password && password.length >= 6;
}

// ==================== HTML Escape ====================

function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// ==================== Modal Functions ====================

function showModal(modalId) {
    const modal = new bootstrap.Modal(document.getElementById(modalId));
    modal.show();
}

function hideModal(modalId) {
    const modal = bootstrap.Modal.getInstance(document.getElementById(modalId));
    if (modal) modal.hide();
}

// ==================== Confirmation Dialog ====================

function confirmAction(message, callback) {
    if (confirm(message)) {
        callback();
    }
}

// ==================== Copy to Clipboard ====================

function copyToClipboard(text) {
    navigator.clipboard.writeText(text).then(() => {
        showToast('Copied to clipboard!', 'success');
    }).catch(() => {
        showToast('Failed to copy', 'error');
    });
}

// ==================== Get URL Parameters ====================

function getUrlParam(param) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(param);
}

// ==================== Redirect with Message ====================

function redirectWithMessage(url, message, type = 'success') {
    sessionStorage.setItem('toastMessage', message);
    sessionStorage.setItem('toastType', type);
    window.location.href = url;
}

// Check for stored toast message on page load
$(document).ready(function() {
    const message = sessionStorage.getItem('toastMessage');
    const type = sessionStorage.getItem('toastType');
    if (message) {
        showToast(message, type);
        sessionStorage.removeItem('toastMessage');
        sessionStorage.removeItem('toastType');
    }
});

// ==================== Pagination ====================

function renderPagination(currentPage, totalPages, onPageChange) {
    const container = $('#pagination');
    if (!container.length) return;

    let html = '<nav><ul class="pagination justify-content-center">';

    // Previous button
    html += `<li class="page-item ${currentPage === 1 ? 'disabled' : ''}">
        <a class="page-link" href="#" data-page="${currentPage - 1}">Previous</a>
    </li>`;

    // Page numbers
    const startPage = Math.max(1, currentPage - 2);
    const endPage = Math.min(totalPages, currentPage + 2);

    if (startPage > 1) {
        html += `<li class="page-item"><a class="page-link" href="#" data-page="1">1</a></li>`;
        if (startPage > 2) html += `<li class="page-item disabled"><span class="page-link">...</span></li>`;
    }

    for (let i = startPage; i <= endPage; i++) {
        html += `<li class="page-item ${currentPage === i ? 'active' : ''}">
            <a class="page-link" href="#" data-page="${i}">${i}</a>
        </li>`;
    }

    if (endPage < totalPages) {
        if (endPage < totalPages - 1) html += `<li class="page-item disabled"><span class="page-link">...</span></li>`;
        html += `<li class="page-item"><a class="page-link" href="#" data-page="${totalPages}">${totalPages}</a></li>`;
    }

    // Next button
    html += `<li class="page-item ${currentPage === totalPages ? 'disabled' : ''}">
        <a class="page-link" href="#" data-page="${currentPage + 1}">Next</a>
    </li>`;

    html += '</ul></nav>';
    container.html(html);

    // Bind click events
    $('.pagination a.page-link').on('click', function(e) {
        e.preventDefault();
        const page = parseInt($(this).data('page'));
        if (!isNaN(page) && page !== currentPage && page >= 1 && page <= totalPages) {
            onPageChange(page);
        }
    });
}

// ==================== Debounce Function ====================

function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// ==================== File Upload Preview ====================

function previewImage(input, previewElement) {
    if (input.files && input.files[0]) {
        const reader = new FileReader();
        reader.onload = function(e) {
            $(previewElement).attr('src', e.target.result).show();
        };
        reader.readAsDataURL(input.files[0]);
    }
}

// ==================== Local Storage Helpers ====================

function saveToLocalStorage(key, value) {
    localStorage.setItem(key, JSON.stringify(value));
}

function getFromLocalStorage(key, defaultValue = null) {
    const value = localStorage.getItem(key);
    return value ? JSON.parse(value) : defaultValue;
}

function removeFromLocalStorage(key) {
    localStorage.removeItem(key);
}

// ==================== Session Storage Helpers ====================

function saveToSessionStorage(key, value) {
    sessionStorage.setItem(key, JSON.stringify(value));
}

function getFromSessionStorage(key, defaultValue = null) {
    const value = sessionStorage.getItem(key);
    return value ? JSON.parse(value) : defaultValue;
}

// ==================== Scroll to Top ====================

function scrollToTop() {
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

// ==================== Add to Cart Animation ====================

function addToCartAnimation(buttonElement) {
    const $btn = $(buttonElement);
    const originalHtml = $btn.html();
    $btn.html('<i class="fas fa-check"></i> Added!').addClass('btn-success');
    setTimeout(() => {
        $btn.html(originalHtml).removeClass('btn-success');
    }, 1500);
}

// Export for use in other files
window.showToast = showToast;
window.showLoading = showLoading;
window.hideLoading = hideLoading;
window.getToken = getToken;
window.getUser = getUser;
window.isLoggedIn = isLoggedIn;
window.isAdmin = isAdmin;
window.logout = logout;
window.updateNavigation = updateNavigation;
window.formatCurrency = formatCurrency;
window.formatDate = formatDate;
window.formatTime = formatTime;
window.formatDateTime = formatDateTime;
window.renderStars = renderStars;
window.validateEmail = validateEmail;
window.validatePassword = validatePassword;
window.escapeHtml = escapeHtml;
window.showModal = showModal;
window.hideModal = hideModal;
window.confirmAction = confirmAction;
window.copyToClipboard = copyToClipboard;
window.getUrlParam = getUrlParam;
window.redirectWithMessage = redirectWithMessage;
window.renderPagination = renderPagination;
window.debounce = debounce;
window.previewImage = previewImage;
window.saveToLocalStorage = saveToLocalStorage;
window.getFromLocalStorage = getFromLocalStorage;
window.removeFromLocalStorage = removeFromLocalStorage;
window.scrollToTop = scrollToTop;
window.addToCartAnimation = addToCartAnimation;