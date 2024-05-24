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
    private UIManager uiManager;

    @Getter
    private static Util util;

    public Handler(DataManager dataManager, UIManager uiManager) {
        this.dataManager = dataManager;
        this.uiManager = uiManager;
    }

    public void start() {
        util = new Util();

        uiManager.startApp();
    }

}
