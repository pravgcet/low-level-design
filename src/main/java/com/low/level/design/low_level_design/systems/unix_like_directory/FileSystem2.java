package com.low.level.design.low_level_design.systems.unix_like_directory;

public class FileSystem2 {

    Folder root, current;

    public FileSystem2(){
        this.root = new Folder("/", null);
        this.current = this.root;
    }

    public void mkdir(String path){
        String[] splitPath = validate(path);
        traverse(splitPath, true);
    }

    public void cd(String path){
        String[] splitPath = validate(path);
        current = traverse(splitPath, false);
    }

    private String[] validate(String path){
        if(null == path){
            throw new IllegalArgumentException("Null path not supported");
        }
        String[] splitPath = path.split("/");
        if(splitPath[splitPath.length-1].contains(".")){
            throw new IllegalArgumentException("Invalid path");
        }
        return splitPath;
    }

    private Folder traverse(String[] path, boolean createDirectory){

        Folder folder = path[0].isEmpty() ? root : current;
        for(String name : path){
            if(name.isEmpty() || name.equals(".")){
                continue;
            }
            if(name.equals("..")){
                folder = folder.getParent();
                continue;
            }
            if(createDirectory){
                folder = folder.getOrCreateSubFolder(name);
            }else{
                folder = folder.getSubFolder(name);
            }
            if(folder == null){
                throw new IllegalArgumentException("Folder not found: "+name);
            }
        }

        return folder;
    }

}
