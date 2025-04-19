package com.example.sbase.Entity.NotPersistent;

import lombok.Data;

import java.util.List;

@Data
public class TaskStats {
    public int total;
    public int completed;
    public int expired;
    public int inProgress;

    public TaskStats(List<Tasks> tasks) {
        this.total = tasks.size();
        this.completed = (int) tasks.stream().filter(Tasks::isCompleted).count();
        this.expired = (int) tasks.stream().filter(Tasks::isExpired ).count();
        this.inProgress = total - completed - expired;
    }
}
