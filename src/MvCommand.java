/**
 * @author mihaimusat
 */

public class MvCommand implements Command {

    private String _dirSource;//director sursa
    private String _dirDest;//director destinatie
    private String _result;//rezultat
    private String _error;//eroare
    private Entity _workingDirectory;//director de lucru

    public MvCommand(String dirSource, String dirDest) {
        _dirSource = dirSource;
        _dirDest = dirDest;
    }

    @Override
    public void execute(Entity workingDirectory) {
        _workingDirectory = workingDirectory;
        String[] splitter = _dirSource.split("/");
        String file = splitter[splitter.length - 1];
        String parentDir = "";
        for (int i = 0; i < splitter.length - 1; i++)
            parentDir += splitter[i] + "/";
        Entity parentFolder = Utils.NavigateToPath(_workingDirectory, parentDir);//setez directorul de lucru la parinte
        if (parentFolder != null) {
            if (!(parentFolder.hasChildFile(file) || parentFolder.hasChildFolder(file)) && !file.equals(".")) {
                _error = "mv: cannot move " + _dirSource + ": No such file or directory";
            } else {
                Entity helper = Utils.NavigateToPath(_workingDirectory, _dirSource);
                Entity parentFolderDst = Utils.NavigateToPath(_workingDirectory, _dirDest);
                if (helper == null) { //daca e fisier
                    if (parentFolderDst == null) {
                        _error = "mv: cannot move into " + _dirDest + ": No such directory";
                    } else if (parentFolderDst.hasChildFile(file)) {
                        _error = "mv: cannot move " + _dirSource + ": Node exists at destination";
                    } else {
                        parentFolderDst.addFile(file);
                    }
                        parentFolder.removeChildFile(file);
                } else { //daca e folder
                    if (parentFolderDst == null) {
                        _error = "mv: cannot move into " + _dirDest + ": No such directory";
                    } else if (parentFolderDst.hasChildFolder(file)) {
                        _error = "mv: cannot move " + _dirSource + ": Node exists at destination";
                    }  else {
                        MkdirCommand mk = new MkdirCommand(helper.getName());
                        mk.execute(parentFolderDst);
                        Entity dstFolder = parentFolderDst.getChildFolder(helper.getName());
                        recursive(dstFolder, helper);

                        PwdCommand pwdWorking = new PwdCommand();
                        pwdWorking.execute(_workingDirectory);//aplic pwd pe directorul de lucru
                        String working = pwdWorking.getResult();//obtin calea curenta a directorului de lucru

                        pwdWorking.execute(helper);
                        String source = pwdWorking.getResult();

                        if (working.startsWith(source)) {
                            String diff = working.replaceFirst(source + "/", "");
                            if (file.equals(".")) {
                                _workingDirectory = Utils.NavigateToPath(workingDirectory, _dirDest + "/" + helper.getName() );
                            } else {
                                _workingDirectory = Utils.NavigateToPath(dstFolder, diff);
                            }
                        }
                        helper.getParent().removeChildFolder(helper.getName());

                    }
                }
            }
        } else {
            _error = "mv: cannot move " + _dirSource + ": No such file or directory";
        }
    }

    /**
     * Functie care realizeaza o mutare recursiva a dir in newDir
     * @param newDir directorul unde vreau sa fac mutarea
     * @param dir directorul pe care vreau sa il mut
     */
    private void recursive(Entity newDir, Entity dir) {
        for (Entity entity : dir.getFiles()) {
            newDir.addFile(entity.getName());
        }
        for (Entity entity : dir.getFolders()) {
            newDir.addFolder(entity.getName());
            recursive(newDir.getChildFolder(entity.getName()), entity);
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