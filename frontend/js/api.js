// ========================================
// CineBook - API Handler with jQuery AJAX
// ========================================

const API_BASE = 'http://localhost:8080';

// API Object with all endpoints
const API = {
    // ==================== Auth APIs ====================
    auth: {
        register: function(data, callback) {
            $.ajax({
                url: API_BASE + '/api/auth/register',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(data),
                success: function(response) {
                    localStorage.setItem('token', response.token);
                    localStorage.setItem('user', JSON.stringify({
                        id: response.id,
                        name: response.name,
                        email: response.email,
                        role: response.role
                    }));
                    if (callback) callback(null, response);
                },
                error: function(xhr) {
                    const error = xhr.responseJSON || { message: 'Registration failed' };
                    if (callback) callback(error, null);
                }
            });
        },

        login: function(data, callback) {
            $.ajax({
                url: API_BASE + '/api/auth/login',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(data),
                success: function(response) {
                    localStorage.setItem('token', response.token);
                    localStorage.setItem('user', JSON.stringify({
                        id: response.id,
                        name: response.name,
                        email: response.email,
                        role: response.role
                    }));
                    if (callback) callback(null, response);
                },
                error: function(xhr) {
                    const error = xhr.responseJSON || { message: 'Invalid credentials' };
                    if (callback) callback(error, null);
                }
            });
        },

        logout: function() {
            localStorage.removeItem('token');
            localStorage.removeItem('user');
        }
    },

    // ==================== Movie APIs ====================
    movies: {
        getAll: function(callback) {
            $.ajax({
                url: API_BASE + '/api/movies',
                type: 'GET',
                success: function(response) {
                    if (callback) callback(null, response);
                },
                error: function(xhr) {
                    if (callback) callback(xhr.responseJSON, null);
                }
            });
        },

        getNowShowing: function(callback) {
            $.ajax({
                url: API_BASE + '/api/movies/now-showing',
                type: 'GET',
                success: function(response) {
                    if (callback) callback(null, response);
                },
                error: function(xhr) {
                    if (callback) callback(xhr.responseJSON, null);
                }
            });
        },

        getPopular: function(callback) {
            $.ajax({
                url: API_BASE + '/api/movies/popular',
                type: 'GET',
                success: function(response) {
                    if (callback) callback(null, response);
                },
                error: function(xhr) {
                    if (callback) callback(xhr.responseJSON, null);
                }
            });
        },

        getById: function(id, callback) {
            $.ajax({
                url: API_BASE + '/api/movies/' + id,
                type: 'GET',
                success: function(response) {
                    if (callback) callback(null, response);
                },
                error: function(xhr) {
                    if (callback) callback(xhr.responseJSON, null);
                }
            });
        },

        create: function(data, callback) {
            $.ajax({
                url: API_BASE + '/api/movies',
                type: 'POST',
                contentType: 'application/json',
                headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') },
                data: JSON.stringify(data),
                success: function(response) {
                    if (callback) callback(null, response);
                },
                error: function(xhr) {
                    if (callback) callback(xhr.responseJSON, null);
                }
            });
        },

        update: function(id, data, callback) {
            $.ajax({
                url: API_BASE + '/api/movies/' + id,
                type: 'PUT',
                contentType: 'application/json',
                headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') },
                data: JSON.stringify(data),
                success: function(response) {
                    if (callback) callback(null, response);
                },
                error: function(xhr) {
                    if (callback) callback(xhr.responseJSON, null);
                }
            });
        },

        delete: function(id, callback) {
            $.ajax({
                url: API_BASE + '/api/movies/' + id,
                type: 'DELETE',
                headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') },
                success: function(response) {
                    if (callback) callback(null, response);
                },
                error: function(xhr) {
                    if (callback) callback(xhr.responseJSON, null);
                }
            });
        }
    },

    // ==================== Show APIs ====================
    shows: {
        getAll: function(callback) {
            $.ajax({
                url: API_BASE + '/api/shows',
                type: 'GET',
                success: function(response) {
                    if (callback) callback(null, response);
                },
                error: function(xhr) {
                    if (callback) callback(xhr.responseJSON, null);
                }
            });
        },

        getById: function(id, callback) {
            $.ajax({
                url: API_BASE + '/api/shows/' + id,
                type: 'GET',
                success: function(response) {
                    if (callback) callback(null, response);
                },
                error: function(xhr) {
                    if (callback) callback(xhr.responseJSON, null);
                }
            });
        },

        getByMovie: function(movieId, callback) {
            $.ajax({
                url: API_BASE + '/api/shows/movie/' + movieId,
                type: 'GET',
                success: function(response) {
                    if (callback) callback(null, response);
                },
                error: function(xhr) {
                    if (callback) callback(xhr.responseJSON, null);
                }
            });
        },

        getUpcoming: function(callback) {
            $.ajax({
                url: API_BASE + '/api/shows/upcoming',
                type: 'GET',
                success: function(response) {
                    if (callback) callback(null, response);
                },
                error: function(xhr) {
                    if (callback) callback(xhr.responseJSON, null);
                }
            });
        },

        create: function(data, callback) {
            $.ajax({
                url: API_BASE + '/api/shows',
                type: 'POST',
                contentType: 'application/json',
                headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') },
                data: JSON.stringify(data),
                success: function(response) {
                    if (callback) callback(null, response);
                },
                error: function(xhr) {
                    if (callback) callback(xhr.responseJSON, null);
                }
            });
        },

        delete: function(id, callback) {
            $.ajax({
                url: API_BASE + '/api/shows/' + id,
                type: 'DELETE',
                headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') },
                success: function(response) {
                    if (callback) callback(null, response);
                },
                error: function(xhr) {
                    if (callback) callback(xhr.responseJSON, null);
                }
            });
        }
    },

    // ==================== Booking APIs ====================
    bookings: {
        getUserBookings: function(callback) {
            $.ajax({
                url: API_BASE + '/api/bookings',
                type: 'GET',
                headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') },
                success: function(response) {
                    if (callback) callback(null, response);
                },
                error: function(xhr) {
                    if (callback) callback(xhr.responseJSON, null);
                }
            });
        },

        getByReference: function(ref, callback) {
            $.ajax({
                url: API_BASE + '/api/bookings/' + ref,
                type: 'GET',
                headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') },
                success: function(response) {
                    if (callback) callback(null, response);
                },
                error: function(xhr) {
                    if (callback) callback(xhr.responseJSON, null);
                }
            });
        },

        getAvailableSeats: function(showId, callback) {
            $.ajax({
                url: API_BASE + '/api/bookings/seats/' + showId,
                type: 'GET',
                headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') },
                success: function(response) {
                    if (callback) callback(null, response);
                },
                error: function(xhr) {
                    if (callback) callback(xhr.responseJSON, null);
                }
            });
        },

        lockSeats: function(data, callback) {
            $.ajax({
                url: API_BASE + '/api/bookings/seats/lock',
                type: 'POST',
                contentType: 'application/json',
                headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') },
                data: JSON.stringify(data),
                success: function(response) {
                    if (callback) callback(null, response);
                },
                error: function(xhr) {
                    if (callback) callback(xhr.responseJSON, null);
                }
            });
        },

        create: function(data, callback) {
            $.ajax({
                url: API_BASE + '/api/bookings',
                type: 'POST',
                contentType: 'application/json',
                headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') },
                data: JSON.stringify(data),
                success: function(response) {
                    if (callback) callback(null, response);
                },
                error: function(xhr) {
                    if (callback) callback(xhr.responseJSON, null);
                }
            });
        },

        cancel: function(id, callback) {
            $.ajax({
                url: API_BASE + '/api/bookings/' + id,
                type: 'DELETE',
                headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') },
                success: function(response) {
                    if (callback) callback(null, response);
                },
                error: function(xhr) {
                    if (callback) callback(xhr.responseJSON, null);
                }
            });
        }
    },

    // ==================== Feedback APIs ====================
    feedback: {
        getByMovie: function(movieId, callback) {
            $.ajax({
                url: API_BASE + '/api/feedback/movie/' + movieId,
                type: 'GET',
                success: function(response) {
                    if (callback) callback(null, response);
                },
                error: function(xhr) {
                    if (callback) callback(xhr.responseJSON, null);
                }
            });
        },

        getUserFeedback: function(movieId, callback) {
            $.ajax({
                url: API_BASE + '/api/feedback/movie/' + movieId + '/my-feedback',
                type: 'GET',
                headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') },
                success: function(response) {
                    if (callback) callback(null, response);
                },
                error: function(xhr) {
                    if (callback) callback(xhr.responseJSON, null);
                }
            });
        },

        submit: function(data, callback) {
            $.ajax({
                url: API_BASE + '/api/feedback',
                type: 'POST',
                contentType: 'application/json',
                headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') },
                data: JSON.stringify(data),
                success: function(response) {
                    if (callback) callback(null, response);
                },
                error: function(xhr) {
                    if (callback) callback(xhr.responseJSON, null);
                }
            });
        },

        update: function(id, data, callback) {
            $.ajax({
                url: API_BASE + '/api/feedback/' + id,
                type: 'PUT',
                contentType: 'application/json',
                headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') },
                data: JSON.stringify(data),
                success: function(response) {
                    if (callback) callback(null, response);
                },
                error: function(xhr) {
                    if (callback) callback(xhr.responseJSON, null);
                }
            });
        }
    },

    // ==================== Admin APIs ====================
    admin: {
        getDashboard: function(callback) {
            $.ajax({
                url: API_BASE + '/api/admin/dashboard',
                type: 'GET',
                headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') },
                success: function(response) {
                    if (callback) callback(null, response);
                },
                error: function(xhr) {
                    if (callback) callback(xhr.responseJSON, null);
                }
            });
        },

        getUsers: function(callback) {
            $.ajax({
                url: API_BASE + '/api/admin/users',
                type: 'GET',
                headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') },
                success: function(response) {
                    if (callback) callback(null, response);
                },
                error: function(xhr) {
                    if (callback) callback(xhr.responseJSON, null);
                }
            });
        },

        updateRole: function(id, role, callback) {
            $.ajax({
                url: API_BASE + '/api/admin/users/' + id + '/role?role=' + role,
                type: 'PUT',
                headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') },
                success: function(response) {
                    if (callback) callback(null, response);
                },
                error: function(xhr) {
                    if (callback) callback(xhr.responseJSON, null);
                }
            });
        },

        getPayments: function(callback) {
            $.ajax({
                url: API_BASE + '/api/admin/payments',
                type: 'GET',
                headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') },
                success: function(response) {
                    if (callback) callback(null, response);
                },
                error: function(xhr) {
                    if (callback) callback(xhr.responseJSON, null);
                }
            });
        }
    }
};

// Export for use in other files
window.API = API;