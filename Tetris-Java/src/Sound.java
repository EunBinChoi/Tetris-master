/*
============================================================================
Name        : Sound.java
Author      : Eunbin Choi
Version     :
Copyright   : 
Description : Sound
============================================================================
 */
import java.util.HashMap;
import java.util.Map;
import javafx.scene.media.AudioClip;

public class Sound {
	private static AudioClip hardDrop = new AudioClip("file:///" + System.getProperty("user.dir").replace('\\', '/') + "/HardDrop.mp3");
	private static AudioClip rotate = new AudioClip("file:///" + System.getProperty("user.dir").replace('\\', '/') + "/Rotate.mp3");
	private static AudioClip move = new AudioClip("file:///" + System.getProperty("user.dir").replace('\\', '/') + "/Move.mp3");
	private static AudioClip singleLineClear = new AudioClip("file:///" + System.getProperty("user.dir").replace('\\', '/') + "/SingleLineClear.mp3");
	private static AudioClip doubleLineClear = new AudioClip("file:///" + System.getProperty("user.dir").replace('\\', '/') + "/DoubleLineClear.mp3");
	private static AudioClip tripleLineClear = new AudioClip("file:///" + System.getProperty("user.dir").replace('\\', '/') + "/TripleLineClear.mp3");
	private static Map<String, AudioClip> soundBox = new HashMap<>();
	static{
		soundBox.put("HardDrop", hardDrop);
		soundBox.put("Rotate", rotate);
		soundBox.put("Move", move);
		soundBox.put("SingleLineClear", singleLineClear);
		soundBox.put("DoubleLineClear", doubleLineClear);
		soundBox.put("TripleLineClear", tripleLineClear);
	}
	
	public static void play(String key){	
		AudioClip clip = soundBox.get(key);
		if(clip!=null) clip.play();
	}
}
