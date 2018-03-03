//Jeffrey Ma
//Brian Abrams
package musicLib.model;

import javafx.beans.property.*;

public class Song {
	private final StringProperty songName;
	private final StringProperty artistName;
	private final StringProperty albumName;
	private final IntegerProperty yearReleased;
	
	/* 
	 * We use a no param constructor because
	 * we never actually use the constructor to 
	 * set the names, artist, etc. We always get
	 * the info from the text boxes, hence the lack
	 * of params @Brian
	 */

	public Song(){
		this.songName = new SimpleStringProperty("");
		this.artistName = new SimpleStringProperty("");
		this.albumName = new SimpleStringProperty("");
		this.yearReleased = new SimpleIntegerProperty(0);
	}
	//All these methods ;-;
	public String getSong(){
		return songName.get();
	}
	
	public void setSong(String songName){
		this.songName.set(songName);
	}
	
	public StringProperty songProperty(){
		return songName;
	}
	
	public String getArtist(){
		return artistName.get();
	}
	
	public void setArtist(String artistName){
		this.artistName.set(artistName);
	}
	
	public StringProperty artistProperty(){
		return artistName;
	}
	
	public String getAlbum(){
		return albumName.get();
	}
	
	public void setAlbum(String albumName){
		this.albumName.set(albumName);
	}
	
	public StringProperty albumProperty(){
		return albumName;
	}
	
	public int getYear() {
        return yearReleased.get();
    }

    public void setYear(int yearReleased) {
        this.yearReleased.set(yearReleased);
    }

    public IntegerProperty yearProperty() {
        return yearReleased;
    }
    //Replacing the default equals so we can check for if a song exists
    //only based on its name and artist
    @Override
    public boolean equals(Object song){
    	return (
    			song instanceof Song && ((Song)song).artistName.get().equals(artistName.get()) &&
    			((Song)song).songName.get().equals(songName.get())
    			);
    }
}