package kz.saya.sbase.Repos

import kz.saya.sbase.Entity.Sex
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface SexRepository : JpaRepository<Sex, Int> {
    // Find by localized name
    @Query(
        "select s from Sex s where" +
                " (:lang = 'RU' and lower(s.langValue1) like lower(concat('%', :value, '%')) ) or " +
                " (:lang = 'EN' and lower(s.langValue2) like lower(concat('%', :value, '%')) )"
    )
    fun searchByLang(
        @Param("value") value: String,
        @Param("lang") lang: String
    ): Sex;
}