/**
 * @author mihaimusat
 */

public class CommandFactory {

    private static CommandFactory _instance;//instanta statica a bash-ului

    private CommandFactory() {}

    public static CommandFactory getInstance() {
        if (_instance == null) {
            _instance = new CommandFactory();//o instantiez o singura data
        }
        return _instance;
    }

    public Command CreateCommand(String line) {
        String[] args = line.split(" ");//obtin vectorul de argumente
        switch (args[0]) {
            case "cd":
                return new CdCommand(args[1]);
            case "ls":
                String arg1 = null, arg2 = null, regex = null;
                if (line.contains("grep")) {
                    String[] parts = line.split("( \\| )");
                    regex = parts[1].replace("grep ", "");
                    regex = regex.substring(1, regex.length() - 1);
                    line = parts[0];
                }
                args = line.split(" ");
                if (args.length == 2) {
                    if (args[1].equals("-R")) {
                        arg2 = args[1];
                    } else {
                        arg1 = args[1];
                    }
                } else if (args.length == 3) {
                    if (args[1].equals("-R")) {
                        arg1 = args[2];
                        arg2 = args[1];
                    } else {
                        arg1 = args[1];
                        arg2 = args[2];
                    }
                }
                return new LsCommand(arg1, arg2, regex);
            case "pwd":
                return new PwdCommand();
            case "cp":
                return new CpCommand(args[1], args[2]);
            case "mv":
                return new MvCommand(args[1], args[2]);
            case "rm":
                return new RmCommand(args[1]);
            case "touch":
                return new TouchCommand(args[1]);
            case "mkdir":
                return new MkdirCommand(args[1]);
        }
        return null;
    }
}