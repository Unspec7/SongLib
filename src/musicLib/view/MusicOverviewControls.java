//Jeffrey Ma
//Brian Abrams
package musicLib.view;

import java.io.File;
import java.util.Comparator;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import musicLib.SongLib;
import musicLib.model.*;

public class MusicOverviewControls {
	@FXML private TableView<Song> tableofSongs;
	@FXML private TableColumn<Song, String> songNameCol;
	@FXML private TableColumn<Song, String> artistNameCol;
	@FXML private Label songNameLabel;
	@FXML private Label artistNameLabel;
	@FXML private Label albumNameLabel;
	@FXML private Label yearReleasedLabel;
	private SortedList<Song> sortedSongs;
	
	private SongLib SongLib;
	public MusicOverviewControls(){
	}
	
	@FXML private void initialize(){
		//Main music screen initialization
		songNameCol.setCellValueFactory(cellData -> cellData.getValue().songProperty());
		artistNameCol.setCellValueFactory(cellData -> cellData.getValue().artistProperty());
		
		details(null);//Clear the labels just in case
		
		tableofSongs.getSelectionModel().selectedItemProperty().addListener(
				(observed, oldItem, newItem) -> details(newItem)
				);
	}
	
	public void setSongLib(SongLib SongLib){
		this.SongLib = SongLib;
		//Sorting
		sortedSongs = new SortedList<>(SongLib.getSongData());
		//Creating our own comparator so it sorts based on name and artist
		sortedSongs.setComparator(
				new Comparator<Song>(){
					public int compare(Song first, Song second){
						int compareSongs = first.getSong().compareTo(second.getSong());
						if (compareSongs == 0){
							compareSongs = first.getArtist().compareTo(second.getArtist());
						}
						return compareSongs;
					}
				}
		);
		tableofSongs.setItems(sortedSongs);
		tableofSongs.getSelectionModel().select(0, songNameCol);
	}
	private void details(Song mySong){
		if (mySong != null){
			//Make labels match
			songNameLabel.setText(mySong.getSong());
			artistNameLabel.setText(mySong.getArtist());
			albumNameLabel.setText(mySong.getAlbum());
			yearReleasedLabel.setText(Integer.toString(mySong.getYear()));
			selectSong(mySong);
		}
		else{
			songNameLabel.setText("");
			artistNameLabel.setText("");
			albumNameLabel.setText("");
			yearReleasedLabel.setText("");
		}
	}
	
	@FXML private void delete(){
		Song selectedItem = tableofSongs.getSelectionModel().getSelectedItem();
		if (selectedItem != null){
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initOwner(SongLib.getmyStage());
			alert.setTitle("Confirmation");
			alert.setHeaderText("Are you sure you want to delete this song?");
			alert.showAndWait();
			
			if (alert.getResult() == ButtonType.OK){
				SongLib.getSongData().remove(selectedItem);
				File file = new File("SongInformation.xml");
				SongLib.saveData(file);
			}
			
		}
		else{
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(SongLib.getmyStage());
			alert.setTitle("Error");
			alert.setHeaderText("A song was not selected for deletion");
			
			alert.showAndWait();
		}
	}
	//Code for the add/edit
	@FXML private TextField songNameBox;
	@FXML private TextField artistNameBox;
	@FXML private TextField albumNameBox;
	@FXML private TextField yearReleasedBox;
	private Song song;
	
	public void selectSong(Song song){
		this.song = song;
		
		songNameBox.setText(song.getSong());
		artistNameBox.setText(song.getArtist());
		albumNameBox.setText(song.getAlbum());
		yearReleasedBox.setText(Integer.toString(song.getYear()));
	}
	@FXML private void clearFields(){
		songNameBox.setText("");
		artistNameBox.setText("");
		albumNameBox.setText("");
		yearReleasedBox.setText("");
	}
	
	@FXML private void onButtonClickAdd(){
		if(checkInput(1)){
			Song song = new Song();
			
			song.setSong(songNameBox.getText());
			song.setArtist(artistNameBox.getText());
			song.setAlbum(albumNameBox.getText());
			if (yearReleasedBox.getText().length() > 0){
				song.setYear(Integer.parseInt(yearReleasedBox.getText()));
			}
			else{
				song.setYear(0);
			}
			if (!tableofSongs.getItems().contains(song)){
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.initOwner(SongLib.getmyStage());
				alert.setTitle("Confirmation");
				alert.setHeaderText("Are you sure you want to add this song?");
				alert.showAndWait();
				
				if(alert.getResult() == ButtonType.OK){
					SongLib.getSongData().add(song);
					int index = tableofSongs.getItems().indexOf(song);
					tableofSongs.getSelectionModel().select(index);
				}
			}
			else{
					Alert newAlert = new Alert(AlertType.WARNING);
					newAlert.initOwner(SongLib.getmyStage());
					newAlert.setTitle("Error");
					newAlert.setHeaderText("Song already exists");	
					newAlert.showAndWait();
			}
			File file = new File("SongInformation.xml");
			SongLib.saveData(file);
		}
	}
	private void editHandling(Song tempSong){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.initOwner(SongLib.getmyStage());
		alert.setTitle("Confirmation");
		alert.setHeaderText("Are you sure you want to edit this song?");
		alert.showAndWait();
		
		if (alert.getResult() == ButtonType.OK){
			song.setSong(tempSong.getSong());
			song.setArtist(tempSong.getArtist());
			song.setAlbum(tempSong.getAlbum());
			song.setYear(tempSong.getYear());
		}
	}
	
	@FXML private void onButtonClickEdit(){
		Song selectedItem = tableofSongs.getSelectionModel().getSelectedItem();
		if (selectedItem != null){
			if(checkInput(2)){
				Song tempSong = new Song();
				tempSong.setSong(songNameBox.getText());
				tempSong.setArtist(artistNameBox.getText());
				tempSong.setAlbum(albumNameBox.getText());
				
				if (yearReleasedBox.getText().length() > 0){
					tempSong.setYear(Integer.parseInt(yearReleasedBox.getText()));
				}
				else{
					tempSong.setYear(0);
				}
				//Checks to see if you're changing the artist or song name
				if(!tempSong.getArtist().equals(selectedItem.getArtist()) ||
						!tempSong.getSong().equals(selectedItem.getSong())){
					
					if (!tableofSongs.getItems().contains(tempSong)){
						editHandling(tempSong);
					}
					else{
						Alert newAlert = new Alert(AlertType.WARNING);
						newAlert.initOwner(SongLib.getmyStage());
						newAlert.setTitle("Error");
						newAlert.setHeaderText("Song already exists");	
						newAlert.showAndWait();
					}
				}
				
				//If you're just editing the album and year, no need to check for dupes
				else{
					editHandling(tempSong);
				}
			}
			/*We need to compare here again because edit doesn't actually update the list
			 * hence it doesn't sort. So we need to sort the entire list again
			 */
			sortedSongs.setComparator(
					new Comparator<Song>(){
						public int compare(Song first, Song second){
							int compareSongs = first.getSong().compareTo(second.getSong());
							if (compareSongs == 0){
								compareSongs = first.getArtist().compareTo(second.getArtist());
							}
							return compareSongs;
						}
					}
					);
			details(song);
			File file = new File("SongInformation.xml");
			SongLib.saveData(file);
		}
		else{
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(SongLib.getmyStage());
			alert.setTitle("Error");
			alert.setHeaderText("A song was not selected for edit");
			
			alert.showAndWait();
		}
	}
	
	private boolean checkInput(int mode){
		//Checks to see if name and artist are filled, and a valid year if there is one
		String error = "";
		//Mode 1 is for adding, 2 is for editing. Editing only cares for valid year.
		if(mode == 1){
			if(songNameBox == null || songNameBox.getText().length() == 0){
				error += "Name of song cannot be empty.\n";
			}
			if(artistNameBox == null || artistNameBox.getText().length() == 0){
				error += "Name of artist cannot be empty.\n";
			}
		}
		
		if (yearReleasedBox.getText().length() > 0){
			try {
				Integer.parseInt(yearReleasedBox.getText());
			}
			catch (NumberFormatException e){
				error += "Not a valid year.\n";
			}
		}
		
		if(error.length() == 0){
			return true;
		}
		else{
			//Issues present
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(SongLib.getmyStage());
			alert.setTitle("Invalid Inputs");
			alert.setHeaderText("Please correct the following issues");
			alert.setContentText(error);
			
			alert.showAndWait();
			return false;
		}
	}
}
