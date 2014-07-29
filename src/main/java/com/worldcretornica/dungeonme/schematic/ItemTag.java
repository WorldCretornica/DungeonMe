package com.worldcretornica.dungeonme.schematic;

import java.util.List;

public class ItemTag extends AbstractSchematicElement {

    private Integer repaircost;
    private List<Ench> enchants;
    private Display display;
    private String author;
    private String title;
    private List<String> pages;

    public ItemTag(Integer repaircost, List<Ench> enchants, Display display, String author, String title, List<String> pages) {
        this.repaircost = repaircost;
        this.enchants = enchants;
        this.display = display;
        this.author = author;
        this.title = title;
        this.pages = pages;
    }

    public Integer getRepairCost() {
        return repaircost;
    }

    public List<Ench> getEnchants() {
        return enchants;
    }

    public Display getDisplay() {
        return display;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getPages() {
        return pages;
    }

    public String toString() {
        return "{" + this.getClass().getName() + 
                ": repaircost=" + Sanitize(repaircost) +
                ", enchants=" + Sanitize(enchants) +
                ", display=" + Sanitize(display) +
                ", author=" + Sanitize(author) +
                ", title=" + Sanitize(title) +
                ", pages=" + Sanitize(pages) + "}";
    }
}
