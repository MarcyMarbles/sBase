package kz.saya.sbasecore.Service;

import kz.saya.sbasecore.Entity.NotPersistent.Langs;
import kz.saya.sbasecore.Entity.Sex;
import kz.saya.sbasecore.Repositories.SexRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SexService {
    private final SexRepository sexRepository;

    public SexService(SexRepository sexRepository) {
        this.sexRepository = sexRepository;
    }

    public Sex findById(UUID id) {
        return sexRepository.findById(id).orElse(null);
    }

    public Sex findByName(String name, Langs langs) {
        return sexRepository.searchByLang(name, langs.name()).orElse(null);
    }
}
