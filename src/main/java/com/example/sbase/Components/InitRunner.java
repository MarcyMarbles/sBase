package com.example.sbase.Components;

import com.example.sbase.Service.MenuService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitRunner implements CommandLineRunner {
    private final MenuService menuService;

    public InitRunner(MenuService menuService) {
        this.menuService = menuService;
    }

    @Override
    public void run(String... args) throws Exception {
        menuService.init();
    }
}
