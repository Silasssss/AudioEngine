package fr.audioengine.model;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Player {


	Clip clip;

	String path;

	boolean loop = false;

	int loop_time;

	AudioInputStream audioInputStream;

	/**
	 * 
	 * Constructeur qui permet de construire un nouveau player
	 * 
	 * @param path : le chemin vers le fichier
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws LineUnavailableException
	 */
	public Player(String path) throws UnsupportedAudioFileException, IOException, LineUnavailableException {

		this.path = path;
		this.audioInputStream = AudioSystem.getAudioInputStream(new File("src/fr/audioengine/samples/" + path));
		this.clip = AudioSystem.getClip();
		this.clip.open(audioInputStream);

	}


	/**
	 * 
	 * Méthode pour démarrer un son 
	 * 
	 * @param loop : boolean -> spécifie si le son doit être jouer en boucle
	 * @return status : boolean -> indique si le son a été jouer
	 */
	public boolean play(boolean loop) {

		boolean status = false;
		/* Si le son n'est pas déjà en route */
		if (!clip.isRunning()) {

			/* si on spécifie de le jouer en boucle */
			if (loop) {

				clip.loop(Clip.LOOP_CONTINUOUSLY);
				status = true;
			} else {
				clip.start();
				status = true;
			}
		}
		
		return status;
	}

	/**
	 * 
	 * Méthode pour démarrer un son 
	 * 
	 * @param loop : entier -> spécifie le nombre de fois que le son doit être jouer
	 * @return status : boolean -> indique si le son a été jouer
	 */
	public boolean play(int loop) {
		boolean status = false;
		/* Si le son n'est pas déjà en route */
		if (!clip.isRunning()) {

			/* si on spécifie un nombre de fois correct */
			if (loop > 1) {

				clip.loop(loop);
				status = true;
			} else {
				System.out.println("play : nombre de boucles incorrect < 1");
			}
		}
		
		return status;
	}

	/**
	 * 
	 * Méthode permettant de régler le volume d'un son
	 * 
	 * @param volume : float -> entre 0.0 (muet) et 1.0 (maximum)
	 * @return status : boolean -> indique si tout s'est bien passé
	 * 
	 */
	public boolean setVolume(float volume) {

		boolean status = false;

		if (clip.isOpen()) {
	

			if (volume < 0f || volume > 1f)
				throw new IllegalArgumentException("Volume not valid: " + volume);

			FloatControl volumeControl = (FloatControl) this.clip.getControl(FloatControl.Type.MASTER_GAIN);
			float range = volumeControl.getMaximum() - volumeControl.getMinimum();
			float gain = (range * volume) + volumeControl.getMinimum();
			volumeControl.setValue(gain);
			status = true;

		}
		return status;
	}

	/**
	 * 
	 * Permet de mettre en pause un son
	 * 
	 * @return boolean : retourne TRUE si tout s'est bien passé FALSE sinon
	 */
	public boolean pause() {

		boolean status = false;

		if (this.clip.isRunning()) {
			status = true;
			this.clip.stop();
		}

		return status;
	}

	/**
	 * 
	 * Méthode permettant de redémarrer un son déjà jouer
	 * 
	 * @return status : indique si tout s'est bien passé
	 * @throws IOException
	 * @throws LineUnavailableException
	 * @throws UnsupportedAudioFileException
	 */
	public boolean restart() throws IOException, LineUnavailableException, UnsupportedAudioFileException {

		boolean status = false;

		this.stop();
		this.clip = AudioSystem.getClip();
		this.audioInputStream = AudioSystem.getAudioInputStream(new File("src/fr/audioengine/samples/" + path));
		this.clip.open(audioInputStream);
		// this.play(this.);
		status = true;

		return status;
	}

	/**
	 * 
	 * Méthode pour arrêter un son.
	 * 
	 * @return status : boolean -> permet de savoir si tout s'est bien passé
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws LineUnavailableException
	 */
	public boolean stop() throws UnsupportedAudioFileException, IOException, LineUnavailableException {

		boolean status = false;

		if (this.clip.isRunning()) {
			this.clip.stop();
			this.clip.close();
			status = true;
		}

		return status;
	}

}
