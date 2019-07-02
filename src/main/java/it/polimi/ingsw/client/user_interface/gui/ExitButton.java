package it.polimi.ingsw.client.user_interface.gui;

import javafx.scene.control.Button;


public class ExitButton extends Button {
    public ExitButton(){
        super("EXIT");
        this.setOnMouseClicked(mouseEvent -> Gui.shutDown());
    }
}
