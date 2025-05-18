package com.example.demo.entity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class JsonConverter implements AttributeConverter<ResultEntity<?>, String> {

    private final ObjectMapper objectMapper;

    public JsonConverter() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.activateDefaultTyping(
            LaissezFaireSubTypeValidator.instance,
            ObjectMapper.DefaultTyping.NON_FINAL,
            JsonTypeInfo.As.PROPERTY
        );
    }

    @Override
    public String convertToDatabaseColumn(ResultEntity<?> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error serializing ResultEntity to JSON", e);
        }
    }

    @Override
    public ResultEntity<?> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, ResultEntity.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error deserializing ResultEntity from JSON", e);
        }
    }
}
