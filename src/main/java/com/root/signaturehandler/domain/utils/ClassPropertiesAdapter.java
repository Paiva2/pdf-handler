package com.root.signaturehandler.domain.utils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.FeatureDescriptor;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        List<String> targetFields = Arrays.stream(
                this.target.getPropertyDescriptors()
        ).map(FeatureDescriptor::getName).collect(Collectors.toList());

        Arrays.stream(this.source.getPropertyDescriptors())
                .filter(field -> {
                    return this.source.getPropertyValue(field.getName()) != null
                            && field.getName().hashCode() != "class".hashCode()
                            && field.getName().hashCode() != "id".hashCode();
                })
                .forEach(field -> {
                    boolean targetHasField = targetFields.contains(field.getName());

                    if (!targetHasField) return;

                    String fieldName = field.getName();
                    Object fieldValue = this.source.getPropertyValue(fieldName);

                    this.target.setPropertyValue(fieldName, fieldValue);
                });
    }
}
