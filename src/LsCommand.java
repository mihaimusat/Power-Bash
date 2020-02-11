import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author mihaimusat
 */

public class LsCommand implements Command {

    private String _dir;//director
    private String _error;//eroare
    private String _result;//rezultat
    private String _regex;//regex
    private Entity _workingDirectory;//directorul de lucru
    private Boolean _recursive = false;//daca trebuie sa aplic -R sau nu

    public LsCommand(String dir, String argument, String regex) {
        _dir = dir;
        _regex = regex;
        if (argument == null)
            return;
        if (argument.equals("-R"))
            _recursive = true;
    }

    @Override
    public void execute(Entity workingDirectory) {
        _workingDirectory = workingDirectory;
        Entity newWd;
        if (_dir != null)
            newWd = Utils.NavigateToPath(_workingDirectory, _dir);//setez noul director de lucru
        else
            newWd = _workingDirectory;

        if (newWd == null) {
            _error = "ls: " + _dir + ": No such directory";
        } else {
            if (!_recursive)
                _result = nonRecursive(newWd);//inseamna ca am un ls obisnuit
            else {
                _result = "";
                recursive(newWd);//inseamna ca am ls -R
            }
        }
    }

    /**
     * Aplica operatia de ls obisnuita asupra directorului curent
     * @param dir directorul pe care vreau sa aplic comanda
     * @return output-ul ce se obtine in urma operatiei
     */
    private String nonRecursive(Entity dir) {
        StringBuilder result;
        PwdCommand pwdCommand = new PwdCommand();
        pwdCommand.execute(dir);//am nevoie de calea curenta pentru ls
        result = new StringBuilder(pwdCommand.getResult() + ":\n");
        List<String> filesAndFolders = new ArrayList<>();
        for (Entity entity : dir.getFiles()) {
            filesAndFolders.add(entity.getName());//adaug fiecare fisier la lista
        }
        for (Entity entity : dir.getFolders()) {
            filesAndFolders.add(entity.getName());//adaug fiecare folder la lista
        }
        Collections.sort(filesAndFolders);//sortez lista obtinuta
        boolean any = false;//pentru tratarea expresiilor regulate
        for (String fileOrFolder : filesAndFolders) {
            String full = (pwdCommand.getResult().equals("/") ? "/" : pwdCommand.getResult() + "/") + fileOrFolder;
            if (_regex == null || fileOrFolder.matches(_regex)) {
                result.append(full + " ");
                any = true;
            }
        }
        if (any) {
            result = result.deleteCharAt(result.length() - 1);
        }
        result.append("\n");
        return result.toString();//am obtinut outputul comenzii ls
    }

    /**
     * Aplica ls -R asupra directorului curent
     * @param dir directorul asupra caruia vreau sa aplic operatia
     */
    private void recursive(Entity dir) {
        _result += nonRecursive(dir);
        for (Entity child: dir.getFolders()) {
            _result += '\n';
            recursive(child);
        }
    }

    @Override
    public String getError() {
        return _error;
    }

    @Override
    public String getResult() {
        return _result;
    }

    @Override
    public Entity getNewWorkingDirectory() {
        return _workingDirectory;
    }
}