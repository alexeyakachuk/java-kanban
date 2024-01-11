package trackManager.utils;

import trackManager.controllers.HistoryManager;
import trackManager.controllers.InMemoryHistoryManager;
import trackManager.controllers.InMemoryTaskManager;
import trackManager.controllers.TaskManager;

public class Managers {



    public static TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public static HistoryManager getDefaultHistory() {

        return new InMemoryHistoryManager();
    }
}
