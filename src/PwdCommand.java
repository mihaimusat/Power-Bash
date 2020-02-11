/**
 * @author mihaimusat
 */

public class PwdCommand implements Command {

    private String _result;//rezultat
    private Entity _workingDirectory;//director de lucru

    public PwdCommand() {}

    @Override
    public void execute(Entity workingDirectory) {
        _workingDirectory = workingDirectory;
        Entity helper = _workingDirectory;
        _result = helper.getName();//obtin numele directorului de lucru
        while (helper.getParent() != null) {
            helper = helper.getParent();//iau parintele acestui director
            if (helper.getParent() != null)
                _result = helper.getName() + "/" + _result;//obtin calea in sens invers
            else
                _result = "/" + _result;//inseamna ca sunt in root
        }
    }

    @Override
    public String getResult() {
        return _result;
    }

    @Override
    public String getError() {
        return null;
    }

    @Override
    public Entity getNewWorkingDirectory() {
        return _workingDirectory;
    }
}