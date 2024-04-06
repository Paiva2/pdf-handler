package com.root.signaturehandler.domain.utils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.util.Arrays;

public class ClassPropertiesAdapter<T> {
    BeanWrapper target;
    BeanWrapper source;

    public ClassPropertiesAdapter(T target, T source) throws RuntimeException {
        this.target = new BeanWrapperImpl(target);
        this.source = new BeanWrapperImpl(source);
        
        if (!this.target.getClass().equals(this.source.getClass())) {
            throw new IllegalArgumentException("Classes must be equals to have their values copied.");
        }
    }

    public void copyNonNullProperties() {
        PropertyDescriptor[] fields = this.source.getPropertyDescriptors();

        Arrays.stream(fields).forEach(field -> {
            String fieldName = field.getName();
            Object fieldValue = this.source.getPropertyValue(fieldName);

            boolean nonUpdatableField =
                    fieldName.hashCode() == "class".hashCode() || fieldName.hashCode() == "id".hashCode();

            if (!nonUpdatableField && fieldValue != null) {
                this.target.setPropertyValue(fieldName, fieldValue);
            }
        });
    }
}
