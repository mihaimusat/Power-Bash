/**
 * @author mihaimusat
 */
public class MkdirCommand implements Command {

    private String _parentDir;//director parinte
    private String _folder;//folder
    private String _result;//rezultat
    private String _error;//eroare
    private Entity _workingDirectory;//director de lucru

    public MkdirCommand(String dir) {
        String[] splitter = dir.split("/");
        _folder = splitter[splitter.length - 1];
        _parentDir = "";
        for (int i = 0; i < splitter.length - 1; i++)
            _parentDir += splitter[i] + "/";
    }

    @Override
    public void execute(Entity workingDirectory) {
        _workingDirectory = workingDirectory;
        Entity newWd = Utils.NavigateToPath(workingDirectory, _parentDir);//setez directorul de lucru la parinte
        if (newWd == null) {
            _error = "mkdir: " + _parentDir.substring(0, _parentDir.length() - 1) + ": No such directory";
        } else if (newWd.hasChildFolder(_folder) || newWd.hasChildFile(_folder)) {
            PwdCommand pwd = new PwdCommand();
            pwd.execute(newWd);//obtin calea folosind pwd ca sa sa arat ca exista deja
            _error = "mkdir: cannot create directory " +
                    pwd.getResult() + (pwd.getResult().equals("/") ? "" : "/") +
                    _folder + ": Node exists";
        } else {
            newWd.addFolder(_folder);
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