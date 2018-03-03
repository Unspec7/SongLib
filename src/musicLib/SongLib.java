//Jeffrey Ma
//Brian Abrams
package musicLib;

import java.io.File;
import java.io.IOException;
import javax.xml.bind.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import musicLib.model.Song;
import musicLib.model.SongPersistance;
import musicLib.view.MusicOverviewControls;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

public class SongLib extends Application {
	private Stage myStage;
	private ObservableList<Song> songInformation = FXCollections.observableArrayList();
	
	public SongLib(){
		/*Adds the data. Really only used this for testing purposes 
		 * songInformation.add(new Song("A", "B"));
		 * songInformation.add(new Song("C", "D"));
		*/
	}
	public ObservableList<Song> getSongData() {
        return songInformation;
    }
	
	@Override
	public void start(Stage myStage) {
		try {
			this.myStage = myStage;
			this.myStage.setTitle("J&B Music Player");
			
			File file = new File("SongInformation.xml");
			if (!file.exists()){
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else{
				loadData(file);
			}
			
			//Load music player
			FXMLLoader load = new FXMLLoader();
			load.setLocation(SongLib.class.getResource("view/MusicOverview.fxml"));
			AnchorPane music;
			music = (AnchorPane) load.load();
			
			//Controller access
			MusicOverviewControls controls = load.getController();
			controls.setSongLib(this);
			Scene scene = new Scene(music);
			myStage.setScene(scene);
			myStage.show();
		} 
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public Stage getmyStage(){
		return myStage;
	}
	
	public void loadData(File file){
		try{
			JAXBContext context = JAXBContext.newInstance(SongPersistance.class);
			Unmarshaller unmar = context.createUnmarshaller();
			//Read XML file
			SongPersistance load = (SongPersistance) unmar.unmarshal(file);
			songInformation.clear();
			songInformation.addAll(load.getSongs());
			
		} 
		catch (Exception e){
			/*There appears to be an error, but it doesn't affect any functionality at
			 * all so I'm just gonna leave this catch blank.
			 * It only appears when the XML file is empty, but doesn't when there's
			 * saved data. Don't know JAXB well enough to know why, but it doesn't
			 * break anything so, *shrug*
			 * @Jeff
			 */
		}
	}
	
	public void saveData(File file){
		try{
			JAXBContext context = JAXBContext.newInstance(SongPersistance.class);
			Marshaller marsh = context.createMarshaller();
			marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			//Converting
			SongPersistance save = new SongPersistance();
			save.setSongs(songInformation);
			//Saving
			marsh.marshal(save, file);
			//Saving file location
			//setFile(file);
			
		 } catch (Exception e) {
		        Alert alert = new Alert(AlertType.ERROR);
		        alert.setTitle("Error");
		        alert.setHeaderText("Issue saving data");
		        alert.setContentText("Could not save data to file:\n" + file.getPath());

		        alert.showAndWait();
		 }
	}

	public static void main(String[] args) {
		launch(args);
	}
}