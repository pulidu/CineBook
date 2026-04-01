/* ========================================
   CineBook - Sidebar Logic
   ======================================== */

$(document).ready(function () {
    // Sidebar Toggle
    $('#sidebarCollapse').on('click', function () {
        $('#sidebar, #content').toggleClass('active');
    });

    // Logout Functionality
    $('.logout-btn').on('click', function (e) {
        e.preventDefault();
        API.auth.logout();
        window.location.href = 'login.html';
    });

    // Set Active Link based on current page
    const currentPage = window.location.pathname.split("/").pop();
    $('#sidebar ul li a').each(function () {
        const href = $(this).attr('href');
        if (href === currentPage) {
            $(this).parent().addClass('active');
        }
    });

    // Check Authentication
    const token = localStorage.getItem('token');
    const user = JSON.parse(localStorage.getItem('user'));

    if (!token || !user) {
        window.location.href = 'login.html';
        return;
    }

    // Role-based Access Control
    const isAdminPage = window.location.pathname.includes('admin');
    if (isAdminPage && user.role !== 'ADMIN') {
        window.location.href = 'dashboard.html';
    } else if (!isAdminPage && user.role === 'ADMIN' && window.location.pathname.includes('dashboard')) {
        // Optional: Redirect admin to admin dashboard if they try to access user dashboard
        // window.location.href = 'admin-dashboard.html';
    }
});
