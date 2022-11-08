package cn.edu.bistu.cs.ir.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Paths;

public class FileUtils {

    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

    public static boolean isDirExists(String parent, String... names){
        File f = Paths.get(parent, names).toFile();
        return f.isDirectory()&&f.exists();
    }

    /**
     * 删除指定路径下的所有子目录和文件，但不删除该路径对应的目录本身
     * @param path 指定的路径
     * @return 删除操作是否成功
     */
    public static boolean deleteSubDirs(String path){
        File parent = new File(path);
        if(parent.exists()&&parent.isDirectory()){
            //path存在且是一个目录
            File[] files = parent.listFiles();
            if(files==null){
                //files为空只有可能是： 1）parent不是一个目录；2）IO异常
                //parent必然是一个目录，所以如果files==null，说明出现IO异常
                return false;
            }
            for(File sub: files){
                if(!delete(sub)){
                    return false;
                }
            }
            return true;
        }else{
            //path不存在或者不是一个目录
            return false;
        }
    }

    /**
     * 递归删除file代表的文件或目录
     * @param file 要删除的文件或目录
     * @return 删除操作是否成功
     */
    private static boolean delete(File file){
        if(file==null||!file.exists()){
            return false;
        }
        if(!file.isFile()){
            //删除目录
            File[] subs = file.listFiles();
            if(subs==null){
                return false;
            }
            for(File sub: subs){
                if(!delete(sub)){
                    return false;
                }
            }
            //子文件/目录全部删除了
        }
        //删除文件
        return file.delete();
    }
}
