/**
 * @author mihaimusat
 */

public interface Command {

    void execute(Entity workingDirectory);//aplica comanda pe directorul de lucru
    String getResult();//obtine rezultat
    String getError();//obtine eroare
    Entity getNewWorkingDirectory();//obtine director de lucru nou
}