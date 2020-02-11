/**
 * @author mihaimusat
 */

public class Utils {
    /**
     * Functie ajutatoare prin intermediul careia ajung de la wd la path
     * @param wd directorul de lucru
     * @param path calea la care vreau sa ajung
     * @return entitatea obtinuta
     */
    public static Entity NavigateToPath(Entity wd, String path) {

        if (path.startsWith("/")) {
            while (wd.getParent() != null) {
                wd = wd.getParent();
            }
            if (path.equals("/")) {
                return wd;
            }
            path = path.substring(1);
        }
        String[] folders =  path.split("/");
        if (path == "") {
            return wd;
        }
        for (int i = 0; i < folders.length; i++) {
            if (folders[i].equals("..")) {
                wd = wd.getParent();
                if (wd == null) {
                    return null;
                }
            } else if (folders[i].equals(".")) {
            } else if (wd.hasChildFolder(folders[i])) {
                wd = wd.getChildFolder(folders[i]);
            } else {
                return null;
            }
        }
        return wd;
    }
}