package nz.ac.auckland.cer.model;

import java.util.List;


public class Page<T> {
    public List<T> content;
    public boolean last;
    public int totalPages;
    public int totalElements;
    public String sort;
    public boolean first;
    public int numberOfElements;
    public int size;
    public int number;

    public Page(List<T> paginatedResults, int totalElements, String orderBy, int size, int page) {
        int totalPages = (int)Math.ceil((float)totalElements / (float)size);
        int numberOfElements = paginatedResults.size();

        this.content = paginatedResults;
        this.last = (page + 1) >= totalPages;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.sort = orderBy;
        this.first = page == 0;
        this.numberOfElements = numberOfElements;
        this.size = size;
        this.number = page;
    }
}