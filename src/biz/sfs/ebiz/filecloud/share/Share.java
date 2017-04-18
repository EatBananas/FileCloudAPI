package biz.sfs.ebiz.filecloud.share;

import java.util.ArrayList;

/** Bildet einen Filecloud Share ab
 * 
 * @author anonym
 *
 */
public class Share {
	private String shareid;
	private String sharename;
	private String sharelocation;
	private String shareurl;
	private String viewmode;
	private String validityperiod;
	private String sharesizelimit;
	private int maxdownloads;
	private int downloadcount;
	private int viewsize;
	private int thumbsize;
	private boolean allowpublicaccess;
	private boolean allowpublicupload;
	private boolean allowpublicviewonly;
	private boolean allowpublicuploadonly;
	private boolean isdir;
	private boolean isvalid;
	private String createddate;
	private int createdts;
	private boolean allowedit;
	private boolean allowdelete;
	private boolean allowsync;
	private boolean allowshare;
	private String shareowner;
	private boolean hidenotifications;
	private boolean ispublicsecure;
	
	public static String[] SHAREORDERLIST = new String[]{"shareid","sharename", "sharelocation", "shareurl", "viewmode", "validityperiod", "sharesizelimit", "maxdownloads", "downloadcount", "viewsize", "thumbsize", "allowpublicaccess", "allowpublicupload", "allowpublicviewonly", "allowpublicuploadonly", "isdir", "isvalid", "createddate", "createdts", "allowedit", "allowdelete", "allowsync", "allowshare", "shareowner", "hidenotifications", "ispublicsecure"};
	
	public Share(ArrayList<String> shares) {
		this.shareid = shares.get(0);
		this.sharename = shares.get(1);
		this.sharelocation = shares.get(2);
		this.shareurl = shares.get(3);
		this.viewmode = shares.get(4);
		this.validityperiod = shares.get(5);
		this.sharesizelimit = shares.get(6);
		this.maxdownloads = Integer.parseInt(shares.get(7));
		this.downloadcount = Integer.parseInt(shares.get(8));
		this.viewsize = Integer.parseInt(shares.get(9));
		this.thumbsize = Integer.parseInt(shares.get(10));
		this.allowpublicaccess = Boolean.parseBoolean(shares.get(11));
		this.allowpublicupload = Boolean.parseBoolean(shares.get(12));
		this.allowpublicviewonly = Boolean.parseBoolean(shares.get(13));
		this.allowpublicuploadonly = Boolean.parseBoolean(shares.get(14));
		this.isdir = Boolean.parseBoolean(shares.get(15));
		this.isvalid = Boolean.parseBoolean(shares.get(16));
		this.createddate = shares.get(17);
		this.createdts = Integer.parseInt(shares.get(18));
		this.allowedit = Boolean.parseBoolean(shares.get(19));
		this.allowdelete = Boolean.parseBoolean(shares.get(20));
		this.allowsync = Boolean.parseBoolean(shares.get(21));
		this.allowshare = Boolean.parseBoolean(shares.get(22));
		this.shareowner = shares.get(23);
		this.hidenotifications = Boolean.parseBoolean(shares.get(24));
		this.ispublicsecure = Boolean.parseBoolean(shares.get(25));
	}

	public String getShareid() {
		return shareid;
	}

	public String getSharename() {
		return sharename;
	}

	public String getSharelocation() {
		return sharelocation;
	}

	public String getShareurl() {
		return shareurl;
	}

	public String getViewmode() {
		return viewmode;
	}

	public String getValidityperiod() {
		return validityperiod;
	}

	public String getSharesizelimit() {
		return sharesizelimit;
	}

	public int getMaxdownloads() {
		return maxdownloads;
	}

	public int getDownloadcount() {
		return downloadcount;
	}

	public int getViewsize() {
		return viewsize;
	}

	public int getThumbsize() {
		return thumbsize;
	}

	public boolean isAllowpublicaccess() {
		return allowpublicaccess;
	}

	public boolean isAllowpublicupload() {
		return allowpublicupload;
	}

	public boolean isAllowpublicviewonly() {
		return allowpublicviewonly;
	}

	public boolean isAllowpublicuploadonly() {
		return allowpublicuploadonly;
	}

	public boolean isIsdir() {
		return isdir;
	}

	public boolean isIsvalid() {
		return isvalid;
	}

	public String getCreateddate() {
		return createddate;
	}

	public int getCreatedts() {
		return createdts;
	}

	public boolean isAllowedit() {
		return allowedit;
	}

	public boolean isAllowdelete() {
		return allowdelete;
	}

	public boolean isAllowsync() {
		return allowsync;
	}

	public boolean isAllowshare() {
		return allowshare;
	}

	public String getShareowner() {
		return shareowner;
	}

	public boolean isHidenotifications() {
		return hidenotifications;
	}

	public boolean isIspublicsecure() {
		return ispublicsecure;
	}
}
