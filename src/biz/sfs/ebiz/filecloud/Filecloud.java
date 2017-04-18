package biz.sfs.ebiz.filecloud;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import biz.sfs.ebiz.filecloud.data.FilelistResponse;
import biz.sfs.ebiz.filecloud.data.FilelistResponseEntry;
import biz.sfs.ebiz.filecloud.share.Share;
import net.codejava.networking.MultipartUtility;

/**
 * Ein Objekt dieser Klasse kann eine Verbindung zur SFS-Filecloud erstellen und
 * in dieser Files und Shares erstellen sowie löschen als auch Uploads und
 * Downloads aufs/vom lokalen Dateisystem tätigen<br>
 *<br>
 *		Beispielcode:<br>
 *		---------------------------------------------------------------------------------------<br>
 * 		
 * 		<br>
 * 		Filecloud fc = new Filecloud();<br>
 * 		fc.setUrl("https://url.to.filecloud")
 *		if (!fc.login(Konstante.USERNAME, Konstante.PASSWORD)){<br>
 *			System.out.println("Login Fehlgeschlagen");<br>
 *			return;<br>
 *		}<br>
 *		System.out.println("Login OK");<br>
 *		<br>
 *		System.out.println(fc.getFiles("/ordner/pfad"));<br>
 *		System.out.println(fc.downloadFiles("/ordner/pfad/dateiname.ext", "C:/test/dateiname.ext"));<br>
 *		System.out.println(fc.getFilesordered("/ordner/pfad").getEntry(5).getName());<br>
 *		System.out.println(fc.createFolder("/ordner/pfad", "test"));<br>
 *		System.out.println(fc.deleteFolder("/ordner/pfad", "test"));<br>
 *		System.out.println(fc.uploadFile("/ordner/pfad", "C:/test/dateiname.ext"));<br>
 *		System.out.println(fc.setQShare("/ordner/pfad/dateiname.ext"));<br>
 *		System.out.println(fc.getShares().get(0).getShareid());<br>
 *		System.out.println(fc.delShare("/ordner/pfad/dateiname.ext"));<br>
 *		System.out.println(fc.setPublicAccess("/ordner/pfad/dateiname.ext"));<br>
 *		System.out.println(fc.addUsertoShare("test@comodor.de", "/ordner/pfad/dateiname.ext"));<br>
 * 		System.out.println(fc.setUserAccessShare("test@comodor.de", "/ordner/pfad/dateiname.ext", true, true, true, true));<br>
 *		System.out.println(fc.sendShareMail("test@comodor.de", "/ordner/pfad/dateiname.ext", "Dies ist eine Sharemail"));<br>
 *		System.out.println(fc.searchUserbyUsername("lord"));<br>
 *<br>
 * @version 1.0
 * @author anonym
 *
 */
public class Filecloud {
	public static final String DEFAULTURL = "https://url.to.filecloud";

	private String url = DEFAULTURL;
	private Map headerFields;
	private String cookies = null;
	private int debuglevel = 0;

	/**
	 * Setzt neues Debuglevel
	 * 
	 * @param debuglevel
	 *            Debuglevel
	 */
	public void setDebuglevel(int debuglevel) {
		this.debuglevel = debuglevel;
	}

	/**
	 * Login an der Filecloud und setzten der Cookies für weitere Verwendung
	 * 
	 * @param username
	 *            Benutzernamen
	 * @param passwort
	 *            Passwort
	 * @return Erfolgreiche(true) oder nicht erfolgreiche Anmeldung
	 */
	public boolean login(String username, String passwort) {
		Collection cookies = null;
		username = stringConvert(username);
		passwort = stringConvert(passwort);
		byte[] resultbyte = doPost("/core/loginguest", "userid=" + username + "&password=" + passwort);
		String result = new String(resultbyte);
		if (result.contains("<result>1</result>")) {
			cookies = (Collection) headerFields.get("Set-Cookie");

			if (cookies != null) {
				String combString = "";
				for (Object object : cookies) {
					String oAsString = (String) object;
					String[] parts = oAsString.split(";");
					combString += parts[0] + "; ";
				}
				combString = combString.substring(0, combString.length() - 2);
				this.cookies = combString;
				// http://www.hccp.org/java-net-cookie-how-to.html
			}
			return true;

		}
		return false;
	}

	/**
	 * Gesamte Ordnerstruktur darstellen, gibt ein Objekt mit weiteren
	 * Unterobjekten zurück
	 * 
	 * @param folder
	 *            Ordner
	 * @return FilelistResponse Objekt
	 */
	public FilelistResponse getFilesordered(String folder) {
		folder = stringConvert(folder);
		byte[] ByteXML;

		ByteXML = doPost("/core/getfilelist", "path=" + folder);
		return createFilelistResponse(ByteXML);
	}

	/**
	 * Wandelt den Pfad des Shares in seine ID um
	 * 
	 * @param location
	 *            Pfad des Shares
	 * @return ID des Shares
	 */
	private String getShareID(String location) {
		String id = null;
		ArrayList<Share> shares = getShares();
		for (int i = 0; i <= shares.size() - 1; i++) {
			if (shares.get(i).getSharelocation().equals(location))
				id = shares.get(i).getShareid();
		}
		return id;
	}

	/**
	 * Wandelt den Pfad des Shares in seinen Namen um
	 * 
	 * @param location
	 *            Pfad des Shares
	 * @return Name des Shares
	 */
	private String getShareName(String location) {
		String name = null;
		ArrayList<Share> shares = getShares();
		for (int i = 0; i <= shares.size() - 1; i++) {
			if (shares.get(i).getSharelocation().equals(location))
				name = shares.get(i).getSharename();
		}
		return name;
	}

	/**
	 * Wandelt den Pfad des Shares in seine URL um
	 * 
	 * @param location
	 *            Pfad des Shares
	 * @return Name des Shares
	 */
	private String getShareURL(String location) {
		String url = null;
		ArrayList<Share> shares = getShares();
		for (int i = 0; i <= shares.size() - 1; i++) {
			if (shares.get(i).getSharelocation().equals(location))
				url = shares.get(i).getShareurl();
		}
		return url;
	}

	/**
	 * Löscht einen Share
	 * 
	 * @param location
	 *            Ordner/Dateipfad von Share
	 * @return Share gelöscht (True, False)
	 */
	public boolean delShare(String location) {
		String id = getShareID(location);
		String result;
		try {
			result = new String(doPost("/core/deleteshare", "shareid=" + id), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return false;
		}
		if (result.contains("<result>1</result>"))
			return true;

		else
			return false;
	}

	/**
	 * Setzt ob man von aussen frei Zugreifen kann auf einen Share oder nicht
	 * 
	 * @param location
	 *            Ordner/Dateipfad von Share
	 * @return Public Access not allowed (True, False)
	 */
	public boolean setPublicAccess(String location) {
		String id = getShareID(location);
		String result;
		try {
			result = new String(
					doPost("/core/setallowpublicaccess",
							"shareid=" + id
									+ "&allowpublicaccess=0&allowpublicupload=0&allowpublicviewonly=0&allowpublicuploadonly=0"),
					"UTF-8");
		} catch (UnsupportedEncodingException e) {
			return false;
		}
		if (result.contains("<result>1</result>"))
			return true;

		else
			return false;
	}

	/**
	 * Entfernt einen User vom Share
	 * 
	 * @param userMail
	 *            Mail des vom Share zu entfernenden User
	 * @param location
	 *            Pfad des Shares
	 * @return User entfernt (True, False)
	 */
	public boolean deleteUserFromShare(String userMail, String location) {
		if(searchUserbyMail(userMail) == false){
			if(searchUserbyUsername(userMail) == null){
				return false;
			}
			userMail = searchUserbyUsername(userMail);
		}
		userMail = stringConvert(userMail);
		String id = getShareID(location);
		String result;
		try {
			result = new String(doPost("/core/deleteuserfromshare", "userid=" + userMail + "&shareid=" + id), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return false;
		}
		if (result.contains("<result>1</result>"))
			return true;

		else
			return false;
	}

	/**
	 * Fügt einen User zu einem Share hinzu
	 * 
	 * @param userMail
	 *            Mail des vom Share hinzuzufügenden User
	 * @param location
	 *            Sharepfad
	 * @return User zum Share hinzugefügt (True, False)
	 */
	public boolean addUsertoShare(String userMail, String location) {
		if(searchUserbyMail(userMail) == false){
			if(searchUserbyUsername(userMail) == null){
				return false;
			}
			userMail = searchUserbyUsername(userMail);
		}
		userMail = stringConvert(userMail);
		String id = getShareID(location);
		String result;
		try {
			result = new String(doPost("/core/addusertoshare", "userid=" + userMail + "&shareid=" + id), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return false;
		}
		if (result.contains("<result>1</result>"))
			return true;

		else
			return false;
	}

	/**
	 * Gibt einem User die Berechtigungen auf einem Share
	 * 
	 * @param userMail
	 *            Mail des Users
	 * @param location
	 *            Sharepfad
	 * @return Userberechtigungen erteilt (True, False)
	 */
	public boolean setUserAccessShare(String userMail, String location, boolean download, boolean write, boolean share,
			boolean sync) {
		userMail = stringConvert(userMail);
		String id = getShareID(location);
		location = stringConvert(location);
		String result;
		try {
			result = new String(doPost("/core/setuseraccessforshare", "shareid=" + id + "&userid=" + userMail
					+ "&download=" + download + "&write=" + write + "&share=" + share + "&sync=" + sync), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return false;
		}
		if (result.contains("<result>1</result>"))
			return true;

		else
			return false;
	}

	/**
	 * Versendet eine Mail mit einem Sharelink
	 * 
	 * @param mailReceiver
	 *            Erhält den Sharelink
	 * @param location
	 *            Pfad zum Share
	 * @param message
	 *            Nachricht
	 * @return Nachricht versandt (True, False)
	 */
	public boolean sendShareMail(String mailReceiver, String location, String message) {
		String id = getShareID(location);
		String url = getShareURL(location);
		String name = getShareName(location);
		String result;
		try {
			result = new String(doPost("/core/sendsharetoemail",
					"from=Rjano Ryser&toemailid=" + mailReceiver + "&sharename=" + name + "&sharelocation=" + location
							+ "&url=" + url + "&message=" + message + "&publicshare=" + id
							+ "&replyto=rjano.ryser@sfs.biz"),
					"UTF-8");
		} catch (UnsupportedEncodingException e) {
			return false;
		}
		if (result.contains("<status>1</status>"))
			return true;

		else
			return false;
	}

	/**
	 * Gibt eine ArrayListe mit allen vorhandenen Shares zurück
	 * 
	 * @return ArrayListe mit allen Shares
	 */
	public ArrayList<Share> getShares() {
		byte[] ByteXML;

		ByteXML = doPost("/core/getshares", "");

		Document doc = byteArraytoDocument(ByteXML);
		NodeList nl = getXPath(doc, "/shares/*");
		String[] order = Share.SHAREORDERLIST;
		ArrayList<String> sharedata = null;
		ArrayList<Share> shares = new ArrayList<Share>();
		int anzEnt = nl.getLength();
		for (int i = 1; i <= anzEnt; i++) {
			String entry = "/shares/share[" + i + "]/*";
			nl = getXPath(doc, entry);
			sharedata = nodeListToArrayList(nl, order);
			Share share = new Share(sharedata);
			shares.add(share);
		}
		return shares;
	}

	/**
	 * Wandelt einen String um damit es mit der URL keine Probleme gibt
	 * 
	 * @param convert
	 *            String zum umwandeln
	 * @return Abgewandelter String
	 */
	private String stringConvert(String convert) {
		if (convert.contains("%")) {
			convert = convert.replace("%", "%25");
		}

		if (convert.contains("@")) {
			convert = convert.replace("@", "%40");
		}

		if (convert.contains("&")) {
			convert = convert.replace("&", "%26");
		}
		return convert;
	}

	/**
	 * Wandelt einen Bytearray in ein Document um
	 * 
	 * @param xml
	 *            Byte[]
	 * @return Document
	 */
	private Document byteArraytoDocument(byte[] xml) {
		InputStream is = new ByteArrayInputStream(xml);

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;

		try {
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(is);
			return doc;
		} catch (SAXException | IOException | ParserConfigurationException e) {
			if (debuglevel == 2)
				e.printStackTrace();
			return null;
		}
	}

	/**
	 * Wandelt das XML-Dokument in eine Nodelist um
	 * 
	 * @param doc
	 *            Dokument
	 * @param xp
	 *            XML-Pfad
	 * @return NodeListe von Pfad
	 */
	private NodeList getXPath(Document doc, String xp) {
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();

		XPathExpression expr = null;

		try {
			expr = xpath.compile(xp);
			NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			return nl;
		} catch (XPathExpressionException e){
			if (debuglevel == 2)
				e.printStackTrace();
			return null;
		}
	}

	/**
	 * Wandelt die Nodelist in eine sortierte ArrayListe(auf die Klassen
	 * angepasst)
	 * 
	 * @param nl
	 *            Nodelist
	 * @param elementNames
	 *            Sortierung der Elemente in der ArrayListe
	 * @return ArrayListe geordnet nach angegebener Klasse
	 */
	private ArrayList<String> nodeListToArrayList(NodeList nl, String[] elementNames) {
		ArrayList<String> al = new ArrayList<String>();
		for (int j = 0; j < nl.getLength(); j++) {
			for (int i = 0; i < nl.getLength(); i++) {
				if (elementNames[i].equals(nl.item(i).getNodeName())) {
					al.add(nl.item(i).getTextContent());
				}
			}
		}
		return al;
	}

	/**
	 * Erstellt ein FilelistResponse Objekt
	 * 
	 * @param xml
	 *            XML-Daten
	 * @return Objekt welches auf die einzelnen Fileattribute überprüft werden kann
	 */
	private FilelistResponse createFilelistResponse(byte[] xml) {
		Document doc = byteArraytoDocument(xml);
		NodeList nl = getXPath(doc, "/entries/meta/*");
		String[] order = FilelistResponse.METAORDERLIST;
		ArrayList<String> filelistresp = nodeListToArrayList(nl, order);
		FilelistResponse filelistresponse = new FilelistResponse(filelistresp);
		nl = getXPath(doc, "/entries/*");
		int anzEnt = nl.getLength() - 1;
		String[] order2 = FilelistResponseEntry.ENTRYORDERLIST;
		for (int i = 1; i <= anzEnt; i++) {
			String entry = "/entries/entry[" + i + "]/*";
			nl = getXPath(doc, entry);
			filelistresp.clear();
			filelistresp = nodeListToArrayList(nl, order2);
			filelistresponse.addEntries(filelistresp);
		}
		return filelistresponse;
	}

	/** Überprüft ob die eigegebene E-Mail Adresse für irgendeinen Nutzer vorhanden ist oder nicht
	 * @param userMail
	 * @return Nutzer vorhanden (True, False)
	 */
	public boolean searchUserbyMail(String userMail) {
		String userMailURL = stringConvert(userMail);
		String result;
		try {
			result = new String(doPost("/core/searchprofiles", "filter=" + userMailURL), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return false;
		}
		if (result.contains("<emailid>"+ userMail +"</emailid>"))
			return true;
		else
			return false;
	}
	
	/** Aus vorhandenem Benutzernamen wird die E-Mail herausgefunden
	 * @param username Benutzername 
	 * @return Nutzer vorhanden (True, False)
	 */
	public String searchUserbyUsername(String username) {
		String userMailURL = stringConvert(username);
		String result = null;
		byte[] XML = doPost("/core/searchprofiles", "filter=" + userMailURL);
		Document doc = byteArraytoDocument(XML);
		NodeList nl = getXPath(doc, "/profiles/profile/username");
		for(int i = 0; i < nl.getLength();i++){
			if(nl.item(i).getTextContent().equals(username)){
				int j = i + 1;
				NodeList nl1 = getXPath(doc, "/profiles/profile["+ j +"]/emailid");
				result = nl1.item(0).getTextContent(); 
			}
		}
		return result;
	}

	/**
	 * Gibt eine XML mit den Files eines Pfads aus
	 * 
	 * @param folder
	 *            Ordner
	 * @return XML der vorhandenen Files
	 */
	public String getFiles(String folder) {
		folder = stringConvert(folder);
		String ByteReconString;
		try {
			ByteReconString = new String(doPost("/core/getfilelist", "path=" + folder), "UTF-8");
			return ByteReconString;
		} catch (UnsupportedEncodingException e) {
			if (debuglevel == 2)
				e.printStackTrace();
			return null;
		}
	}

	/**
	 * Erstellt einen Quickshare
	 * 
	 * @param filepath
	 *            Ordner/Dateipfad für Share
	 * @return Share erstellte (True, False)
	 */
	public boolean setQShare(String location) {
		location = stringConvert(location);
		String result;
		try {
			result = new String(doPost("/core/quickshare", "sharelocation=" + location), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return false;
		}

		if (result.contains("<maxdownloads>0</maxdownloads>"))
			return true;

		else
			return false;
	}

	/**
	 * Gibt die Infos über ein File zurück
	 * 
	 * @param filepath
	 *            Ordner/Dateipfad
	 * @return Fileinfos als XML
	 */
	public String getFileinfo(String filepath) {
		filepath = stringConvert(filepath);
		String result;
		try {
			result = new String(doPost("/core/fileinfo", "path=" + filepath), "UTF-8");
			return result;
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * Erstellt einen Ordner
	 * 
	 * @param path
	 *            Ordnerpfad
	 * @param name
	 *            Ordnername
	 * @return Ordner erstellt (True, False)
	 */
	public boolean createFolder(String path, String name) {
		name = stringConvert(name);
		path = stringConvert(path);
		String result;
		try {
			result = new String(doPost("/app/explorer/createfolder", "name=" + name + "&path=" + path), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return false;
		}

		if (result.contains("<result>1</result>"))
			return true;

		else
			return false;
	}

	/**
	 * Lädt eine Datei hoch
	 * 
	 * @param serverFolder
	 *            Server Zielspeicherort
	 * @param localPath
	 *            Lokaler Speicherort
	 * @return Upload erfolgreich oder nicht
	 */
	public boolean uploadFile(String serverFolder, String localPath) {
		boolean exist = new File(localPath).isFile();
		if(!exist) return false;
		localPath = stringConvert(localPath);
		serverFolder = stringConvert(serverFolder);
		String charset = "UTF-8";
		String[] filename = localPath.split("/");
		String requestURL = url + "/core/upload?appname=explorer&path=" + serverFolder
				+ "&offset=0&complete=1&filename=" + filename[filename.length - 1];
		try {
			MultipartUtility multipart = new MultipartUtility(requestURL, charset, this.cookies);
			File uploadFile = new File(localPath);
			multipart.addFilePart("filename", uploadFile);

			List<String> response = multipart.finish();

			for (String line : response) {
				if (line.equals("OK")) {
					return true;
				}
			}
		} catch (IOException ex) {
			return false;
		}
		return false;
	}

	/**
	 * Löschte eine Datei/Verzeichnis Loescht eine Datei in einem Verzeichnis
	 * 
	 * @param name
	 *            Dateiname
	 * @param path
	 *            Verzeichnis
	 * @return Datei gelöscht oder nicht
	 */
	public boolean deleteFolder(String path, String name) {
		name = stringConvert(name);
		path = stringConvert(path);
		String result;
		try {
			result = new String(doPost("/core/deletefile", "path=" + path + "&name=" + name), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			if (debuglevel == 2)
				e.printStackTrace();
			return false;
		}

		if (result.contains("<result>1</result>"))
			return true;

		else
			return false;
	}

	/**
	 * Lädt Files herunter
	 * 
	 * @param serverPath
	 *            Serverspeicherort
	 * @param localPath
	 *            Lokaler Zielspeicherort
	 * @return File heruntergeladen oder nicht
	 */
	public boolean downloadFiles(String serverPath, String localPath) {
		serverPath = stringConvert(serverPath);
		String[] filename = serverPath.split("/");
		byte[] content = doPost("/core/downloadfile",
				"filepath=" + serverPath + "&filename=" + filename[filename.length - 1]);
		if (content == null)
			return false;
		try {
			Files.write(new File(localPath).toPath(), content, StandardOpenOption.CREATE);
			return true;
		} catch (IOException e) {
			if (debuglevel == 2)
				e.printStackTrace();
			return false;
		}
	}

	/**
	 * Setzt eine URL
	 * 
	 * @param url
	 *            URL
	 */
	
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Führt den Post-Befehl auf den Webserver aus, und gibt Informationen als
	 * Byte[] zurück
	 * 
	 * @param name
	 *            Post-Ort
	 * @param data
	 *            Post-Daten
	 * @return Angeforderte Daten/Antworten als byte[]
	 */
	private byte[] doPost(String name, String data) {
		String type = "application/x-www-form-urlencoded";
		URL u;
		ByteArrayOutputStream baos;
		// String result="";
		try {
			u = new URL(url + name);
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);

			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", type);
			conn.setRequestProperty("Content-Length", String.valueOf(data.length()));
			conn.setRequestProperty("User-Agent", "Mozilla");
			if (cookies != null)
				conn.setRequestProperty("Cookie", cookies);

			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8);
			writer.write(data);
			writer.flush();

			InputStream is = conn.getInputStream();

			baos = new ByteArrayOutputStream();
			int reads = is.read();

			while (reads != -1) {
				baos.write(reads);
				reads = is.read();
			}

			headerFields = conn.getHeaderFields();

		} catch (MalformedURLException | FileNotFoundException e) {
			if (debuglevel == 2)
				e.printStackTrace();
			return null;
		} catch (IOException e) {
			if (debuglevel == 2)
				e.printStackTrace();
			return null;
		}

		// System.out.println("Antwort: "+result);
		return baos.toByteArray();
	}
}
