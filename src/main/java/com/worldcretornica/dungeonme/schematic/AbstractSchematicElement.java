package com.worldcretornica.dungeonme.schematic;

public abstract class AbstractSchematicElement {

    public abstract String toString();
    
    protected String Sanitize(Object object)
    {
        return (object == null ? "" : object.toString());
    }
}
