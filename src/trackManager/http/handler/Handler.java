package trackManager.http.handler;

import trackManager.controllers.TaskManager;

public abstract class Handler {
    protected TaskManager manager;
    public Handler(TaskManager manager) {
        this.manager = manager;
    }
}
