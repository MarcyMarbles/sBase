package kz.saya.sbase.POJOs;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class MenuPOJO {

    private UUID id;
    private String name;
    private String icon;
    private String url;
    private boolean group;
    private int rank;

    private final List<MenuPOJO> children = new ArrayList<>();
}
