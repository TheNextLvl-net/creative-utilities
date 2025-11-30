package net.thenextlvl.utilities.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import org.jspecify.annotations.NullMarked;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;

@NullMarked
public final class GsonFile<R> {
    private static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    private final Path file;
    private final R defaultRoot;

    private R root;
    private boolean loaded;

    public GsonFile(Path file, R root) {
        this.defaultRoot = root;
        this.file = file;
        this.root = root;
    }

    public R getRoot() {
        if (loaded) return root;
        loaded = true;
        return root = load();
    }

    private R load() {
        if (!Files.isRegularFile(file)) return getRoot();
        try (var reader = new JsonReader(new InputStreamReader(
                Files.newInputStream(file, READ),
                StandardCharsets.UTF_8
        ))) {
            R root = GSON.fromJson(reader, this.root.getClass());
            return root != null ? root : defaultRoot;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public GsonFile<R> setRoot(R root) {
        this.loaded = true;
        this.root = root;
        return this;
    }

    public GsonFile<R> save(FileAttribute<?>... attributes) {
        try {
            var root = getRoot();
            Files.createDirectories(file.getParent(), attributes);
            try (var writer = new BufferedWriter(new OutputStreamWriter(
                    Files.newOutputStream(file, WRITE, CREATE, TRUNCATE_EXISTING),
                    StandardCharsets.UTF_8
            ))) {
                GSON.toJson(root, root.getClass(), writer);
                return this;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public GsonFile<R> validate() {
        if (!Files.isRegularFile(file)) return this;
        var defaultTree = GSON.toJsonTree(defaultRoot, root.getClass());
        var currentTree = GSON.toJsonTree(getRoot(), root.getClass());
        var validatedTree = validate(defaultTree, currentTree);
        if (currentTree.equals(validatedTree)) return this;
        return setRoot(GSON.<R>fromJson(validatedTree, root.getClass()));
    }

    private static JsonElement validate(JsonElement defaultTree, JsonElement currentTree) {
        if (!defaultTree.isJsonObject() || !currentTree.isJsonObject()) return currentTree;
        return validate(defaultTree.getAsJsonObject(), currentTree.getAsJsonObject());
    }

    private static JsonObject validate(JsonObject defaultTree, JsonObject currentTree) {
        var currentCopy = currentTree.deepCopy();
        filterUnused(defaultTree, currentCopy);
        fillMissing(defaultTree, currentCopy);
        return currentCopy;
    }

    private static void fillMissing(JsonObject defaultTree, JsonObject currentCopy) {
        defaultTree.entrySet().stream()
                .filter(entry -> !currentCopy.has(entry.getKey()))
                .forEach(entry -> currentCopy.add(entry.getKey(), entry.getValue()));
    }

    private static void filterUnused(JsonObject defaultTree, JsonObject currentCopy) {
        currentCopy.entrySet().removeIf(entry -> !defaultTree.has(entry.getKey()));
    }
}
