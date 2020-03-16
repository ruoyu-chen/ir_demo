package cn.edu.bistu.cs.ir.data;

import java.util.List;

/**
 * 图书对象
 * @author ruoyuchen
 */
public class Book {

    private String id;

    private String title;

    private List<String> authors;

    private String price;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
