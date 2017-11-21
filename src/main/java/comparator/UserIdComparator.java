package comparator;

import model.Task;

import java.util.Comparator;

/**
 * @author krish
 */
public class UserIdComparator implements Comparator<Task> {
    @Override
    public int compare(Task o1, Task o2) {
        return o1.getUserId().compareTo(o2.getUserId());
    }
}
