package org.dacs.quackstagramdatabase;

import lombok.Data;
import lombok.Getter;
import lombok.extern.apachecommons.CommonsLog;
import org.dacs.quackstagramdatabase.data.DataManager;
import org.dacs.quackstagramdatabase.ui.UIManager;
import org.dacs.quackstagramdatabase.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Data
public class Handler {

    @Getter
    private DataManager dataManager;

    @Getter
    private static UIManager uiManager;

    @Getter
    private static Util util;

    @Autowired
    public Handler(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void start() {
        uiManager = new UIManager();
        util = new Util();

        uiManager.startApp();
    }

}
