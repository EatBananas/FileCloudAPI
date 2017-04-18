package biz.sfs.ebiz.filecloud.data;

import java.util.ArrayList;

/** Bildet die Eigenschaften eines, Ordners ab mit seinen einzelnen Entries
 * 
 * @author anonym
 *
 */
public class FilelistResponse {
	private String partentpath;
	private Integer total;
	private String realpath;
	private Boolean isshareable;
	private Boolean candownload;
	private Boolean cansetacls;
	private Boolean showshareoption;
	private Boolean result;
	private String message;
	private ArrayList<FilelistResponseEntry> entries;

	public static String[] METAORDERLIST=new String[]{"parentpath","total", "realpath", "canupload", "isshareable", "candownload", "cansetacls", "showshareoption", "result", "message"};
	
	
	public FilelistResponse(ArrayList<String> meta) {
		this.partentpath = meta.get(0);
		this.total = Integer.parseInt(meta.get(1));
		this.realpath = meta.get(2);
		this.isshareable = Boolean.parseBoolean(meta.get(3));
		this.candownload = Boolean.parseBoolean(meta.get(4));
		this.cansetacls = Boolean.parseBoolean(meta.get(5));
		this.showshareoption = Boolean.parseBoolean(meta.get(6));
		this.result = Boolean.parseBoolean(meta.get(7));
		this.message = meta.get(8);
		entries = new ArrayList<FilelistResponseEntry>();
	}

	public void addEntries(ArrayList<String> entrywerte) {
		FilelistResponseEntry Entry = new FilelistResponseEntry(entrywerte);
		entries.add(Entry);
	}

	public String getPartentpath() {
		return partentpath;
	}

	public Integer getTotal() {
		return total;
	}

	public String getRealpath() {
		return realpath;
	}

	public Boolean getIsshareable() {
		return isshareable;
	}

	public Boolean getCandownload() {
		return candownload;
	}

	public Boolean getCansetacls() {
		return cansetacls;
	}

	public Boolean getShowshareoption() {
		return showshareoption;
	}

	public Boolean getResult() {
		return result;
	}

	public String getMessage() {
		return message;
	}
	
	public ArrayList<FilelistResponseEntry> getEntry(){
		return entries;
	}
	
	public FilelistResponseEntry getEntry(int index){
		return entries.get(index);
	}
}
