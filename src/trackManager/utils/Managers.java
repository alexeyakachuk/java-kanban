package trackManager.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import trackManager.adapters.LocalDateTimeAdapter;
import trackManager.controllers.HistoryManager;
import trackManager.controllers.InMemoryHistoryManager;
import trackManager.controllers.InMemoryTaskManager;
import trackManager.controllers.TaskManager;

import java.time.LocalDateTime;
public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        return gsonBuilder.create();
    }
}
