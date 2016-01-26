package be.bendem.chess;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;

public abstract class BaseApplication extends Application {

    private final Map<Object, Scene> scenes;
    private final Map<String, Object> controllers;
    private final Set<URL> stylesheets;
    protected Stage stage;

    protected BaseApplication(URL... stylesheets) {
        this(Arrays.asList(stylesheets));
    }

    protected BaseApplication(Collection<URL> stylesheets) {
        this.scenes = new HashMap<>();
        controllers = new HashMap<>();
        this.stylesheets = new HashSet<>(stylesheets);
    }

    public <T> T open(String fxml, String title, Stage stage) throws IOException {
        this.stage = stage;

        T controller = setScene(fxml);

        stage.setTitle(title);
        stage.show();

        return controller;
    }

    public <T> T setScene(String fxml) {
        if(stage == null) {
            throw new IllegalStateException("No stage setup yet");
        }

        @SuppressWarnings("unchecked")
        T controller = (T) controllers.computeIfAbsent(fxml, f -> {
            FXMLLoader loader = getFxmlLoader(f);
            Parent app;
            try {
                app = loader.load();
            } catch(IOException e) {
                throw new RuntimeException(e);
            }

            for(URL stylesheet : stylesheets) {
                app.getStylesheets().add(stylesheet.toExternalForm());
            }

            T loadedController = loader.getController();
            Scene newScene = new Scene(app);
            scenes.put(loadedController, newScene);
            return loadedController;
        });

        stage.setScene(scenes.get(controller));
        return controller;
    }

    private FXMLLoader getFxmlLoader(String fxml) {
        FXMLLoader loader = new FXMLLoader(getResource(fxml));
        loader.setControllerFactory(clazz -> {
            try {
                Constructor<?> constructor;
                try {
                    constructor = clazz.getConstructor(getClass());
                } catch(NoSuchMethodException e) {
                    constructor = clazz.getConstructor(BaseApplication.class);
                }
                return constructor.newInstance(this);
            } catch(NoSuchMethodException e) {
                try {
                    return clazz.newInstance();
                } catch(InstantiationException | IllegalAccessException e1) {
                    throw new RuntimeException("Could not instantiate controller for " + clazz, e);
                }
            } catch(InstantiationException | IllegalAccessException
                | InvocationTargetException e) {
                throw new RuntimeException("Could not instantiate controller for " + clazz, e);
            }
        });
        return loader;
    }

    protected static URL getResource(String name) {
        return getResource(BaseApplication.class, name);
    }

    protected static URL getResource(Class<?> clazz, String name) {
        return Objects.requireNonNull(clazz.getClassLoader().getResource(name), "Resource not found: " + name);
    }

    protected InputStream getResourceStream(String name) {
        return Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(name), "Resource not found: " + name);
    }

    protected byte[] getResourceBytes(String name) {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int read;
        try(InputStream resource = getResourceStream(name)) {
            while((read = resource.read(buffer)) > 0) {
                out.write(buffer, 0, read);
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        return out.toByteArray();
    }

}
