/**
 * @author mihaimusat
 */

public class CpCommand implements Command {

    private String _dirSource;//director sursa
    private String _dirDest;//director destinatie
    private String _result;//rezultat
    private String _error;//eroare
    private Entity _workingDirectory;//director de lucru

    public CpCommand(String dirSource, String dirDest) {
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
            parentDir += splitter[i] + "/";//obtin directorul parinte
        Entity parentFolder = Utils.NavigateToPath(_workingDirectory, parentDir);//setez folderul parinte
        if (parentFolder != null) {
            if (!(parentFolder.hasChildFile(file) || parentFolder.hasChildFolder(file)) && !file.equals(".")) {
                _error = "cp: cannot copy " + _dirSource + ": No such file or directory";
            } else {
                Entity helper = Utils.NavigateToPath(_workingDirectory, _dirSource);//directorul de lucru e sursa
                Entity parentFolderDst = Utils.NavigateToPath(_workingDirectory, _dirDest);
                if (helper == null) { //daca e fisier
                    if (parentFolderDst == null) {
                        _error = "cp: cannot copy into " + _dirDest + ": No such directory";
                    } else if (parentFolderDst.hasChildFile(file)) {
                        _error = "cp: cannot copy " + _dirSource + ": Node exists at destination";
                    } else {
                        parentFolderDst.addFile(file);
                    }
                } else { //daca e folder
                    if (parentFolderDst == null) {
                        _error = "cp: cannot copy into " + _dirDest + ": No such directory";
                    } else if (parentFolderDst.hasChildFolder(file)) {
                        _error = "cp: cannot copy " + _dirSource + ": Node exists at destination";
                    }  else {
                        MkdirCommand mk = new MkdirCommand(helper.getName());
                        mk.execute(parentFolderDst);
                        recursive(parentFolderDst.getChildFolder(helper.getName()), helper);
                    }
                }
            }
        } else {
            _error = "cp: cannot copy " + _dirSource + ": No such file or directory";
        }
    }

    /**
     * Functie prin care fac o copiere recursiva din dir in newDir
     * @param newDir folderul in care fac copierea folderului fisier cu fisier
     * @param dir directorul sursa
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