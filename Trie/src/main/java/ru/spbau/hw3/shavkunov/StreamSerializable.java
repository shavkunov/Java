package ru.spbau.hw3.shavkunov;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Класс реазиующий этот интерфейс умеет сериализоваться и десериализоваться.
 */
public interface StreamSerializable {
    void serialize(OutputStream out) throws IOException;
    void deserialize(InputStream in) throws IOException;
}
