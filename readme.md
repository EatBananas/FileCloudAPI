# FileCloudAPI
This is an Example for a FileCloud API written in Java.

## FileCloud

[![FileCloud Logo][filecloud_logo]][filecloud]

FileCloud is a Dropbox-like file sharing and sync solution. You can run FileCloud on
your own IT Infrastructure or let it be Hosted by FileCloud. With FileCloud you have
securely access to your data from anywhere using any device. You can share Files by
creating public or passwort protected links or share it to other users.

## Sample Code

```
Filecloud fc = new Filecloud();

fc.setUrl("https://url.to.filecloud")
if (!fc.login(USERNAME, PASSWORD)){
	System.out.println("Login Fehlgeschlagen");
	return;
}
System.out.println("Login OK");

System.out.println(fc.getFiles("/ordner/pfad"));
System.out.println(fc.downloadFiles("/ordner/pfad/dateiname.ext", "C:/test/dateiname.ext"));
System.out.println(fc.getFilesordered("/ordner/pfad").getEntry(5).getName());
System.out.println(fc.createFolder("/ordner/pfad", "test"));
System.out.println(fc.deleteFolder("/ordner/pfad", "test"));
System.out.println(fc.uploadFile("/ordner/pfad", "C:/test/dateiname.ext"));
System.out.println(fc.setQShare("/ordner/pfad/dateiname.ext"));
System.out.println(fc.getShares().get(0).getShareid());
System.out.println(fc.delShare("/ordner/pfad/dateiname.ext"));
System.out.println(fc.setPublicAccess("/ordner/pfad/dateiname.ext"));
System.out.println(fc.addUsertoShare("test@gmail.com", "/ordner/pfad/dateiname.ext"));
System.out.println(fc.setUserAccessShare("test@gmail.com", "/ordner/pfad/dateiname.ext", true, true, true, true));
System.out.println(fc.sendShareMail("test@gmail.com", "/ordner/pfad/dateiname.ext", "Dies ist eine Sharemail"));
System.out.println(fc.searchUserbyUsername("lord"));
```

## Licence

This Filecloud API is released under the [MIT license](LICENSE), and written in [Java](https://www.java.com).


[filecloud_logo]: https://www.getfilecloud.com/supportdocs/download/attachments/1966229/logoblue.png
[filecloud]: https://www.getfilecloud.com/