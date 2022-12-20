package ru.clevertec.test.checkapp.util;

import java.util.List;

public interface ModelMapper<E,M> {
    E toEntity(M model);
    M toModel(E entity);
    List<E> toEntityList(List<M> modelList);
    List<M> toModelList(List<E> entityList);
}
