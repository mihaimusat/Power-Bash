import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mihaimusat
 */

public class Entity {

    private String _name;//nume
    private List<Entity> _children = null;//initial lista abstracta de entitati e nula
    private Entity _parent;//parinte

    /**
     * Constructor pentru tipul abstract de obiecte(fisier sau folder)
     * cu care lucreaza sistemul de fisiere
     * @param name numele entitatii
     * @param parent parintele entitatii
     * @param isFolder in functie de variabila(true sau false), se creaza sau nu o lista de entitati
     */
    public Entity(String name, Entity parent, boolean isFolder) {
        _name = name;
        _parent = parent;
        if (isFolder) {
            _children = new ArrayList<>();
        }
    }

    public Entity getParent() {
        return _parent;
    }

    public String getName() {
        return _name;
    }

    public boolean isFolder() {
        return _children != null;//un folder are in el fisiere sau foldere
    }

    public void addFolder(String name) {
        if (isFolder()) { //trebuie sa fim intr-un director pentru a putea crea unul
            _children.add(new Entity(name, this, true));
        }
    }

    public void addFile(String name) {
        if (isFolder()) { //trebuie sa fim intr-un director pentru a putea crea unul
            _children.add(new Entity(name, this, false));
        }
    }

    public List<Entity> getFiles() {
        if (isFolder()) {
            return _children.stream().filter(item -> !item.isFolder())
                    .sorted(Comparator.comparing(Entity::getName))
                    .collect(Collectors.toList());//obtine lista de entitati sortata lexicografic
        }
        return null;
    }

    public List<Entity> getFolders() {
        if (isFolder()) {
            return _children.stream().filter(Entity::isFolder)
                    .sorted(Comparator.comparing(Entity::getName))
                    .collect(Collectors.toList());//obtine lista de entitati sortata lexicografic
        }
        return null;
    }

    public boolean hasChildFolder(String folderName) {
        return getFolders().stream().anyMatch(i -> i.getName().equals(folderName));
    }

    public boolean hasChildFile(String fileName) {
        return getFiles().stream().anyMatch(i -> i.getName().equals(fileName));
    }

    public void removeChildFolder(String folderName) {
        _children.remove(getChildFolder(folderName));
    }

    public void removeChildFile(String fileName) {
        _children.remove(getChildFile(fileName));
    }

    public Entity getChildFolder(String folderName) {
        return getFolders().stream().filter(i -> i.getName().equals(folderName)).findFirst().orElse(null);
    }

    public Entity getChildFile(String fileName) {
        return getFiles().stream().filter(i -> i.getName().equals(fileName)).findFirst().orElse(null);
    }
}