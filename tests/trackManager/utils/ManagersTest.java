package trackManager.utils;

import org.junit.jupiter.api.Test;
import trackManager.controllers.HistoryManager;
import trackManager.controllers.TaskManager;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManagersTest {

    @Test
    void getDefaultTest() {
        TaskManager aDefault = Managers.getDefault();

        assertNotNull(aDefault, "TaskManager не найден");
    }

    @Test
    void getDefaultHistoryTest() {
        HistoryManager defaultHistory = Managers.getDefaultHistory();

        assertNotNull(defaultHistory, "InMemoryHistoryManager не найден");
    }
}