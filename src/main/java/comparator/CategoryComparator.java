package comparator;

import model.Task;

import java.util.Comparator;

/**
 * @author krish
 */
public class CategoryComparator  implements Comparator<Task> {
    @Override
    public int compare(Task o1, Task o2) {
        return o1.getCategory().compareTo(o2.getCategory());
    }

}
