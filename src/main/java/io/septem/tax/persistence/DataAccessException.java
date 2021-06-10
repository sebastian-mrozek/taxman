package io.septem.tax.persistence;

import java.io.IOException;

public class DataAccessException extends RuntimeException {
    public DataAccessException(String s, IOException e) {
        super(s, e);
    }
}
