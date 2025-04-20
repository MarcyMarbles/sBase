package kz.saya.sbase.POJOs;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MenuPOJO {

    private int    id;
    private String name;
    private String icon;
    private String url;
    private boolean group;

    private final List<MenuPOJO> children = new ArrayList<>();
}
