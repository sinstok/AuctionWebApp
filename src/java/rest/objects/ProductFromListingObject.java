/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest.objects;

/**
 *
 * @author Ragnhild
 */
public class ProductFromListingObject {
    String fieldName;
    Long id;

    public ProductFromListingObject(String fieldName, Long id) {
        this.fieldName = fieldName;
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    
}
