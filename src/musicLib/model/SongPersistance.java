//Jeffrey Ma
//Brian Abrams
package musicLib.model;

import java.util.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MySongs")
public class SongPersistance {
	//This class is mostly used to hold the data so it can be saved as an XML
	private List<Song> songs;
		
	@XmlElement(name = "Song")
	public List<Song> getSongs(){
		return songs;
	}
	
	public void setSongs(List<Song> songs){
		this.songs = songs;
	}
}