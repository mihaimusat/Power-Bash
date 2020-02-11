/**
 * @author mihaimusat
 */

public class RmCommand implements Command {

    private String _parentDir;//director parinte
    private String _file;//fisier
    private String _result;//rezultat
    private String _error;//eroare
    private Entity _workingDirectory;//director de lucru

    public RmCommand(String dir) {
        String[] splitter = dir.split("/");
        _file = splitter[splitter.length - 1];
        _parentDir = "";
        for (int i = 0; i < splitter.length - 1; i++)
            _parentDir += splitter[i] + "/";
    }

    @Override
    public void execute(Entity workingDirectory) {
        _workingDirectory = workingDirectory;
        Entity newWd = Utils.NavigateToPath(workingDirectory, _parentDir);//setez directorul de lucru la parinte
        if (newWd == null) {
            _error = "rm: cannot remove " + _parentDir + _file + ": No such file or directory";
            return;
        }
        if (_file.equals("..") || _file.equals(".")) { //daca am "." sau ".." nu am nimic de facut
            return;
        }

        PwdCommand pwdWorking = new PwdCommand();
        pwdWorking.execute(_workingDirectory);
        String working = pwdWorking.getResult();

        pwdWorking.execute(newWd);
        String toDelete = pwdWorking.getResult();

        //daca sunt in root nu am nimic de facut
        if (working.startsWith(toDelete + (toDelete.equals("/") ? "" : "/") + _file)) {
            return;
        }

        //altfel, inseamna ca trebuie sa sterg tot
        if (newWd.getChildFolder(_file) != null) {
            newWd.removeChildFolder(_file);
        } else if (newWd.getChildFile(_file) != null) {
            newWd.removeChildFile(_file);
        } else {
            _error = "rm: cannot remove " + _parentDir + _file + ": No such file or directory";
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