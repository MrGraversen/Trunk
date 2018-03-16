package io.graversen.trunk.fx;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Provides some helper methods to aid JavaFX development.
 *
 * @author Martin
 */
public class FxUtils
{
    private FxUtils()
    {

    }

    public static Stage centerOnScreen(Stage stage)
    {
        final double width = stage.getWidth();
        final double height = stage.getHeight();
        final Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        stage.setX((screenBounds.getWidth() - width) / 2);
        stage.setY((screenBounds.getHeight() - height) / 2);

        return stage;
    }

    public static void toFront(Stage stage)
    {
        stage.setAlwaysOnTop(true);
        stage.toFront();
        stage.requestFocus();
        stage.setAlwaysOnTop(false);
    }
}
