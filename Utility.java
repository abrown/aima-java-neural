package aima.core.learning.neural2;

import aima.core.learning.neural.*;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.*;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 *
 * @author andrew
 */
public class Utility {

    public static void main(String[] args) {
        try {
            Class[] classes = Utility.getClasses("aima.core.learning.neural2");
            for (Class c : classes) {
                System.out.println(Utility.summarize(c) + "\n\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    public static Class[] getClasses(Path path) {
//        ArrayList<String> classes = new ArrayList<String>();
//        ArrayList<Class> _classes = new ArrayList<Class>();
//        Utility u = new Utility();
//        ClassLoader classLoader = u.getClass().getClassLoader();
//        // get files
//        File[] files = Utility.getFiles(path);
//        for (File f : files) {
//            int end = f.getName().indexOf(".java");
//            classes.add(f.getName().substring(0, end));
//        }
//        // initialize reflection classes
//        for (String c : classes) {
//            try {
//                c = "aima.core.learning.neural." + c;
//                System.out.println(c);
//                _classes.add(Class.forName(c, true, classLoader));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        // return
//        return _classes.toArray(new Class[1]);
//    }

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
}
