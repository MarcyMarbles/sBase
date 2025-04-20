package kz.saya.sbase.Service;

import kz.saya.sbase.Entity.NotPersistent.Langs;
import kz.saya.sbase.Entity.Sex;
import kz.saya.sbase.Repos.SexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SexService {
    private final SexRepository sexRepository;

    public SexService(SexRepository sexRepository) {
        this.sexRepository = sexRepository;
    }

    public Sex findById(Integer id) {
        return sexRepository.findById(id).orElse(null);
    }

    public Sex findByName(String name, Langs langs) {
        return sexRepository.searchByLang(name, langs.name());
    }
}
