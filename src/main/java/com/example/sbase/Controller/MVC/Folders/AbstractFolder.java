package com.example.sbase.Controller.MVC.Folders;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Наследуйте от этого класса, чтобы пункт меню был «папкой».
 * Метод возвращает 204, поэтому браузер остаётся на той же странице,
 * а фронт может раскрывать/сворачивать подменю по JS.
 */
public abstract class AbstractFolder {
    @GetMapping
    public ResponseEntity<Void> folder() {
        return ResponseEntity.noContent().build();
    }
}
