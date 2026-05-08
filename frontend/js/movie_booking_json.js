// ==============================
// API CONFIG
// ==============================
const API_BASE_URL = "http://localhost:8080/api";

// ==============================
// GLOBAL DATABASE (replaces MOCK)
// ==============================
let MOVIES_DB = [];
let SHOWS_DB = []; // still local unless you also have shows API

// ==============================
// FETCH MOVIES FROM BACKEND
// ==============================
async function fetchMoviesFromAPI() {
    try {
        const response = await fetch(`${API_BASE_URL}/movies/allMovies`);

        if (!response.ok) {
            throw new Error("Failed to fetch movies");
        }

        const data = await response.json();

        // 🔥 Map backend JSON → frontend format
        MOVIES_DB = data.map(movie => ({
            id: movie.id,
            title: movie.title,
            description: movie.description,
            duration: movie.duration || 120,
            genre: movie.genre || "Unknown",
            language: movie.language || "English",
            rating: movie.rating || 4,
            releaseDate: movie.releaseDate,
            posterUrl: movie.posterUrl || movie.imageUrl,
            trailerUrl: movie.trailerUrl
        }));

        console.log("✅ Movies Loaded:", MOVIES_DB);

        // After loading movies → generate dummy shows
        generateShows();

        // Load UI
        loadMovieAndShows();

    } catch (error) {
        console.error("❌ Error loading movies:", error);
        showToast("Failed to load movies from server", "danger");
    }
}

// ==============================
// GENERATE SHOWS (TEMPORARY)
// ==============================
function generateShows() {
    SHOWS_DB = [];

    let showId = 1;

    MOVIES_DB.forEach(movie => {
        for (let i = 0; i < 3; i++) {
            SHOWS_DB.push({
                id: showId++,
                movieId: movie.id,
                showDate: new Date().toISOString(),
                showTime: ["10:00 AM", "2:00 PM", "6:00 PM"][i],
                price: 10 + (i * 2),
                totalSeats: 48,
                availableSeats: 48,
                seatLayoutRows: 6,
                seatLayoutCols: 8
            });
        }
    });

    console.log("🎬 Shows Generated:", SHOWS_DB);
}

// ==============================
// OVERRIDE INITIAL LOADER
// ==============================
document.addEventListener("DOMContentLoaded", () => {
    loadReservedSeats(); // keep your local seat system

    fetchMoviesFromAPI(); // 🔥 replace mock with API

    setTimeout(bindCheckoutListener, 400);

    const seatModalEl = document.getElementById('seatModal');
    seatModalEl.addEventListener('hidden.bs.modal', () => {
        currentlySelectedSeats = [];
        selectedShowForModal = null;
    });

    window.addEventListener('storage', (e) => {
        if (e.key === 'cinebook_reserved_seats') {
            loadReservedSeats();
            loadMovieAndShows();
        }
    });

    document.getElementById("userStatusBtn")
        ?.addEventListener("click", () =>
            showToast("Logged in as Cinephile • Ready to book!", "info")
        );
});