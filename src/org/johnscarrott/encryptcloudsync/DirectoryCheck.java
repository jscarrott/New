package org.johnscarrott.encryptcloudsync;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Path;


public class DirectoryCheck {
	String dirLoc;
	

	public DirectoryCheck(String dirIn){
		dirLoc = dirIn;
	}
	
	public Path watchServ() throws IOException, InterruptedException{
        	FileSystem fs = FileSystems.getDefault();
        	WatchService ws = null;
        	 ws = fs.newWatchService();
        	 Path path = fs.getPath(dirLoc);
             path.register(ws, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY,  StandardWatchEventKinds.ENTRY_DELETE);

             WatchKey key = ws.take();
             
             List<WatchEvent<?>> events = key.pollEvents();
	
             for (WatchEvent<?> event : events) {
                 if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                     return (Path) event.context();

                 }
                 if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
                	 return (Path) event.context();
                 }
                 if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                	 return (Path) event.context();
                 }
             }
			return path;

}
	public void pollDirectory() throws IOException{
		FileSystem fs = FileSystems.getDefault();
		Path unecnryptedDir = fs.getPath(dirLoc);
		File[] filesReturn = new File[unecnryptedDir.toFile().listFiles().length];
		filesReturn = unecnryptedDir.toFile().listFiles();
		filesReturn.toString();
		delete(new File(dirLoc + "\\" + "listoffiles.txt"));
		BufferedWriter filesInDirOut = new BufferedWriter(new FileWriter(dirLoc + "\\" + "listoffiles.txt"));
		filesInDirOut.write("FirstLine");
		filesInDirOut.newLine();
		for(int counter = 0; counter < filesReturn.length; counter++){
			filesInDirOut.write(filesReturn[counter].toString());
			filesInDirOut.newLine();
			filesInDirOut.newLine();
		}
		filesInDirOut.close();
		
	}

	public ArrayList<File> readDirectory() throws IOException{

		BufferedReader filesInDirIn = new BufferedReader(new FileReader(dirLoc + "\\" + "listoffiles.txt"));
		ArrayList<File> listOFiles = new ArrayList<File>();
		String buff;
		while(filesInDirIn.readLine() != null){
			buff = filesInDirIn.readLine();
			
			if(buff != null){
				//buff = buff.replaceAll(" ", "%20");
				
				listOFiles.add(new File(buff));
			}
			
		}
		return listOFiles;
		
		
	}
	public void delete(File toDelete){
		
		if(toDelete.exists()){
			toDelete.delete();
		}
		
	}
}
