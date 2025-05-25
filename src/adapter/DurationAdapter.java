package adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;


public class DurationAdapter extends TypeAdapter<Duration> {


    @Override
    public void write(final JsonWriter jsonWriter, final Duration duration) throws IOException {
        if (duration == null) {
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.value(duration.toMinutes()); // Сохраняем длительность в минутах
    }

    @Override
    public Duration read(final JsonReader jsonReader) throws IOException {

        try {
            long minutes = jsonReader.nextLong(); // Читаем число (минуты)
            return Duration.ofMinutes(minutes);
        } catch (NumberFormatException e) {
            throw new IOException("Некорректный формат длительности", e);
        }
    }

}