package org.dacs.quackstagramdatabase;

import lombok.Getter;
import lombok.extern.apachecommons.CommonsLog;
import org.dacs.quackstagramdatabase.data.DataManager;
import org.dacs.quackstagramdatabase.ui.UIManager;
import org.dacs.quackstagramdatabase.util.Util;
import org.springframework.stereotype.Component;

@Component
public class Handler {

    @Getter
    private static DataManager dataManager;

    @Getter
    private static UIManager uiManager;

    @Getter
    private static Util util;

    public void start() {
        dataManager = new DataManager();
        uiManager = new UIManager();
        util = new Util();

        uiManager.startApp();
    }

}
