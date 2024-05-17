package org.dacs.quackstagramdatabase;

import lombok.Getter;
import org.dacs.quackstagramdatabase.data.DataManager;
import org.dacs.quackstagramdatabase.ui.UIManager;
import org.dacs.quackstagramdatabase.util.Util;

public class Handler {

    @Getter
    private static DataManager dataManager;
    @Getter
    private static UIManager uiManager;
    @Getter
    private static Util util;

    public static void main(String[] args) {
        dataManager = new DataManager();
        uiManager = new UIManager();
        util = new Util();

        uiManager.startApp();
    }

}
