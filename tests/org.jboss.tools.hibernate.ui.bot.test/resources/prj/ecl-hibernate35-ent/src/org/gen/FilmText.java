package org.gen;

// Generated Mar 6, 2015 3:28:00 PM by Hibernate Tools 3.4.0.CR1

/**
 * FilmText generated by hbm2java
 */
public class FilmText implements java.io.Serializable {

	private short filmId;
	private String title;
	private String description;

	public FilmText() {
	}

	public FilmText(short filmId, String title) {
		this.filmId = filmId;
		this.title = title;
	}

	public FilmText(short filmId, String title, String description) {
		this.filmId = filmId;
		this.title = title;
		this.description = description;
	}

	public short getFilmId() {
		return this.filmId;
	}

	public void setFilmId(short filmId) {
		this.filmId = filmId;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
