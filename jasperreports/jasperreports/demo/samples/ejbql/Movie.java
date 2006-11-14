/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;


/**
 * @author Marcel Overdijk (marceloverdijk@hotmail.com)
 * @version $Id$
 */
@Entity
public class Movie {

    private int id;
    private Person director;
    private String title;
    private String genre;
    private Date releaseDate;

    private java.util.Collection<Cast> cast;
    private java.util.Collection<Varia> varia;
    
    public Movie() {
    }
    
    public Movie(int id, Person director, String title, String genre, Date releaseDate) {
    	this.id = id;
    	this.director = director;
    	this.title = title;
    	this.genre = genre;
    	this.releaseDate = releaseDate;
    }    

    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    @JoinColumn(name="director")
    @ManyToOne
	public Person getDirector() {
		return director;
	}

	public void setDirector(Person director) {
		this.director = director;
	}
    
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	
	@OneToMany(mappedBy="movie")
    public java.util.Collection<Cast> getCast() {
        return this.cast;
    }

    public void setCast(java.util.Collection<Cast> cast) {
        this.cast = cast;
    }

	@OneToMany(mappedBy="movie")
    public java.util.Collection<Varia> getVaria() {
        return this.varia;
    }

    public void setVaria(java.util.Collection<Varia> varia) {
        this.varia = varia;
    }
}
