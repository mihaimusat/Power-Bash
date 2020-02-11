/**
 * @author mihaimusat
 */

public class TouchCommand implements Command {

    private String _parentDir;//directorul parinte
    private String _file;//fisier
    private String _result;//rezultat
    private String _error;//eroare
    private Entity _workingDirectory;//director de lucru

    public TouchCommand(String dir) {
        String[] splitter = dir.split("/");
        _file = splitter[splitter.length - 1];
        _parentDir = "";
        for (int i = 0; i < splitter.length - 1; i++)
            _parentDir += splitter[i] + "/";
    }

    @Override
    public void execute(Entity workingDirectory) {
        _workingDirectory = workingDirectory;
        Entity newWd = Utils.NavigateToPath(workingDirectory, _parentDir);//seteaza directorul de lucru la parinte
        if (newWd == null) {
            _error = "touch: " + _parentDir.substring(0, _parentDir.length() - 1) + ": No such directory";
        } else if (newWd.hasChildFile(_file)) {
            PwdCommand pwd = new PwdCommand();
            pwd.execute(newWd);//obtin calea curenta sa vad daca exista
            _error = "touch: cannot create file " +
                    pwd.getResult() + (pwd.getResult().equals("/") ? "" : "/") +
                    _file + ": Node exists";
        } else {
            newWd.addFile(_file);//altfel, creez fisierul
        }
    }

    @Override
    public String getResult() {
        return _result;
    }

    @Override
    public String getError() {
        return _error;
    }

    @Override
    public Entity getNewWorkingDirectory() {
        return _workingDirectory;
    }
}