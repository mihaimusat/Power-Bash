import java.util.ArrayList;
import java.util.List;

/**
 * @author mihaimusat
 */

public class CommandExecutor {

    private Entity _wd;//director de lucru
    private int _commandNumber = 1;//numarul comenzii
    private StringBuilder _result = new StringBuilder();//rezultatul comenzii
    private StringBuilder _error = new StringBuilder();//eroarea comenzii
    private List<Command> commandsList = new ArrayList<Command>();//bash-ul este o lista de comenzi

    public CommandExecutor(Entity wd) {
        _wd = wd;
    }

    public void takeCommand(Command order) {
        commandsList.add(order);
    }

    public void executeCommands() {
        for (Command order : commandsList) {
            order.execute(_wd);//execut fiecare comanda din lista
            _result.append(_commandNumber + "\n");
            if (order.getResult() != null) {
                _result.append(order.getResult() + "\n");//retin rezultatul
            }
            _error.append(_commandNumber + "\n");
            if (order.getError() != null) {
                _error.append(order.getError() + "\n");//retin eroarea
            }
            _wd = order.getNewWorkingDirectory();
            _commandNumber++;
        }
        commandsList.clear();
    }

    public String getResult() {
        return _result.toString();
    }

    public String getError() {
        return _error.toString();
    }

}