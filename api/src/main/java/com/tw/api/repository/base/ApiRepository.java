package com.tw.api.repository.base;

import org.springframework.data.repository.CrudRepository;

public interface ApiRepository<T, Id> extends CrudRepository<T, Id> {
}
