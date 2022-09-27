package com.denis.music_rec.data.model;


public class Music {

    private Integer id;
    private String title;
    private String artist;
    private String genre;
    private String releaseDate;
    private String lyrics;

    public Music() {
    }

    public Music(Integer id,
                 String title,
                 String artist,
                 String genre,
                 String releaseDate,
                 String lyrics) {

    }

    public String getSongTitle() {
        return title;
    }

    public void setSongTitle(String title) {
        this.title = title;
    }

    public String getSongArtist() {
        return artist;
    }

    public void setSongArtist(String artist) {
        this.artist = artist;
    }

    public String getSongGenre() {
        return genre;
    }

    public void setSongGenre(String genre) {
        this.genre = genre;
    }

    public String getSongReleaseDate() {
        return releaseDate;
    }

    public void setSongReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getSongLyrics() {
        return lyrics;
    }

    public void setSongLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
