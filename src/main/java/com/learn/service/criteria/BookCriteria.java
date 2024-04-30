package com.learn.service.criteria;

import java.io.Serializable;
import com.learn.service.Criteria;

import lombok.Data;
import lombok.NoArgsConstructor;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.StringFilter;

@Data
@NoArgsConstructor
public class BookCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private IntegerFilter id;

    private StringFilter title;

    private IntegerFilter stock;

    private IntegerFilter rating;

    private StringFilter description;

    private InstantFilter releaseDate;

    private IntegerFilter borrowManagementId;

    private IntegerFilter authorId;

    private IntegerFilter categoryId;

    public BookCriteria(BookCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.stock = other.stock == null ? null : other.stock.copy();
        this.rating = other.rating == null ? null : other.rating.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.releaseDate = other.releaseDate == null ? null : other.releaseDate.copy();
        this.borrowManagementId = other.borrowManagementId == null ? null : other.borrowManagementId.copy();
        this.authorId = other.authorId == null ? null : other.authorId.copy();
        this.categoryId = other.categoryId == null ? null : other.categoryId.copy();
    }

    @Override
    public Criteria copy() {
        return new BookCriteria(this);
    }

    public IntegerFilter id() {
        if (id == null) {
            id = new IntegerFilter();
        }
        return id;
    }

    public StringFilter title() {
        if (title == null) {
            title = new StringFilter();
        }
        return title;
    }

    public IntegerFilter stock() {
        if (stock == null) {
            stock = new IntegerFilter();
        }
        return stock;
    }

    public IntegerFilter rating() {
        if (rating == null) {
            rating = new IntegerFilter();
        }
        return rating;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public InstantFilter releaseDate() {
        if (releaseDate == null) {
            releaseDate = new InstantFilter();
        }
        return releaseDate;
    }

    public IntegerFilter borrowManagementId() {
        if (borrowManagementId == null) {
            borrowManagementId = new IntegerFilter();
        }
        return borrowManagementId;
    }

    public IntegerFilter authorId() {
        if (authorId == null) {
            authorId = new IntegerFilter();
        }
        return authorId;
    }

    public IntegerFilter categoryId() {
        if (categoryId == null) {
            categoryId = new IntegerFilter();
        }
        return categoryId;
    }

}
