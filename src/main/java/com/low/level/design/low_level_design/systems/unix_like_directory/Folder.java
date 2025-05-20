package com.low.level.design.low_level_design.systems.unix_like_directory;

import java.util.HashMap;
import java.util.Map;

public class Folder {
    private String name;
    private Folder parent, self;
    private Map<String, Folder> subFolders;
    private Map<String, File> files;

    public Folder(String name, Folder parent){
        this.name = name;
        this.parent = parent;
        subFolders = new HashMap<>();
        files = new HashMap<>();
    }

    public Folder getParent(){
        return this.parent;
    }

    public Folder getSubFolder(String name){
        return this.subFolders.get(name);
    }

    public Folder getOrCreateSubFolder(String name){
        return this.subFolders.computeIfAbsent(name, k -> new Folder(k, this));
    }

    public void createFile(String name){
        this.files.computeIfAbsent(name, File::new);
    }
}
