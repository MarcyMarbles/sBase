package kz.saya.sbase.Repos

import kz.saya.sbase.Entity.Sex
import org.springframework.data.jpa.repository.JpaRepository

interface SexRepository : JpaRepository<Sex, Int>