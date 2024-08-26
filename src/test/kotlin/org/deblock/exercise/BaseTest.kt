package org.deblock.exercise

import Deblock
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(classes = [Deblock::class])
@ActiveProfiles("test")
open class BaseTest {

}