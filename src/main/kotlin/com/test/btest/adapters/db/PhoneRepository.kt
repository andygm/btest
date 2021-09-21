package com.test.btest.adapters.db

import com.test.btest.adapters.db.dto.PhoneDto
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface PhoneRepository : ReactiveCrudRepository<PhoneDto, Int> {}