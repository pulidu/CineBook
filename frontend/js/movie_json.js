// ================= MOCK DATABASE =================

// Movies


// Shows

// ================= MOCK API =================

const API = {

    movies: {
        getAll: function (callback) {
            setTimeout(() => callback(null, MOVIES_DB), 300);
        },

        getById: function (id, callback) {
            const movie = MOVIES_DB.find(m => m.id === id);
            setTimeout(() => {
                if (!movie) callback({ message: "Movie not found" });
                else callback(null, movie);
            }, 300);
        }
    },

    shows: {
        getByMovieId: function (movieId, callback) {
            const shows = SHOWS_DB.filter(s => s.movieId === movieId);
            setTimeout(() => callback(null, shows), 300);
        }
    },

    booking: {
        reserveSeats: function (showId, seats, callback) {
            let reserved = JSON.parse(localStorage.getItem("cinebook_reserved_seats") || "{}");

            if (!reserved[showId]) reserved[showId] = [];

            // conflict check
            const conflict = seats.some(s => reserved[showId].includes(s));
            if (conflict) {
                callback({ message: "Some seats already booked" });
                return;
            }

            reserved[showId].push(...seats);
            localStorage.setItem("cinebook_reserved_seats", JSON.stringify(reserved));

            callback(null, { success: true });
        },

        getReservedSeats: function (showId, callback) {
            let reserved = JSON.parse(localStorage.getItem("cinebook_reserved_seats") || "{}");
            callback(null, reserved[showId] || []);
        }
    }
};


// ================= OPTIONAL HELPERS =================

// simulate login
const AUTH = {
    currentUser: {
        id: 1,
        name: "CineFan",
        role: "USER"
    },

    getUser: function () {
        return this.currentUser;
    }
};


// ================= INIT (Optional Sync) =================

// Sync available seats with localStorage
(function syncSeats() {
    let reserved = JSON.parse(localStorage.getItem("cinebook_reserved_seats") || "{}");

    SHOWS_DB.forEach(show => {
        const booked = (reserved[show.id] || []).length;
        show.availableSeats = show.totalSeats - booked;
    });
})();


// ================= DEBUG (Optional) =================

window.API = API;
window.MOVIES_DB = MOVIES_DB;
window.SHOWS_DB = SHOWS_DB;

fetch('', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json'
    },
    body: JSON.stringify({
        name: "John",
        email: "john@example.com",
        age: 25
    })
})
    .then(response => response.json())
    .then(data => console.log(data))
    .catch(error => console.error('Error:', error));