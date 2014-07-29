package com.worldcretornica.dungeonme.schematic;

import java.util.List;

public class ItemTag {

    private Integer repaircost;
    private List<Ench> enchants;
    private List<Display> display;
    private String author;
    private String title;
    private List<String> pages;
    
    public ItemTag(Integer repaircost, List<Ench> enchants, List<Display> display, String author, String title, List<String> pages)
    {
        this.repaircost = repaircost;
        this.enchants = enchants;
        this.display = display;
        this.author = author;
        this.title = title;
        this.pages = pages;
    }
    
    public Integer getRepairCost()
    {
        return repaircost;
    }
    
    public List<Ench> getEnchants()
    {
        return enchants;
    }
    
    public List<Display> getDisplay()
    {
        return display;
    }
    
    public String getAuthor()
    {
        return author;
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public List<String> getPages()
    {
        return pages;
    }
}
