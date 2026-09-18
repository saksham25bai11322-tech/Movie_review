# Movie_review
# Movie Watchlist & Review Portal (Web Version)

A simple website version of the movie watchlist app.

Files

- index.html — the entire app 
- Main.java — a small Java web server that serves index.html.

Both files must be in the same folder to work.

 How to run


javac Main.java
java Main



 Features

- User accounts (register/log in)
- Add, update, and delete movies in your watchlist
- Mark movies as "To Watch" or "Watched"
- Rate (1–5) and review watched movies
- Analytics: genre-wise average ratings and watched history

Notes

- Main.java only serves the page — it doesn't store any data itself.
- All watchlist data is saved in your browser's local storage, so it stays between visits, but only on that browser/device.
- No external libraries or frameworks are used on either side.
