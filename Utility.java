package aima.core.learning.neural2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 *
 * @author andrew
 */
public class Utility {

    public static final String PACKAGE = "aima.core.learning.neural2";
    public static final String DIR = "C:\\Data\\andrew\\code\\aima-java\\aima-core\\src\\main\\java\\aima\\core\\learning\\neural2";
    
    public static void main(String[] args) {
//        try {
//            Class[] classes = Utility.getClasses(Utility.PACKAGE);
//            for (Class c : classes) {
//                System.out.println(Utility.summarize(c) + "\n\n");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        for(File f : Utility.getFiles(Paths.get(Utility.DIR)) ){
            String content = Utility.getFile(f.toPath());
            System.out.println(f.getName());
            System.out.println(Utility.uncode(content));
        }
    }
    
    
    public static String uncode(String classString){
        int start = 0;
        int end = classString.indexOf("{") + 1; // starts after class... {
        int next = 0;
        ArrayList<String> bodies = new ArrayList<>();
        while( end != -1 ){
            start = classString.indexOf("{", end);
            end = Utility.findEndBrace(classString, start);
            if( start > 0 && end > 0 ){
                System.out.println(start+" "+end);
                //bodies.add(classString.substring(start+1, end));
            }      
        }
        // remove
        for(String b : bodies){
            classString = classString.replace(b, " ");
        }
        // return
        return classString;
    }
    
    private static int findEndBrace(String s, int start){
        int end = s.indexOf("}", start);
        int next = s.indexOf("{", start);
        if( end < next ){
            return end;
        }
        else{
            return findEndBrace(s, end + 1);
        }      
    }
    
    private static String remove(String s, int start, int end){
        return s.substring(0, start) + s.substring(end);
    }
    
    
    /**
     * Summarizes classes
     * @param c
     * @return 
     */
    public static String summarize(Class c) {
        // get name
        String out = "CLASS:\n\t" + c.getName() + "\n\n";
        // get properties
        out += "PROPERTIES:\n";
        for (Field f : c.getDeclaredFields()) {
            out += "\t" + f.toString() + "\n";
        }
        // get methods
        out += "METHODS:\n";
        for (Method m : c.getDeclaredMethods()) {
            out += "\t" + m.toString() + "\n";
        }
        // return
        return out;
    }

    /**
     * Gets classes by package name
     * @param packageName
     * @return
     * @throws ClassNotFoundException
     * @throws IOException 
     */
    public static Class[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(Utility.findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

    /**
     * Returns list of files in folder
     * @param folder
     * @return 
     */
    private static File[] getFiles(Path folder) {
        File[] list = null;
        File dir = new File(folder.toString());
        if (dir.isDirectory()) {
            FileFilter filter = new FileFilter() {
                public boolean accept(File file) {
                    if (file.isFile() && !file.isDirectory()) {
                        String filename = file.getName();
                        if (filename.endsWith(".java")) {
                            return true;
                        }
                    }
                    return false;
                }
            };
            list = dir.listFiles(filter);
        }
        // return
        return list;
    }
    
    /**
     * Returns file as string
     * @param folder
     * @return 
     */
    private static String getFile(Path file) {
        File f = new File(file.toString());
        StringBuilder s = new StringBuilder("");
        try{
            BufferedReader r = new BufferedReader( new FileReader(f) );
            String line = "";
            String ls = System.getProperty("line.separator");
            while( (line = r.readLine()) != null ){
                s.append(line);
                s.append(ls);
            }
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        // return
        return s.toString();
    }
}
