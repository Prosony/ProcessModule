package services.file;

import debug.LocalLog;

import java.io.*;

public class FileService {

    private LocalLog LOG = LocalLog.getInstance();

    public boolean copyFolders(String directoryFrom, String directoryTo){
        File sourceFolder = new File(directoryFrom);
        File resultFolder = new File(directoryTo);

        if (sourceFolder.exists()){
            boolean isCreate = false;
            if (!resultFolder.exists()){
                LOG.info("[FileService][copyFolders] directory does not exist, path: "+directoryTo);
                isCreate = resultFolder.mkdirs();
            }else{
                isCreate = true;
            }
            if (isCreate){
                LOG.success("[FileService][copyFolders] directory was created, path: "+directoryTo);
                File source = new File(directoryFrom);
                LOG.info(directoryFrom);
                String files[] = source.list();
                for (String file: files){
                    LOG.info(file);
                }
                copyFiles(new File(directoryFrom),new File(directoryTo));
            }else{
                LOG.error("[FileService][copyFolders] directory was not created, path: "+directoryTo);
            }
        }else{
            LOG.error("[FileService][copyFolders] Directory does not exist, path: "+directoryFrom);
            return false;
        }
        return false;
    }

    private void copyFiles(File source, File dest){
        if(source.isDirectory()){
            boolean isCreate = false;

            if(!dest.exists()){ //if directory not exists, create it
                isCreate = dest.mkdirs();
                LOG.info("[FileService][copyFiles] Directory copied from " +source+ " to " +dest);
            }else{
                isCreate = true;
            }

            if (isCreate){
                String files[] = source.list();//list all the directory contents
                if (files != null){
                    for (String file : files) {
                        File srcFile = new File(source, file);//construct the src and dest file structure
                        File destFile = new File(dest, file);
                        copyFiles(srcFile,destFile);//recursive copy
                    }
                }else{
                    LOG.error("[FileService][copyFiles] This algo is shit, source.list() is null");
                }
            }else{
                LOG.error("[FileService][copyFiles] Directory was not created, path: "+dest);
            }
        }else{
            InputStream in = null; //if file, then copy it. Use bytes stream to support all file types
            try {
                in = new FileInputStream(source);
                OutputStream out = new FileOutputStream(dest);
                byte[] buffer = new byte[1024];
                int length;
                //copy the file content in bytes
                while ((length = in.read(buffer)) > 0){
                    out.write(buffer, 0, length);
                }
                in.close();
                out.close();
                LOG.info("[FileService][copyFiles] File copied from " + source + " to " + dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}