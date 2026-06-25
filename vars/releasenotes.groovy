import java.io.*;
import groovy.io.*;

def call(Map config=[:]){
    def dir = new File(pwd());

    new File(dir.path + '\\releasenotes.txt').withWriter('utf-8')

    dir.eachFileRecurse(FileType.ANY) { file ->
        if(file.isDirectory()){
            writer.writeLine(file.name);
        }else{
            writer.writeLine(file.name + '\t' + file.length());
        }
    }
}