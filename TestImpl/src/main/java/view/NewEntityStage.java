package view;

import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class NewEntityStage extends Stage {

    public NewEntityStage(){
        CreateEntityScene createEntityScene = new CreateEntityScene(new StackPane());
        this.setScene(createEntityScene);
        initModality(Modality.APPLICATION_MODAL);
        show();
    }

}
