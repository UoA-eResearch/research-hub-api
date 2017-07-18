package nz.ac.auckland.cer.model;

import java.util.List;

public class Page<T> {
    public List<T> content;
    public boolean last;
    public int totalPages;
    public int totalElements;
    public boolean sort;
    public boolean first;
    public int numberOfElements;
    public int size;
    public int number;
}