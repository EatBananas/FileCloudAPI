package biz.sfs.ebiz.filecloud.data;

import java.util.ArrayList;

/** Bildet die Eigenschaften eines Files/Ordner in einem Ordner ab
 * 
 * 
 * @author anonym
 *
 */
public class FilelistResponseEntry {
	private String path;
	private String dirpath;
	private String name;
	private String ext;
	private int fullsize;
	private String modified;
	private String type;
	private String fullfilename;
	private String size;
	private int modifiedepoch;
	private boolean isroot;
	private boolean isshareable;
	private boolean issyncable;
	private boolean isshared;
	private boolean canrename;
	private boolean showprev;
	private boolean canfavorite;
	private boolean canupload;
	private boolean candownload;
	private boolean favoritelistid;
	private boolean favoriteid;
	private boolean order;
	private boolean showquickedit;
	private boolean showlockunlock;
	private boolean showshareoption;
	private boolean locked;
	
	public static String[] ENTRYORDERLIST = new String[]{"path","dirpath", "name", "ext", "fullsize", "modified", "type", "fullfilename", "size", "modifiedepoch", "isroot", "isshareable", "issyncable", "isshared", "canrename", "showprev", "canfavorite", "canupload", "candownload", "favoritelistid", "favoriteid", "order", "showquickedit", "showlockunlock", "showshareoption", "locked"};
	
	public FilelistResponseEntry(ArrayList<String> entries) {
		this.path = entries.get(0);
		this.dirpath = entries.get(1);
		this.name = entries.get(2);
		this.ext = entries.get(3);
		this.fullsize = Integer.parseInt(entries.get(4));
		this.modified = entries.get(5);
		this.type = entries.get(6);
		this.fullfilename = entries.get(7);
		this.size = entries.get(8);
		if(entries.get(9).equals("")){this.modifiedepoch = 0;} else{ this.modifiedepoch = Integer.parseInt(entries.get(9));}
		this.isroot = Boolean.parseBoolean(entries.get(10));
		this.isshareable = Boolean.parseBoolean(entries.get(11));
		this.issyncable = Boolean.parseBoolean(entries.get(12));
		this.isshared = Boolean.parseBoolean(entries.get(13));
		this.canrename = Boolean.parseBoolean(entries.get(14));
		this.showprev = Boolean.parseBoolean(entries.get(15));
		this.canfavorite = Boolean.parseBoolean(entries.get(16));
		this.canupload = Boolean.parseBoolean(entries.get(17));
		this.candownload = Boolean.parseBoolean(entries.get(18));
		this.favoritelistid = Boolean.parseBoolean(entries.get(19));
		this.favoriteid = Boolean.parseBoolean(entries.get(20));
		this.order = Boolean.parseBoolean(entries.get(21));
		this.showquickedit = Boolean.parseBoolean(entries.get(22));
		this.showlockunlock = Boolean.parseBoolean(entries.get(23));
		this.showshareoption = Boolean.parseBoolean(entries.get(24));
		this.locked = Boolean.parseBoolean(entries.get(25));
	}



	public String getPath() {
		return path;
	}

	public String getDirpath() {
		return dirpath;
	}

	public String getName() {
		return name;
	}

	public String getExt() {
		return ext;
	}

	public int getFullsize() {
		return fullsize;
	}

	public String getModified() {
		return modified;
	}

	public String getType() {
		return type;
	}

	public String getFullfilename() {
		return fullfilename;
	}

	public String getSize() {
		return size;
	}

	public int getModifiedepoch() {
		return modifiedepoch;
	}

	public boolean isIsroot() {
		return isroot;
	}

	public boolean isIsshareable() {
		return isshareable;
	}

	public boolean isIssyncable() {
		return issyncable;
	}

	public boolean isIsshared() {
		return isshared;
	}

	public boolean isCanrename() {
		return canrename;
	}

	public boolean isShowprev() {
		return showprev;
	}

	public boolean isCanfavorite() {
		return canfavorite;
	}

	public boolean isCanupload() {
		return canupload;
	}

	public boolean isCandownload() {
		return candownload;
	}

	public boolean isFavoritelistid() {
		return favoritelistid;
	}

	public boolean isFavoriteid() {
		return favoriteid;
	}

	public boolean isOrder() {
		return order;
	}

	public boolean isShowquickedit() {
		return showquickedit;
	}

	public boolean isShowlockunlock() {
		return showlockunlock;
	}

	public boolean isShowshareoption() {
		return showshareoption;
	}

	public boolean isLocked() {
		return locked;
	}
	
}
