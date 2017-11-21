package comparator;

import model.Task;

import java.util.Comparator;

/**
 * @author krish
 */
public class PriorityComparator implements Comparator<Task> {
    @Override
    public int compare(Task o1, Task o2) {
        return o1.getPriority().compareTo(o2.getPriority());
    }

}
