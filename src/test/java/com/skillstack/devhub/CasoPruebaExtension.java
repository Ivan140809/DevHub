package com.skillstack.devhub;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.atomic.AtomicBoolean;

public class CasoPruebaExtension implements BeforeAllCallback, AfterEachCallback {

    private static final Path META_FILE = Paths.get("target/caso-prueba-meta.jsonl");
    private static final AtomicBoolean initialized = new AtomicBoolean(false);

    @Override
    public void beforeAll(ExtensionContext context) throws IOException {
        if (initialized.compareAndSet(false, true)) {
            Files.createDirectories(META_FILE.getParent());
            Files.deleteIfExists(META_FILE);
        }
    }

    @Override
    public void afterEach(ExtensionContext context) throws IOException {
        Method method = context.getRequiredTestMethod();
        CasoPrueba meta = method.getAnnotation(CasoPrueba.class);
        if (meta == null) return;

        String line = "{" +
            "\"metodo\":\"" + esc(method.getName())      + "\"," +
            "\"id\":\""      + esc(meta.id())             + "\"," +
            "\"descripcion\":\"" + esc(meta.descripcion()) + "\"," +
            "\"entrada\":\""  + esc(meta.entrada())       + "\"," +
            "\"tipo\":\""     + esc(meta.tipo())          + "\"," +
            "\"esperado\":\"" + esc(meta.esperado())      + "\"" +
            "}";

        synchronized (CasoPruebaExtension.class) {
            Files.write(META_FILE, line.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }
    }

    private String esc(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
