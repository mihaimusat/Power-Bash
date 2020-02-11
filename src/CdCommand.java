/**
 * @author mihaimusat
 */

public class CdCommand implements Command {

    private String _dir;//director
    private String _result;//rezultat
    private String _error;//eroare
    private Entity _workingDirectory;//directorul de lucru

    public CdCommand(String dir) {
        _dir = dir;
    }

    @Override
    public void execute(Entity workingDirectory) {
        _workingDirectory = workingDirectory;
        Entity newWd = Utils.NavigateToPath(workingDirectory, _dir);//setez noul director de lucru
        if (newWd == null) {
            _error = "cd: " + _dir + ": No such directory";
        } else {
            _workingDirectory = newWd;
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