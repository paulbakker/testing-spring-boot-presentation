package com.netflix.testingdemo.lolomo.repository;

import com.netflix.testingdemo.lolomo.generated.types.Category;
import com.netflix.testingdemo.lolomo.generated.types.Show;
import jakarta.persistence.*;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "shows")
public class ShowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private Set<Category> categories;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShowEntity that = (ShowEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(description, that.description) && Objects.equals(categories, that.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, categories);
    }

    @Override
    public String toString() {
        return "ShowEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", categories=" + categories +
                '}';
    }

    public Show asShow() {
        return Show.newBuilder()
                .title(title)
                .description(description)
                .categories(categories != null ? categories.stream().toList() : Collections.emptyList())
                .build();
    }

    public static ShowEntity fromShow(Show show) {
        var entity = new ShowEntity();
        entity.setTitle(show.getTitle());
        entity.setDescription(show.getDescription());
        entity.setCategories(Set.copyOf(show.getCategories()));
        return entity;
    }
}
