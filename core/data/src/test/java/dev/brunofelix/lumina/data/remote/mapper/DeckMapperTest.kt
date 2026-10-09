package dev.brunofelix.lumina.data.remote.mapper

import dev.brunofelix.lumina.data.remote.dto.DeckDto
import dev.brunofelix.lumina.domain.model.Deck
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.util.Date

class DeckMapperTest : DescribeSpec({

    describe("DeckDto.toDomain") {
        it("should map the document id, name and card count") {
            val dto = DeckDto(name = "Spanish Travel", cardCount = 12, createdAt = Date(0L))

            dto.toDomain(id = "deck-1") shouldBe Deck(id = "deck-1", name = "Spanish Travel", cardCount = 12)
        }

        it("should keep the defaults of an empty document") {
            DeckDto().toDomain(id = "deck-2") shouldBe Deck(id = "deck-2", name = "", cardCount = 0)
        }
    }
})
