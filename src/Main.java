import java.io.*;

/**
 * @author mihaimusat
 */

public class Main {
    public static void main(String[] args) {

        Entity root = new Entity("/", null, true);
        CommandExecutor ex = new CommandExecutor(root);

        File file = new File(args[0]);
        BufferedReader bf = null;

        try {
            bf = new BufferedReader(new FileReader(file));
            String st;
            while ((st = bf.readLine()) != null) {
                ex.takeCommand(CommandFactory.getInstance().CreateCommand(st));
            }
            ex.executeCommands();
            try (PrintWriter out = new PrintWriter(args[1])) {
                out.print(ex.getResult());
            }
            try (PrintWriter out = new PrintWriter(args[2])) {
                out.print(ex.getError());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}