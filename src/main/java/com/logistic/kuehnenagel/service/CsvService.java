package com.logistic.kuehnenagel.service;


import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
/**
 * Service class for processing CSV files.
 * It provides method which returns steam of chunks of the provided class.
 */
public class CsvService {

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    /**
     * Creates <code>Stream</code> statement.
     *
     * @param reader Stream reader.
     * @param tClass the type of the object for CSV mapping. See {@link com.logistic.kuehnenagel.domain.csv.CsvCity} f.e.
     *
     * @return a chunk of streams.
     */
    public <T> Stream<List<T>> getStreamFromCSV(Reader reader, Class<T> tClass) {
        CsvToBean<T> csvToBean =  new CsvToBeanBuilder<T>(reader)
                .withType(tClass)
                .withIgnoreLeadingWhiteSpace(true)
                .build();

        return chunk(csvToBean.stream(), batchSize);
    }

    private <T> Stream<List<T>> chunk(Stream<T> stream, int size) {
        Iterator<T> iterator = stream.iterator();
        Iterator<List<T>> listIterator = new Iterator<>() {

            public boolean hasNext() {
                return iterator.hasNext();
            }

            public List<T> next() {
                List<T> result = new ArrayList<>(size);
                for (int i = 0; i < size && iterator.hasNext(); i++) {
                    result.add(iterator.next());
                }
                return result;
            }
        };
        return StreamSupport.stream(((Iterable<List<T>>) () -> listIterator).spliterator(), false);
    }
}
