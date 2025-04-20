package com.example.sbase.Repos

import com.example.sbase.Entity.Sex
import org.springframework.data.jpa.repository.JpaRepository

interface SexRepository : JpaRepository<Sex, Int>