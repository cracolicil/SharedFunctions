import java.io.*;
import groovy.io.*;
import java.util.Calendar.*;
import java.text.SimpleDateFormat;

@NonCPS
def call(Map config=[:]){
    def dir = new File(pwd());

    new File(dir.path + '/releasenotes.txt').withWriter('utf-8'){ writer ->

        dir.eachFileRecurse(FileType.ANY) { file ->
            if(file.isDirectory()){
                writer.writeLine(file.name);
            }else{
                writer.writeLine(file.name + '\t' + file.length());
            }
        }
    }

    def date = new Date();
    def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    echo "Date and Time is: " + sdf.format(date)

    echo "Buil Number is ${BUILD_NUMBER}";
}