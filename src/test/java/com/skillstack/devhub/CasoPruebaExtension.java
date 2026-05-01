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
            "\"method\":\"" + esc(method.getName())       + "\"," +
            "\"id\":\""      + esc(meta.id())              + "\"," +
            "\"descripcion\":\"" + esc(meta.descripcion()) + "\"," +
            "\"entrada\":\""  + esc(meta.entrada())        + "\"," +
            "\"tipo\":\""     + esc(meta.tipo())           + "\"," +
            "\"esperado\":\"" + esc(meta.esperado())       + "\"" +
            "}\n";

        synchronized (CasoPruebaExtension.class) {
            Files.write(META_FILE, line.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }
    }

    private static String esc(String s) {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"':  sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n");  break;
                case '\r': sb.append("\\r");  break;
                case '\t': sb.append("\\t");  break;
                default:
                    if (c < 0x20) sb.append(String.format("\\u%04x", (int) c));
                    else          sb.append(c);
            }
        }
        return sb.toString();
    }
}
