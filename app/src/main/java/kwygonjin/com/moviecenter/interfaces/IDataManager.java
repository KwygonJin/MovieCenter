package kwygonjin.com.moviecenter.interfaces;

import java.io.IOException;
import java.util.List;

/**
 * Created by KwygonJin on 05.12.2015.
 */
public interface IDataManager<T> {

    long save(T t) throws IOException;
    boolean delete(T t) throws IOException;
    T get(String id);
    List<T> getAll();
     boolean contains(T t);
    boolean update(T t);
}
