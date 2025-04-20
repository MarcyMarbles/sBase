package kz.saya.sbase.XmlUtils;

import kz.saya.sbase.Entity.Roles;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class RoleDeserializer extends JsonDeserializer<Roles> {

    @Override
    public Roles deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String roleName = p.getText();
        Roles role = new Roles();
        role.setName(roleName);
        return role;
    }
}
