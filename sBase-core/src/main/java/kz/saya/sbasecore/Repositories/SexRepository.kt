package kz.saya.sbasecore.Repositories;

import kz.saya.sbasecore.Entity.Sex
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.Optional
import java.util.UUID

interface SexRepository : JpaRepository<Sex, UUID> {
    // Find by localized name
    @Query(
        "select s from Sex s where" +
                " (:lang = 'RU' and lower(s.langValue1) like lower(concat('%', :value, '%')) ) or " +
                " (:lang = 'EN' and lower(s.langValue2) like lower(concat('%', :value, '%')) )"
    )
    fun searchByLang(
        @Param("value") value: String,
        @Param("lang") lang: String
    ): Optional<Sex>;
}