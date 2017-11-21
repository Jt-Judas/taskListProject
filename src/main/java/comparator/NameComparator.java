package comparator;

import model.Task;
import java.util.Comparator;

/**
 * @author krish
 */
public class NameComparator implements Comparator<Task>{
    @Override
    public int compare(Task o1, Task o2) {
        return o1.getTask().compareTo(o2.getTask());
    }
}
