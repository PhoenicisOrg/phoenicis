package com.phoenicis.library;

import com.phoenicis.library.dto.ShortcutDTO;

public class ShortcutRunner {
    public void run(ShortcutDTO shortcutDTO) {
        System.out.println("I will run");
        System.out.println(shortcutDTO.getScript());
    }
}
