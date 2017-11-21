package model;

public class Task {

    private int id;
    private String task;
    private String userId;
    private String requiredBy;
    private String priority;
    private String category;

    public Task(int id, String task, String userId, String dueDate, String priority, String category) {
        this.id = id;
        this.task = task;
        this.userId = userId;
        this.requiredBy = dueDate;
        this.priority = priority;
        this.category = category;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getRequiredBy() {
        return requiredBy;
    }

    public void setRequiredBy(String requiredBy) {
        this.requiredBy = requiredBy;
    }

    public String getPriority() { return priority; }

    public void setPriority(String priority) {this.priority = priority; }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
