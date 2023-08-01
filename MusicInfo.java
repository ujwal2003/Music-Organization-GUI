package muiscStorage;

public class MusicInfo {
	private String songName;
	private String author;
	private String descr;
	
	public MusicInfo() {
		
	}
	
	public MusicInfo(String songName, String author, String descr) {
		this.songName = songName;
		this.author = author;
		this.descr = descr;
	}

	public String getSongName() {
		return songName;
	}

	public void setSongName(String songName) {
		this.songName = songName;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}
}