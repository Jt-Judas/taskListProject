package comparator;

import model.Task;

import java.util.Comparator;

/**
 * @author krish
 */
public class DueComparator implements Comparator<Task> {
    @Override
    public int compare(Task o1, Task o2) {
        return o1.getRequiredBy().compareTo(o2.getRequiredBy());
    }

}
